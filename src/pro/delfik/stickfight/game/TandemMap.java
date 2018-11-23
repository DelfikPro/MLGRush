package pro.delfik.lmao.stickfight.game;

import org.bukkit.Location;
import org.bukkit.Material;
import pro.delfik.lmao.stickfight.util.CyclicIterator;

public class TandemMap extends Map {
	private final Teams<CyclicIterator<Location>> locations;
	
	public TandemMap(String name, String schematic, Teams<Location[]> locations, Material material) {
		super(name, schematic, material);
		
		// Конвертируем массивы с точками спавна в бесконечные итераторы
		this.locations = locations.convert(CyclicIterator::new);
	}
	
	@Override
	public Location getSpawnLocation(Color color) {
		return locations.get(color).next();
	}
	
	@Override
	public int getPlayersInTeam() {
		return 2;
	}
}
