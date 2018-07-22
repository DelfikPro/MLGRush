package pro.delfik.mlg.interact;

import lib.Texteria;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pro.delfik.lmao.core.connection.PacketEvent;
import pro.delfik.net.Packet;
import pro.delfik.net.packet.PacketTop;

public class Top implements Listener{
	
	public static Texteria.Text numbers = null;
	public static Texteria.Text names = null;
	public static Texteria.Text wins = null;
	public static Texteria.Text games = null;

	@EventHandler
	public void event(PacketEvent event){
		Packet packet = event.getPacket();
		if (packet instanceof PacketTop){
			PacketTop.Top[] top = ((PacketTop) packet).getTop();
			update(top);
		}
	}
	
	public static void update(PacketTop.Top[] array) {
		World w = Bukkit.getWorlds().get(0);
		
		String[] names = new String[array.length + 1];
		String[] wins = new String[array.length + 1];
		String[] games = new String[array.length + 1];
		
		Location first = new Location(w, 11, 99, -2);
		
		if (numbers == null) numbers = Texteria.create(first.clone(),
				new String[] {"§d§l#", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
		names[0] = "§d§lИмя";
		wins[0] = "§d§lПобеды";
		games[0] = "§d§lИгры";
		for (int i = 1; i < array.length + 1; i++) {
			PacketTop.Top top = array[i - 1];
			boolean isNull = top == null;
			names[i] = isNull ? "§7§o- Пусто -" : top.getNick();
			wins[i] = isNull ? "§7-" : ("§a" + top.getWins());
			games[i] = isNull ? "§7-" : ("§a" + top.getGames());
		}
		if (Top.names == null) Top.names = Texteria.create(first.clone().add(0, 0, 1), names);
		else Top.names.setLines(names);
		if (Top.games == null) Top.games = Texteria.create(first.clone().add(0, 0, 3), games);
		else Top.games.setLines(games);
		if (Top.wins == null) Top.wins = Texteria.create(first.clone().add(0, 0, 2), wins);
		else Top.wins.setLines(wins);
	}
	
}
