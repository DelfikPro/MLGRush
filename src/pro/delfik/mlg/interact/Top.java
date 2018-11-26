package pro.delfik.mlg.interact;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import pro.delfik.lmao.outward.Texteria;

public class Top {
	
	public static Texteria.Text numbers = null;
	public static Texteria.Text names = null;
	public static Texteria.Text games = null;
	public static Texteria.Text wins = null;
	
	public static void update(String[] array) {
		World w = Bukkit.getWorlds().get(0);
		
		String[] names = new String[array.length + 2];
		String[] wins = new String[array.length + 2];
		String[] games = new String[array.length + 2];
		
		Location first = new Location(w, 11, 99, -2);
		
		if (numbers == null) numbers = Texteria.create(first.clone(),
				new String[] {"§d§l#", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
		names[0] = "§d§lИмя";
		names[1] = "§36oogle";
		games[0] = "§d§lИгры";
		wins[1] = "§a\u221e";
		wins[0] = "§d§lПобеды";
		games[1] = "§a0";
		for (int i = 2; i < array.length + 2; i++) {
			String split[] = array[i - 2].split(" ");
			boolean isNull = split[0].equals("null");
			names[i] = isNull ? "§7§o- Пусто -" : split[0];
			wins[i] = isNull ? "§7-" : "§a" + split[1];
			games[i] = isNull ? "§7-" : "§a" + split[2];
		}
		if (Top.names == null) Top.names = Texteria.create(first.clone().add(0, 0, 1), names);
		else Top.names.setLines(names);
		if (Top.wins == null) Top.wins = Texteria.create(first.clone().add(0, 0, 3), games);
		else Top.wins.setLines(games);
		if (Top.games == null) Top.games = Texteria.create(first.clone().add(0, 0, 2), wins);
		else Top.games.setLines(wins);
	}
	
}
