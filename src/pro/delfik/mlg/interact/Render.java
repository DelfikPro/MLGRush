package pro.delfik.mlg.interact;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pro.delfik.lmao.outward.Generate;
import pro.delfik.lmao.outward.inventory.SlotGUI;
import pro.delfik.mlg.MLGRush;
import pro.delfik.mlg.Sector;
import pro.delfik.mlg.side.RedSide;

public class Render {
	public static Inventory generate() {
		SlotGUI gui = new SlotGUI("Рендер секторов", Sector.MAPS, (p, slot, item) -> p.teleport(MLGRush.sectorize(RedSide.defaultLoc, slot)), true);
		for (int row = 0; row < Sector.MAPS; row++) {
			for (int col = 0; col < Sector.SECTORS_IN_ROW; col++) {
				Sector s = Sector.ingame[row * Sector.SECTORS_IN_ROW + col];
				ItemStack is;
				try {
					is = Generate.itemstack(
							s == null ? Material.COAL_BLOCK : Material.EMERALD_BLOCK, 1, 0, "§f§lСектор " + row + "-" + col,
							s == null ? "§eПустой сектор" : ("§aИдёт игра: " + s.sides[0].getPlayer().getName() + " и " + s
																																  .sides[1].getPlayer().getName())
					);
				} catch (NullPointerException e) {is = Generate.itemstack(
						s == null ? Material.COAL_BLOCK : Material.EMERALD_BLOCK, 1, 0, "§f§lСектор " + row + "-" + col,
						s == null ? "§eПустой сектор" : ("§aИдёт игра: §cNULLPOINTEREXCEPTION"));
				}
				gui.inv().setItem(row * Sector.SECTORS_IN_ROW + col, is);
			}
		}
		return gui.inv();
	}
}
