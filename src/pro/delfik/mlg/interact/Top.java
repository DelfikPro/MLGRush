package pro.delfik.mlg.interact;

import lib.Texteria;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import pro.delfik.lmao.core.connection.database.ServerIO;

public class Top {
	
	public static Texteria.Text numbers = null;
	public static Texteria.Text names = null;
	public static Texteria.Text wins = null;
	public static Texteria.Text games = null;
	
	public static void update(boolean b) {
		World w = Bukkit.getWorlds().get(0);
		String s = ServerIO.connect("gettop sf");
		
		if (b) Bukkit.broadcastMessage("§aТоп: §e" + s);
		
		String[] lines = s.split("\n");
		//String[] top = new String[lines.length];
		String[] names = new String[lines.length + 1];
		String[] wins = new String[lines.length + 1];
		String[] games = new String[lines.length + 1];
		
		Location first = new Location(w, 11, 99, -2);
		
		if (numbers == null) numbers = Texteria.create(first.clone(),
				new String[] {"§d§l#", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
		names[0] = "§d§lИмя";
		wins[0] = "§d§lПобеды";
		games[0] = "§d§lИгры";
		for (int i = 1; i < lines.length + 1; i++) {
			if (lines[i - 1].equals("null")) {
				//top[i] = "§e" + (i + 1) + ". §f- EMPTY -";
				names[i] = "- EMPTY -";
				wins[i] = "-";
				games[i] = "-";
			} else {
				String[] args = lines[i - 1].split("}");
				names[i] = args[0];
				wins[i] = "§a" + args[2];
				games[i] = "§a" + args[1];
				//top[i] = "§e" + (i + 1) + ". §f" + args[0] + "§b - " + args[2] + " побед §f| §b" + args[1] + " игр §f| §b" +
				//		args[3] + " кроватей §f| §b" + args[4] + " смертей §f|";
			}
		}
		if (Top.names == null) Top.names = Texteria.create(first.clone().add(0, 0, 1), names);
		else Top.names.setLines(names);
		if (Top.games == null) Top.games = Texteria.create(first.clone().add(0, 0, 3), games);
		else Top.games.setLines(games);
		if (Top.wins == null) Top.wins = Texteria.create(first.clone().add(0, 0, 2), wins);
		else Top.wins.setLines(wins);
	}
	
	
	public static void update() {update(false);}
}
