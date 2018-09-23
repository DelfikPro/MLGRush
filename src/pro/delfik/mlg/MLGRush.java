package pro.delfik.mlg;

import implario.net.packet.PacketUpdateTop;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import pro.delfik.lmao.util.Registrar;
import pro.delfik.lmao.Connect;
import pro.delfik.lmao.outward.Generate;
import pro.delfik.mlg.command.CommandCapitulate;
import pro.delfik.mlg.command.CommandMLG;
import pro.delfik.mlg.command.CommandSF;
import pro.delfik.mlg.interact.Call;
import pro.delfik.mlg.interact.Queue;
import pro.delfik.mlg.interact.Render;
import pro.delfik.mlg.interact.Top;
import pro.delfik.mlg.side.BlueSide;
import pro.delfik.mlg.side.RedSide;

import java.io.File;
import java.util.List;

public class MLGRush extends JavaPlugin {
	public static World w = null;
	public static double fallY;
	public static Location spawn;
	public final static ItemStack stick = Generate.enchant(Generate.itemstack(Material.STICK, 1, 0, "§diStick",
			"§b- Лицензированная палка от Apple", "§b §b всего за 29 баксов!"), Enchantment.KNOCKBACK, 1);
	public final static ItemStack HUB = Generate.itemstack(Material.COMPASS, 1, 0, "§f>> §cВернуться в лобби §f<<");
	public static final ItemStack GARPOON = Generate.unbreak(Generate.itemstack(
			Material.FISHING_ROD, 1, 0, "§aАбордажный крюк!", "§7§o - \"Лучше, чем кукла\""));
	
	public static MLGRush plugin;
	public static final ItemStack queue = Generate.enchant(Generate.charge(Color.LIME, "§6>> §aВойти в очередь §6<<"), Enchantment.LUCK, 1);
	public static final ItemStack leavequeue = Generate.enchant(Generate.charge(Color.RED, "§6>> §cВыйти из очереди §6<<"), Enchantment.LUCK, 1);
	
	public static void equip(Player p) {
		Inventory i = p.getInventory();
		p.getInventory().clear();
		p.getInventory().setItem(0, queue);
		p.getInventory().setItem(1, GARPOON);
		p.getInventory().setItem(8, HUB);
		try {
			ItemStack item = com.yapzhenyie.GadgetsMenu.GadgetsMenu.getGadgetsMenuData().getMenuSelector();
			p.getInventory().setItem(7, item);
		} catch (NoClassDefFoundError ignored) {}
		p.updateInventory();
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		Registrar r = new Registrar(this);
		r.regCommand(new CommandMLG());
		r.regCommand(new CommandSF());
		r.regCommand(new CommandCapitulate());
		r.regEvent(new Events());
		r.regEvent(new Top());
		Render.class.getCanonicalName();
		Call.class.getCanonicalName();
		Top.class.getCanonicalName();
		Queue.class.getCanonicalName();
		
		try {
			w = Bukkit.getWorlds().get(0);
			spawn = w.getSpawnLocation().add(0.5, 0.5, 0.5);
			spawn.setYaw(-90);
		} catch (IndexOutOfBoundsException ignored) {}
		if (w != null) {
			initDefaults();
		}
		Connect.send(new PacketUpdateTop("Nemo", false, 0, 100));
		fallY = 29;
	}
	
	@EventHandler
	public void onWorld(WorldInitEvent e) {
		if (e.getWorld().getName().startsWith("SF")) {
			w = e.getWorld();
			spawn = w.getSpawnLocation().add(0.5, 0.5, 0.5);
			spawn.setYaw(-90);
			initDefaults();
		}
	}
	
	@Override
	public void onDisable() {
		for (int i = 0; i < Sector.ingame.length; i++) if (Sector.ingame[i] != null) Sector.ingame[i].clearArea();
		for (Entity e : w.getEntities()) if (e.getType() == EntityType.ARMOR_STAND) e.remove();
	}
	
	public static void initDefaults() {
		MLGRush.w = Bukkit.getWorlds().get(0);
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File("settings.yml"));
		try {
			BlueSide.defaultLoc = parse(yml.getString("map.blue-spawn"), w);
			RedSide.defaultLoc = parse(yml.getString("map.red-spawn"), w);
			BlueSide.defaultBed = parse(yml.getStringList("map.blue-bed"), w);
			RedSide.defaultBed = parse(yml.getStringList("map.red-bed"), w);
		} catch (Exception e) {
			Bukkit.broadcastMessage("§cУ тебя кривые конфиги. Исправляй.");
			Bukkit.getPluginManager().disablePlugin(MLGRush.plugin);
		}
	}
	
	public static Location[] sectorize(Location[] loc, int id) {
		return new Location[] {sectorize(loc[0], id), sectorize(loc[1], id)};
	}
	
	public static Location sectorize(Location loc, int id) {
		int betweenSameMaps = 46;
		int betweenVariousMaps = 48;
		Location l = loc.clone();
		l.setZ(l.getZ() + (betweenSameMaps * (id % Sector.SECTORS_IN_ROW)));
		l.setX(l.getX() + (betweenVariousMaps * (id / Sector.SECTORS_IN_ROW)));
		return l;
	}
	
	public static Location[] parse(List<String> s, World w) {
		Location[] locs = new Location[s.size()];
		for (int i = 0; i < locs.length; i++) {
			locs[i] = parseBlock(s.get(i), w);
		}
		return locs;
	}
	public static Location parseBlock(String s, World w) {
		s = s.replaceAll(" ", "");
		String s1[] = s.split(",");
		if (s1.length == 3) {
			return new Location(w, Integer.decode(s1[0]), Integer.decode(s1[1]), Integer.decode(s1[2]));
		} else return null;
	}
	public static Location parse(String s, World w){
		s = s.replaceAll(" ", "");
		String s1[] = s.split(",");
		if(s1.length == 3) {
			return new Location(w, Integer.decode(s1[0]) + 0.5, Integer.decode(s1[1]) + 0.5, Integer.decode(s1[2])
																									 + 0.5);
		} else if (s1.length == 5) {
			return new Location(w, Integer.decode(s1[0]) + 0.5, Integer.decode(s1[1]) + 0.5, Integer.decode(s1[2]) + 0.5,
									   (float) (Integer.decode(s1[3]) + 0.5), (float) (Integer.decode(s1[4]) + 0.5));
		} else return null;
	}
}
