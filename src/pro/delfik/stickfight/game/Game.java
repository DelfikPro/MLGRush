package pro.delfik.lmao.stickfight.game;

import lib.I;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.stickfight.SFPlugin;
import pro.delfik.lmao.stickfight.StickFight;
import pro.delfik.lmao.stickfight.util.FixedArrayList;
import pro.delfik.lmao.stickfight.util.Vec3i;
import pro.delfik.lmao.util.Cooldown;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Game {
	
	// Максимальное количество одновременных игр
	protected final static int MAX_RUNNING_GAMES = 20;
	
	// Список активных игр
	private final static FixedArrayList<Game> runningGames = new FixedArrayList<>(MAX_RUNNING_GAMES);
	
	// Команды на арене. Соответствие: Цвет команды - команда.
	private final Teams<SFTeam> teams;
	
	// Карта, на которой идёт игра
	private final Map map;
	
	// Таймер до конца игры
	private volatile int remaining = 300;
	
	// Номер игры в списке активных игр
	private final int id;
	
	// Таймер до конца игры в шедулере баккита
	private BukkitTask task;
	
	// Отдельный скорборд для игры
	private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	private final Objective objective = scoreboard.registerNewObjective("stickfight", "dummy");
	
	/**
	 * Создаёт и начинает игру на выбранной карте:
	 * Игроки видят обратный отсчёт, в это время карта вставляется из схематики
	 * На координаты {@code id * 500, 60, 0}
	 * @param map Карта, на которой будет идти игра
	 * @param teams Сформированные массивы игроков
	 * @throws NoFreeMapsException Если количество активных игр превысит максимальное
	 */
	protected Game(Map map, Teams<Person[]> teams) throws NoFreeMapsException {
		// Поиск свободного сектора
		id = runningGames.firstEmpty();
		if (id == -1) throw new NoFreeMapsException();
		// Всё хорошо, добавляем и начинаем игру
		runningGames.set(id, this);
		this.map = map;
		
		// Настраиваем скорборд
		objective.setDisplayName("§d§lStick§c§lFight");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		// Перенос игроков из массива в SFTeam'ы
		this.teams = teams.convert((color, players) -> new SFTeam(this, color, players));
		
		// Обратный отсчёт
		Cooldown cooldown = new Cooldown("SF_" + id, 4, null, this::start);
		
		// Генерация карты
		buildMap();
	}
	
	/**
	 * Начать игру: телепортировать игроков на карту, выдать предметы и начать отсчёт до конца игры.
	 */
	public void start() {
		task = I.timer(this::tick, 20); // Обратный отсчёт
		respawnPlayers(); // Сбросить игроков
		forPlayers(p -> p.sendTitle("&aИгра началась!")); // Сбросить нахуй с обрыва лакая
	}
	
	/**
	 * Тик таймера до конца игры. Как только время достигает нуля, автоматически вычисляется победитель.
	 */
	private void tick() {
		if (--remaining > 0) return;
		task.cancel(); // Завершить тиканье таймера
		forPlayers(p -> p.sendSubtitle("Время истекло.")); // Отправить игрокам причину конца игры
		finish(computeWinner());
	}
	
	/**
	 * Рассчитать победителя игры.
	 * @return Команду, набравшую наибольшее количество очков, либо {@code null}, если несколько команд набрали наибольшее число очков.
	 */
	private SFTeam computeWinner() {
		SFTeam winner = null;
		boolean conflict = false;
		for (SFTeam team : teams.values()) {
			if (winner == null) winner = team;
			else if (winner.getPoints() == team.getPoints()) conflict = true;
			else if (winner.getPoints() < team.getPoints()) {
				conflict = false;
				winner = team;
			}
		}
		if (conflict) return null;
		else return winner;
	}
	
	/**
	 * Закончить игру.
	 * @param winner Команда, в пользу которой закончится игра.
	 */
	public void finish(SFTeam winner) {
		if (winner == null) {
			tie();
			return;
		}
		forTeams(team -> {
			boolean victory = winner.getColor() == team.getColor();
			for (Person p : team.getPlayers()) {
				p.sendTitle(victory ? "§a§lПобеда!" : "§c§lПоражение!");
				StickFight.toLobby(p);
			}
			
		});
	}
	/**
	 * Закончить игру в ничью.
	 */
	private void tie() {
		forTeams(team -> {
			for (Person p : team.getPlayers()) {
				p.sendTitle("§e§lНичья.");
				StickFight.toLobby(p);
			}
		});
	}
	
	public void buildMap() {
		SFPlugin.schematics.loadSchematic(map.getSchematic(), sectorize(0, 0, 0));
	}
	
	private Vec3i sectorize(int x, int y, int z) {
		return new Vec3i(500 * (id + 1) + x, y + 60, z);
	}
	
	// Итератор по командам
	protected void forTeams(Consumer<SFTeam> teamConsumer) {
		for (SFTeam team : teams.values()) teamConsumer.accept(team);
	}
	
	// Итератор по игрокам в команде
	protected void forPlayers(Consumer<Person> personConsumer) {
		for (SFTeam team : teams.values()) for (Person p : team.getPlayers()) personConsumer.accept(p);
	}
	
	/**
	 * Список всех игроков, которые участвуют в игре
	 * @return Все игроки, участвующие в этой игре
	 */
	protected List<Person> getPlayers() {
		List<Person> list = new ArrayList<>(map.getPlayersInTeam() * teams.size());
		forTeams(t -> list.addAll(t.getPlayers()));
		return list;
	}
	
	/**
	 * @return Карта, на которой идёт игра
	 */
	public Map getMap() {
		return map;
	}
	
	/**
	 * @return Номер сектора, в котором идёт игра
	 */
	public int getID() {
		return id;
	}
	protected Scoreboard getScoreboard() {
		return scoreboard;
	}
	protected Objective getObjective() {
		return objective;
	}
	
	public void breakBed(Map.Color target, String breaker) {
	
	}
	
	// Сбросить игроков
	protected void respawnPlayers() {
		forTeams(SFTeam::resetPlayers);
	}
	
	public static class NoFreeMapsException extends Exception {}
	
}
