package pro.delfik.lmao.mlg.command;

import pro.delfik.lmao.permissions.Rank;
import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.Command;
import pro.delfik.lmao.command.handle.ImplarioCommand;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.mlg.game.Sector;
import pro.delfik.lmao.mlg.game.interact.Render;
import pro.delfik.lmao.mlg.game.interact.Top;
import pro.delfik.lmao.util.U;

public class CommandMLG extends ImplarioCommand {
	@Command(name = "mlgrush", description = "Управление MLGRush", usage = "mlgrush [...]", rankRequired = Rank.DEV, argsRequired = 1)
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		String prefix = "§8[§dMLG§8] §a";
		try {
			switch (args[0].toLowerCase()) {
				case "top": {
					Top.update(true);
					sender.sendMessage(prefix + "Топ обновлён.");
					return true;
				}
				case "inv": {
					Person.get(sender).getHandle().openInventory(Render.generate());
					return true;
				}
				case "sector": {
					try {
						int sector = Integer.parseInt(args[1]);
						Person p = U.unary(sender, args[2]);
						if (p == null) sender.sendMessage(prefix + "§cИгрок не найден.");
						Person self = Person.get(sender);
						new Sector(self, p, sector);
						sender.sendMessage(prefix + "Игра начата.");
						return true;
					} catch (NumberFormatException e) {
						sender.sendMessage(prefix + "§e" + args[1] + "§c - не число.");
						return false;
					} catch (ArrayIndexOutOfBoundsException e) {
						sender.sendMessage(prefix + "§cИспользование: §e/mlg sector [Номер сектора] [Игрок]");
						return false;
					}
				}
				case "win": {
					Sector sec = Sector.byname.get(sender.getName());
					if (sec == null) {
						sender.sendMessage(prefix + "§cВы не в игре.");
						return false;
					}
					sender.sendMessage(prefix + "Принудительная победа...");
					sec.end(sec.getSide(sender.getName()));
					return true;
				}
				case "lose": {
					Sector sec = Sector.byname.get(sender.getName());
					if (sec == null) {
						sender.sendMessage(prefix + "§cВы не в игре.");
						return false;
					}
					sender.sendMessage(prefix + "Принудительное поражение...");
					sec.end(sec.opposite(sec.getSide(sender.getName())));
					return true;
				}
				case "break": {
					Sector sec = Sector.byname.get(sender.getName());
					if (sec == null) {
						sender.sendMessage(prefix + "§cВы не в игре.");
						return false;
					}
					sender.sendMessage(prefix + "Ломаем кровать...");
					sec.breakBed(sender.getName(), sec.opposite(sec.getSide(sender.getName())));
					return true;
				}
				case "clear": {
					Sector sec = Sector.byname.get(sender.getName());
					if (sec == null) {
						sender.sendMessage(prefix + "§cВы не в игре.");
						return false;
					}
					sender.sendMessage(prefix + "Очистка территории...");
					sec.clearArea();
					return true;
				}
				case "disallow": {
					sender.sendMessage(prefix + "Входы запрещены.");
					return true;
				}
				default: {
					sender.sendMessage(prefix + "§6Подкоманда не найдена.");
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(prefix + "§cПроизошла ошибка. Чекай консоль. §e" + e.getClass().getSimpleName());
			return false;
		}



//		Person initiator = Person.get(sender);
//		if (args.length == 0) {
//			initiator.getHandle().openInventory(Render.generate());
//			return true;
//		}
//		Person victim = Person.get(args[0]);
//		if (victim == null) {
//			sender.sendMessage("§8[§dMLG§8] §cИгрок §e" + args[0] + "§c не найден.");
//			return false;
//		}
//		sender.sendMessage("§8[§dMLG§8] §aЗапрос на дуэль отправлен.");
//		victim.sendTitle("§f");
//		victim.sendSubtitle("§aИгрок §e" + initiator.getDisplayName() + "§a вызывает вас на дуэль.");
//		return true;
	}
}
