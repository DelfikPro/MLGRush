package pro.delfik.lmao.stickfight;

import lib.Ench;
import lib.Generate;
import lib.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Предметы, которые лучше не создавать много раз, а использовать одни и те же.
 */
public final class Items {
	private Items() {throw new AssertionError("Нидам тебе инстанцию итемса!");}
	
	public static final ItemStack STICK_DEFAULT, STICK_ADVANCED, STICK_SUPER, STICK_ULTIMATE;
	public static final ItemStack BLOCKS_DEFAULT, BLOCKS_ADVANCED;
	public static final ItemStack BACK_TO_HUB;
	public static final ItemStack JOIN_QUEUE, LEAVE_QUEUE;
	
	
	static {
		STICK_DEFAULT = new ItemBuilder(Material.STICK)
						.enchant(new Ench(Enchantment.KNOCKBACK, 1))
						.withDisplayName("§eЗубочистка")
						.build();
		STICK_ADVANCED = new ItemBuilder(Material.BONE)
				.enchant(new Ench(Enchantment.KNOCKBACK, 1))
				.withDisplayName("§bСлоновья кость")
				.build();
		STICK_SUPER = new ItemBuilder(Material.BLAZE_ROD)
				.enchant(new Ench(Enchantment.KNOCKBACK, 1))
				.withDisplayName("§6§lЗолотая реликвия")
				.build();
		STICK_ULTIMATE = new ItemBuilder(Material.PRISMARINE_SHARD)
				.enchant(new Ench(Enchantment.KNOCKBACK, 2))
				.withDisplayName("§d§lАртефакт #1")
				.withLore("§e§oОдин из восьми мощнейших", "§e§oПредметов во вселенной.", "§e§oЛегенды гласят, что если",
						"§e§oВсе 8 воедино, то хранитель", "§e§oОбретёт немыслимую силу.")
				.build();
		BACK_TO_HUB = ItemBuilder.create(Material.COMPASS, "§f>> §cВернуться в лобби §f<<");
		JOIN_QUEUE = Generate.charge(Color.LIME, "§f>> §aНайти игру §f<<");
		LEAVE_QUEUE = Generate.enchant(Generate.charge(Color.RED, "§f>> §cПрекратить поиск §f<<"), Enchantment.LUCK, 10);
		BLOCKS_DEFAULT = new ItemBuilder(Material.SANDSTONE)
						.withData(2)
						.withAmount(64)
						.withDisplayName("§eКуски асфальта")
						.build();
		BLOCKS_ADVANCED = new ItemBuilder(Material.STAINED_GLASS)
						.withData(3)
						.withAmount(64)
						.withDisplayName("§b§lТессеракт")
						.enchant(new Ench(Enchantment.ARROW_INFINITE, 1))
						.build();
	}
}
