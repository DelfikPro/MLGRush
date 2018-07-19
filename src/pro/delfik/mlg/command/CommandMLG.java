package pro.delfik.mlg.command;

import pro.delfik.lmao.command.handle.CustomException;
import pro.delfik.lmao.command.handle.LmaoCommand;
import pro.delfik.lmao.command.handle.NotEnoughArgumentsException;
import org.bukkit.command.CommandSender;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.core.connection.Connect;
import pro.delfik.mlg.Sector;
import pro.delfik.mlg.interact.Render;
import pro.delfik.mlg.interact.Top;
import pro.delfik.lmao.util.U;
import pro.delfik.net.packet.PacketUpdateTop;
import pro.delfik.util.Rank;

public class CommandMLG extends LmaoCommand {
	public CommandMLG(){
		super("mlgrush", Rank.DEV, "Управление MLGRush");
	}

	@Override
	protected void run(CommandSender sender, String command, String[] args) {
		requireArgs(args, 1, "[Подкоманда]");
		String prefix = "§8[§dMLG§8] §a";
		try {
			switch (args[0].toLowerCase()) {
				case "top":
					Connect.send(new PacketUpdateTop("Nemo", false, 0, 100));
					sender.sendMessage(prefix + "Топ обновлён.");
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
					if (sec == null) {
						sender.sendMessage(prefix + "§cВы не в игре.");
						return;
					}
					sender.sendMessage(prefix + "Очистка территории...");
					sec.clearArea();
					return;
				}
				case "disallow": throw new CustomException(prefix + "Входы запрещены.");
				default: throw new CustomException(prefix + "§6Подкоманда не найдена.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(prefix + "§cПроизошла ошибка. Чекай консоль. §e" + e.getClass().getSimpleName());
		}
	}
}
