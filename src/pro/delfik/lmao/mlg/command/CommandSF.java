package pro.delfik.lmao.mlg.command;

import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.Command;
import pro.delfik.lmao.command.handle.ImplarioCommand;
import pro.delfik.lmao.core.Person;
import pro.delfik.lmao.mlg.game.Sector;
import pro.delfik.lmao.mlg.game.interact.Call;
import pro.delfik.lmao.util.U;

public class CommandSF extends ImplarioCommand {
	@Command(name = "stickfight", description = "Вызов игрока на дуэль", usage = "sf [Игрок]", argsRequired = 1)
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Person invoker = Person.get(sender);
		Person receiver = U.unary(sender, args[0]);
		
		// Проверка на игрока
		if (invoker == null) {sender.sendMessage("§cЛибо вы не прогрузились как игрок, либо вы не игрок.");
			return false;}
		
		// Проверка на получателя
		if (receiver == null) {sender.sendMessage("§8[§dMLG§8] §6Игрок §e" + receiver + "§6 не найден.");
			return false;}
		
		// Проверка на то, играет ли игрок
		if (Sector.byname.get(receiver.getName()) != null) {
			sender.sendMessage("§8[§dMLG§8] §6Игрок §e" + receiver + "§6 уже играет.");
			return false;
		}
		
		//
		if (Sector.byname.get(invoker.getName()) != null) {
			sender.sendMessage("§8[§dMLG§8] §6Вы не можете кидать вызовы во время игры.");
			return false;
		}
		if (receiver.getName().equals(invoker.getName())) {
			sender.sendMessage("§8[§dMLG§8] §6И как ты себе это представляешь?");
			return false;
		}
		Call call = Call.list.get(args[0].toLowerCase());
		if (call != null) {
			sender.sendMessage("§8[§dMLG§8] §aПринятие вызова...");
			call.accept();
			return true;
		}
		new Call(invoker.getName(), receiver.getName()).call();
		sender.sendMessage("§8[§dMLG§8] §aВызов брошен игроку §e" + receiver.getDisplayName());
		return true;
	}
}
