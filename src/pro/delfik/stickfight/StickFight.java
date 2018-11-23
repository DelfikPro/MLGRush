package pro.delfik.lmao.stickfight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import pro.delfik.lmao.core.Person;

/**
 * Класс для управления вещами, происходящими на сервере.
 * Главный класс нужен только для запуска плагина,
 * Так что следующие методы перенесены сюда.
 */
public class StickFight {
	
	private static World world;
	private static Location lobby;
	
	/**
	 * Получение игрового мира.
	 * @return Мир, в котором находится StickFight.
	 */
	public static World getWorld() {
		return world == null ? world = Bukkit.getWorlds().get(0) : world;
	}
	
	/**
	 * Получение точки спавна игроков.
	 * @return Местоположение лобби.
	 */
	private static Location getLobbyLocation() {
		return lobby == null ? lobby = world.getSpawnLocation().add(0.5, 0.5, 0.5) : lobby;
	}
	
	/**
	 * Телепортировать игрока в лобби и сбросить все свойства на стандартные.
	 * Сбрасываются: скорборд, инвентарь, жизненные показатели.
	 * @param person Игрок для телепортации.
	 */
	public static void toLobby(Person person) {
		person.teleport(getLobbyLocation());
		Inventory inv = person.getHandle().getInventory();
		inv.clear();
		inv.setItem(0, Items.JOIN_QUEUE);
		inv.setItem(8, Items.BACK_TO_HUB);
	}
	
	
	
}
