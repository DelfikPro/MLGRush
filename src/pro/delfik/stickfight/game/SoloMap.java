package pro.delfik.lmao.stickfight.game;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Карта 1х2, в которой у каждого игрока одна точка спавна.
 */
public class SoloMap extends Map {
	
	// Соответствие: команда - точка спавна
	private final Teams<Location> spawns;
	
	/**
	 * Создатель инстанции карты, отличающийся от суперконструктора только наличием параметра spawns
	 * @param spawns Соответствие команда - точка спавна
	 */
	public SoloMap(String name, String schematic, Material material, Teams<Location> spawns) {
		super(name, schematic, material);
		this.spawns = spawns;
	}
	
	@Override
	public int getPlayersInTeam() {
		return 1;
	}
	
	@Override
	public Location getSpawnLocation(Color color) {
		return spawns.get(color);
	}
}
