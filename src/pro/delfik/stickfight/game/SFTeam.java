package pro.delfik.lmao.stickfight.game;

import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.stickfight.Items;
import pro.delfik.lmao.stickfight.game.Map.Color;
import pro.delfik.lmao.stickfight.util.FixedArrayList;

import java.util.Collection;

public class SFTeam {
	
	private final FixedArrayList<Person> players;
	private final Game game;
	private final Color color;
	private volatile int points = 0;
	private Score score;
	private Team scoreboardTeam;
	
	/**
	 * Создаёт новую команду
	 * @param game Игра, которой принадлежит команда
	 * @param color Цвет команды для отделения её от других команд
	 */
	public SFTeam(Game game, Color color, Person[] players) {
		this.players = players == null ? new FixedArrayList<>(game.getMap().getPlayersInTeam()) : new FixedArrayList<>(players);
		this.game = game;
		this.color = color;
		
		// Разбираемся со скорбордом
		this.score = game.getObjective().getScore(color.toString());
		this.scoreboardTeam = game.getScoreboard().registerNewTeam(color.toString());
		scoreboardTeam.setPrefix(color.getColor());
		scoreboardTeam.setAllowFriendlyFire(false);
		scoreboardTeam.setCanSeeFriendlyInvisibles(true);
		for (Person player : players) {
			player.getHandle().setScoreboard(game.getScoreboard());
			scoreboardTeam.addEntry(player.getName());
		}
	}
	
	// Геттеры
	public Game getGame() {return game;}
	public Collection<Person> getPlayers() {return players;}
	public Color getColor() {return color;}
	public int getPoints() {return points;}
	
	
	/**
	 * Добавление очка за сломанную кровать
	 */
	public void addPoint() {
		score.setScore(++points);
	}
	
	/**
	 * Проверка на наличие игрока в команде
	 * @param player Игрок, которого нужно проверить
	 * @return Содержит ли список команды игрока
	 */
	public boolean contains(String player) {
		// Пробежка по всем игрокам и сравнение по нику
		for (Person person : players) if (person != null && person.getName().equalsIgnoreCase(player)) return true;
		return false;
	}
	
	/**
	 * Добавляет игрока в список команды
	 * @param p Игрок, которого нужно добавить
	 */
	public void addPlayer(Person p) {
		if (this.contains(p.getName())) return; // Одного игрока нельзя добавить дважды
		players.add(p);
		scoreboardTeam.addEntry(p.getName());
	}
	
	/**
	 * Удаление игрока из списка команды
	 * @param p Игрок, которого нужно удалить
	 */
	public void removePlayer(Person p) {
		players.remove(p);
		scoreboardTeam.removeEntry(p.getName());
	}
	
	/**
	 * Сброс игроков к началу игры:
	 * Телепортация на точки спавна, экипировка предметами
	 */
	public void resetPlayers() {
		for (Person p : players) resetPlayer(p);
	}
	// Сбросить отдельного игрока
	protected void resetPlayer(Person p) {
		p.teleport(game.getMap().getSpawnLocation(color));
		Inventory inv = p.getHandle().getInventory();
		inv.setItem(0, Items.STICK_DEFAULT);
		for (int i = 1; i < 9; i++) inv.setItem(i, Items.BLOCKS_DEFAULT);
	}
	
}
