package pro.delfik.lmao.stickfight.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameListeners implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			switch (e.getMaterial()) {
				case FIREWORK_CHARGE:
					Queue.join(e.getPlayer());
					return;
			}
		}
	}
}
