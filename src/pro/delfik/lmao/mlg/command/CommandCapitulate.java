package pro.delfik.lmao.mlg.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pro.delfik.lmao.command.handle.Command;
import pro.delfik.lmao.command.handle.ImplarioCommand;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.mlg.game.Sector;
import pro.delfik.lmao.util.U;

public class CommandCapitulate extends ImplarioCommand {
	@Command(name = "capitulate", usage = "capitulate", description = "Сдаться")
	public void capitulate(CommandSender sender, String command, String[] args) {
		Sector s = Sector.byname.get(sender.getName());
		if (s == null) {
			sender.sendMessage("§6Вы не можете сдаться, не находясь в игре.");
			return;
		}
		if (args.length != 0 && (args[0].equals("confirm=") || args[0].equals("="))) {
			sender.sendMessage("§6Вы сдались.");
			s.end(s.opposite(s.getSide(Person.get(sender))));
			return;
		}
		sender.sendMessage("§6Вы ввели команду, которая позволяет мгновенно проиграть (сдаться)");
		((Player) sender).spigot().sendMessage(U.run("§6§nНажмите сюда, чтобы подтвердить ввод команды",
				"§eЛучше не тыкай, ты сможешь выиграть, я верю в тебя!", "/capitulate confirm="));
	}
}
