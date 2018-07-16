package pro.delfik.mlg.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pro.delfik.lmao.command.handle.CustomException;
import pro.delfik.lmao.command.handle.LmaoCommand;
import pro.delfik.lmao.core.Person;
import pro.delfik.mlg.Sector;
import pro.delfik.lmao.util.U;
import pro.delfik.util.Rank;

public class CommandCapitulate extends LmaoCommand {
	public CommandCapitulate(){
		super("capitulate", Rank.PLAYER, "Сдаться");
	}

	@Override
	protected void run(CommandSender sender, String command, String[] args) {
		Sector s = Sector.byname.get(sender.getName());
		if (s == null) throw new CustomException("§6Вы не можете сдаться, не находясь в игре.");
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
