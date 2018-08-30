package pro.delfik.mlg;

import implario.net.Packet;
import implario.net.packet.PacketUpdateTop;
import implario.util.Converter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import pro.delfik.lmao.command.handle.PersonNotFoundException;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.core.connection.Connect;
import pro.delfik.mlg.side.RedSide;
import pro.delfik.mlg.side.Side;
import pro.delfik.mlg.side.BlueSide;
import pro.delfik.lmao.util.Cooldown;
import pro.delfik.lmao.util.U;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Sector {
	
	public static final int SECTORS_IN_ROW = 6;
	public static final int MAPS = 9;
	
	// blue 0 | red 1
	public static Sector[] ingame = new Sector[MAPS * SECTORS_IN_ROW];
	public static final HashMap<String, Sector> byname = new HashMap<>();
	
	public final Scoreboard sb;
	public LinkedList<Location> blocks = new LinkedList<>();
	public final int id;
	
	private final RedSide red;
	private final BlueSide blue;
	public final Side[] sides;
	public BukkitTask task;
	
	public Sector(Person blue, Person red) throws PersonNotFoundException {
		this(blue, red, -1);
	}
	public Sector(Person blue, Person red, int sector) throws PersonNotFoundException {
		if (sector == -1) id = randomEmpty(); else id = sector;
		ingame[id] = this;
		this.red = new RedSide(this, red);
		this.blue = new BlueSide(this, blue);
		sides = new Side[] {this.red, this.blue};
		for (Side s : sides) {
			if (!s.getPlayer().exists()) {
				Side a = opposite(s);
				if (a.getPlayer().exists()) {
					a.getPlayer().sendMessage("§6Ваш соперник испугался и ливнул. Земля ему пухом.");
					a.getPlayer().getHandle().getInventory().setItem(0, MLGRush.queue);
				}
				byname.remove(a.getPlayer().getName());
				throw new PersonNotFoundException(s.getPlayer().getName());
			} else byname.put(s.getPlayer().getName(), this);
		}
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = sb.registerNewObjective("dummy", "dummy");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.getScore("§e" + blue.getName()).setScore(0);
		o.getScore("§e" + red.getName()).setScore(0);
		Score score = o.getScore("§bВремя до конца игры");
		o.setDisplayName("§a§lMLGRush");
		for (Side s : sides) {
			s.getPlayer().sendMessage("Вы играете в секторе §e" + id/SECTORS_IN_ROW + "-" + id%SECTORS_IN_ROW + "§f против " + opposite(s).getPlayer().getName());
			s.getPlayer().getHandle().setScoreboard(sb);
			Events.hits.put(s.getPlayer().getHandle(), 0);
		}
		start(score);
	}
	
	
	public static int randomEmpty() {
		List<Integer> integers = new LinkedList<>();
		Random r = new Random();
		for (int i = 0; i < ingame.length; i++) {
			if (ingame[i] == null) integers.add(i);
		}
		if (integers.isEmpty()) return -1;
		int se = integers.get(r.nextInt(integers.size()));
		try {if (ingame[se] != null) return randomEmpty();}
		catch (StackOverflowError e) {return -1;}
		return se;
	}
	
	public static int firstEmpty() {
		for (int i = 0; i < ingame.length; i++) if (ingame[i] == null) return i;
		throw new IllegalStateException("All sectors are ingame");
	}
	
	
	
	public void start(final Score o) {
		for (Side s : sides) {
			s.getPlayer().sendTitle("§aИгра началась!");
			s.respawn(false);
			s.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		o.setScore(300);
		task = new BukkitRunnable() {@Override public void run() {
			int score = o.getScore();
			if (score == 0) {
				Side s = null;
				int max = 0;
				int redpoints = red.getPoints();
				int bluepoints = blue.getPoints();
				if (redpoints > bluepoints) s = red;
				if (redpoints < bluepoints) s = blue;
				end(s);
				return;
			}
			o.setScore(score - 1);
		}}.runTaskTimer(MLGRush.plugin, 20L, 20L);
	}
	public Side getSide(Person p) {return getSide(p.getName());}
	public Side getSide(String name) {
		if (red.getPlayer().getName().equals(name)) return red;
		else return blue;
	}
	
	public void breakBed(String name, Side s) {
		clearArea();
		opposite(s).breakEnemy(sb);
		for (Side side : sides) {
			side.getPlayer().sendMessage("§e" + name + "§a сломал вражескую кровать! §6" + side.getPoints() + ":" + this.opposite(side).getPoints());
			if (side.beds > 4) {
				end(side);
				return;
			} else side.respawn(true);
		}
	}
	
	public void clearArea() {
		blocks.forEach((l) -> l.getBlock().setType(Material.AIR));
		blocks = new LinkedList<>();
	}
	
	public Side opposite(Side s) {
		if (s instanceof RedSide) return blue;
		else return red;
	}
	
	public void end(Side winner) {
		task.cancel();
		String score = red.getPoints() + ":" + blue.getPoints();
		clearArea();
		ingame[id] = null;
		for (Side s : sides) {
			if (Events.hits.get(s.getPlayer().getHandle()) < 10) {
				unfair(true);
				return;
			}
		}
		if (winner == null) {
			for (Side s : sides) {
				updateStats(s, false);
				Person p = s.getPlayer();
				if (p == null) continue;
				U.msg(p.getHandle(), "MLG §e> §f§lНичья.");
				p.sendTitle("Ничья!");
				p.getHandle().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
				byname.remove(p.getName());
				p.getHandle().getInventory().clear();
				new Cooldown("endgame", 5, Converter.asList(p), () -> {
					p.teleport(MLGRush.w.getSpawnLocation());
					MLGRush.equip(p.getHandle());
				});
				addDust(p.getName(), 30);
			}
			return;
		}
		addDust(winner.getPlayer().getName(), 50);
		winner.getPlayer().sendTitle("§aПобеда!");
		winner.getPlayer().sendSubtitle("§aВы победили со счётом §6" + score);
		for (Side s : sides) {
			updateStats(s, s.equals(winner));
			if (!s.equals(winner)) {
				s.getPlayer().sendTitle("§cПоражение!");
				addDust(s.getPlayer().getName(), 10);
			}
			Person p = s.getPlayer();
			if (p == null) continue;
			U.msg(p.getHandle(), "§8[§dMLG§8] §eИгрок §f", winner.getPlayer(), "§e победил со счётом §f" + score + "§e!");
			p.getHandle().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			byname.remove(p.getName());
			p.getHandle().getInventory().clear();
			new Cooldown("endgame", 5, Converter.asList(p), () -> {
				p.teleport(MLGRush.w.getSpawnLocation());
				MLGRush.equip(p.getHandle());
			});
		}
	}

	public static void updateStats(Side s, boolean winner) {
		Packet p = new PacketUpdateTop(s.getPlayer().getName(), winner, s.beds, s.deaths);
		Connect.send(p);
	}
	
	private int get(String[] array, int element) {
		try {return Integer.parseInt(array[element]);}
		catch (Exception e) {return 0;}
	}
	
	public void unfair() {unfair(false);}
	public void unfair(boolean b) {
		task.cancel();
		clearArea();
		ingame[id] = null;
		for (Side s : sides) {
			Person p = s.getPlayer();
			if (!p.getHandle().isOnline()) continue;
			p.teleport(MLGRush.w.getSpawnLocation());
			U.msg(p.getHandle(), b ? "§8[§dMLG§8] §eИгра признана нечестной и не засчитана." :
										 "§8[§dMLG§8] §eВаш соперник сбежал, а минуты времени от игры не прошло. Игра не засчитана.");
			p.getHandle().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
			byname.remove(p.getName());
			MLGRush.equip(p.getHandle());
		}
	}
	
	public static void addDust(String player, int dust) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mysterydust add " + player + " " + dust);
	}
}
