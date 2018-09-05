package pro.delfik.lmao.stickfight.game;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GameListeners implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
			ItemStack item = e.getPlayer().getItemInHand();
			if (item == null) return;
			switch (item.getType()) {
				case FIREWORK_CHARGE: {
					
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}
