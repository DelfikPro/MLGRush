package pro.delfik.lmao.stickfight.game;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;

/**
 * Карта для игры.
 * С помощью этого класса определяются все местоположения (Кроватей, Спавна игроков, Центра карты),
 * А также осуществляется построение и уничтожение карты через схематику.
 *
 * Наследуя этот класс можно делать разные форматы игры.
 * @see SoloMap
 * @see TandemMap
 */
public abstract class Map {
	
	// Соответствие Материал - Карта для выбора карты через инвентарь.
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private static final HashMap<Material, Map> set = new HashMap<>();
	
	private final String name;
	private final String schematic;
	
	/**
	 * Создаёт новую инстанцию карты.
	 * Обычно используется при инициализации плагина.
	 *
	 * @param name Название карты
	 * @param schematic Название файла со схематикой карты
	 */
	public Map(String name, String schematic, Material material) {
		this.name = name;
		this.schematic = schematic;
		set.put(material, this);
	}
	
	/**
	 * Получение места, в которое нужно телепортировать игрока после падения или разрушения кровати.
	 *
	 * @param color Цвет команды
	 * @return Местоположение спауна
	 */
	public abstract Location getSpawnLocation(Color color);
	
	/**
	 * @return Количество игроков в одной команде
	 */
	public abstract int getPlayersInTeam();
	
	public String getName() {
		return name;
	}
	public String getSchematic() {
		return schematic;
	}
	
	
	/**
	 * Цвет команды, по которому одну команду можно отличить от другой.
	 * Не исключено, что будут карты не только на две команды.
	 */
	public enum Color {
		RED("Красные", "§c"),
		BLUE("Синие", "§9");
		
		private final String string, color;
		Color(String string, String color) {
			this.string = string;
			this.color = color;
		}
		@Override
		public String toString() {
			return string;
		}
		
		public String getColor() {
			return color;
		}
	}
}
