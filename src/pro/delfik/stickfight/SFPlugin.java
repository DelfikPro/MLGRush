package pro.delfik.lmao.stickfight;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import pro.delfik.lmao.core.Registrar;
import pro.delfik.lmao.stickfight.command.StickFightManagement;
import pro.delfik.lmao.stickfight.game.GameListeners;

public class SFPlugin extends JavaPlugin {
	
	public static SFPlugin instance;
	public static Schematics schematics;
	
	@Override
	public void onEnable() {
		instance = this;
		schematics = new Schematics(((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit")), Bukkit.getWorlds().get(0));
		Registrar r = new Registrar(instance);
		r.regCommand(new StickFightManagement());
		r.regEvent(new GameListeners());
		
		// Сделать ничего. Я не знаю, зачем этот код, но без него ничего не работает. Серьёзно.
		doNothing();
		doNothing();
	}
	
	/**
	 * Метод, который ничего не делает
	 * @since ничего
	 */
	public static void doNothing() {
		// Ничего не делаем
	}
}
