package pro.delfik.mlg.interact;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lmao.command.handle.PersonNotFoundException;
import pro.delfik.lmao.user.Person;
import pro.delfik.mlg.MLGRush;
import pro.delfik.mlg.Sector;
import pro.delfik.lmao.util.Cooldown;

import java.util.Arrays;

public class Queue {
	public static volatile Person waiting = null;
	public static boolean join(Person p) {
		if (waiting == null) {
			waiting = p;
			p.getHandle().getInventory().setItem(0, MLGRush.leavequeue);
			return true;
		}
		Person[] players = new Person[] {p, waiting};
		for (Person pp : players) {
			pp.sendMessage("§aСоперник найден, игра начинается!");
			pp.getHandle().getInventory().setItem(0, new ItemStack(Material.AIR));
			pp.getHandle().updateInventory();
		}
		Person p2 = waiting;
		new Cooldown("queue", 4, Arrays.asList(players), () -> {try {new Sector(p, p2);} catch (PersonNotFoundException ignored) {}});
		waiting = null;
		return true;
	}
	
}
