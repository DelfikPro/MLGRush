package pro.delfik.stickfight;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pro.delfik.lmao.core.Registrar;
import pro.delfik.stickfight.command.StickFightManagement;
import pro.delfik.stickfight.game.GameListeners;

public class SFPlugin extends JavaPlugin {
	
	public static SFPlugin instance;
	public static Schematics schematics;
	
	@Override
	public void onEnable() {
		instance = this;
		schematics = new Schematics(((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit")), Bukkit.getWorlds().get(0));
		Registrar r = new Registrar(instance);
		r.regCommand(new StickFightManagement(null, null, null));
		r.regEvent(new GameListeners());
	}
}
