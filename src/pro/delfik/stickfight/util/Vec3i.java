package pro.delfik.lmao.stickfight.util;

import com.sk89q.worldedit.Vector;
import org.bukkit.Location;
import pro.delfik.lmao.stickfight.SFPlugin;

/**
 * Три числа int в одном объекте.
 * Используется вместо Location чтобы лишний раз не создавать мир.
 */
public class Vec3i {
	public final int x, y, z;
	
	public Vec3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * @return Объект вектора WorldEdit для управления схематиками
	 */
	public Vector toWEVector() {
		return new Vector(x, y, z);
	}
	/**
	 * @return Объект вектора Bukkit для указания направления
	 */
	public org.bukkit.util.Vector toBukkitVector() {
		return new org.bukkit.util.Vector(x, y, z);
	}
	
	/**
	 * @return Локация на углу блока
	 */
	public Location toBlock() {
		return new Location(SFPlugin.getWorld(), x, y, z);
	}
	
	/**
	 * @return Локация в центре блока
	 */
	public Location toLocation() {
		return new Location(SFPlugin.getWorld(), x + 0.5, y + 0.5, z + 0.5);
	}
}
