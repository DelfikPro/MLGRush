package pro.delfik.mlg.command;

import implario.util.Rank;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.CustomException;
import pro.delfik.lmao.command.handle.LmaoCommand;
import pro.delfik.lmao.user.Person;
import pro.delfik.lmao.util.U;
import pro.delfik.mlg.MLGRush;
import pro.delfik.mlg.Sector;
import pro.delfik.mlg.interact.Render;
import pro.delfik.mlg.side.BlueSide;
import pro.delfik.mlg.side.RedSide;

public class CommandMLG extends LmaoCommand {
	public CommandMLG(){
		super("mlgrush", Rank.LMAO, "Управление MLGRush");
	}

	@Override
	protected void run(CommandSender sender, String command, String[] args) {
		requireArgs(args, 1, "[Подкоманда]");
		String prefix = "§8[§dMLG§8] §a";
		switch (args[0].toLowerCase()) {
			case "top2":
				Sector.updateStats(null, true, sender.getName());
				sender.sendMessage("§aOK");
				return;
			case "inv":
				Person.get(sender).getHandle().openInventory(Render.generate());
				return;
			case "sector": {
				try {
					int sector = Integer.parseInt(args[1]);
					Person p = U.unary(sender, args[2]);
					if (p == null) sender.sendMessage(prefix + "§cИгрок не найден.");
					Person self = Person.get(sender);
					new Sector(self, p, sector);
					sender.sendMessage(prefix + "Игра начата.");
					return;
				} catch (NumberFormatException e) {
					sender.sendMessage(prefix + "§e" + args[1] + "§c - не число.");
					return;
				} catch (ArrayIndexOutOfBoundsException e) {
					sender.sendMessage(prefix + "§cИспользование: §e/mlg sector [Номер сектора] [Игрок]");
					return;
				}
			}
			case "setup": {
				requireArgs(args, 2, "setup [Номер сектора]");
				int id = requireInt(args[1]);
				MLGRush.sectorize(RedSide.defaultLoc, id).getBlock().setType(Material.REDSTONE_BLOCK);
				MLGRush.sectorize(BlueSide.defaultLoc, id).getBlock().setType(Material.LAPIS_BLOCK);
				for (Location loc : MLGRush.sectorize(RedSide.defaultBed, id)) loc.getBlock().setType(Material.GOLD_BLOCK);
				for (Location loc : MLGRush.sectorize(BlueSide.defaultBed, id)) loc.getBlock().setType(Material.GOLD_BLOCK);
				sender.sendMessage("§aОтметки для сектора §e" + id + " §aустановлены.");
				return;
			}
			case "win": {
				Sector sec = Sector.byname.get(sender.getName());
				if (sec == null) throw new CustomException(prefix + "§cВы не в игре.");
				sender.sendMessage(prefix + "Принудительная победа...");
				sec.end(sec.getSide(sender.getName()));
				return;
			}
			case "lose": {
				Sector sec = Sector.byname.get(sender.getName());
				if (sec == null) {
					sender.sendMessage(prefix + "§cВы не в игре.");
					return;
				}
				sender.sendMessage(prefix + "Принудительное поражение...");
				sec.end(sec.opposite(sec.getSide(sender.getName())));
				return;
			}
			case "break": {
				Sector sec = Sector.byname.get(sender.getName());
				if (sec == null) {
					sender.sendMessage(prefix + "§cВы не в игре.");
					return;
				}
				sender.sendMessage(prefix + "Ломаем кровать...");
				sec.breakBed(sender.getName(), sec.opposite(sec.getSide(sender.getName())));
				return;
			}
			case "clear": {
				Sector sec = Sector.byname.get(sender.getName());
				if (sec == null) throw new CustomException(prefix + "§cВы не в игре");
				sender.sendMessage(prefix + "Очистка территории...");
				sec.clearArea();
				return;
			}
			case "disallow": throw new CustomException(prefix + "Входы запрещены.");
			default: throw new CustomException(prefix + "§6Подкоманда не найдена.");
			}
	}
}
