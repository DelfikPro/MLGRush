package pro.delfik.mlg.command;

import implario.util.Rank;
import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.CustomException;
import pro.delfik.lmao.command.handle.LmaoCommand;
import pro.delfik.lmao.core.Person;
import pro.delfik.mlg.Sector;
import pro.delfik.mlg.interact.Call;
import pro.delfik.lmao.util.U;

public class CommandSF extends LmaoCommand {
	public CommandSF(){
		super("stickfight", Rank.PLAYER, "Вызов игрока на дуэль.");
	}

	@Override
	protected void run(CommandSender sender, String command, String[] args) {
		requireArgs(args, 1, "[Игрок]");
		Person invoker = Person.get(sender);
		Person receiver = U.unary(sender, args[0]);

		// Проверка на игрока
		if (invoker == null) throw new CustomException("§cЛибо вы не прогрузились как игрок, либо вы не игрок.");

		// Проверка на получателя
		if (receiver == null) throw new CustomException("§8[§dMLG§8] §6Игрок §e" + args[0] + "§6 не найден.");

		// Проверка на то, играет ли игрок
		if (Sector.byname.get(receiver.getName()) != null)
			throw new CustomException("§8[§dMLG§8] §6Игрок §e" + receiver.getName() + "§6 уже играет.");

		//
		if (Sector.byname.get(invoker.getName()) != null)
			throw new CustomException("§8[§dMLG§8] §6Вы не можете кидать вызовы во время игры.");
		if (receiver.getName().equals(invoker.getName()))
			throw new CustomException("§8[§dMLG§8] §6И как ты себе это представляешь?");
		Call call = Call.list.get(args[0].toLowerCase());
		if (call != null) {
			sender.sendMessage("§8[§dMLG§8] §aПринятие вызова...");
			call.accept();
			return;
		}
		new Call(invoker.getName(), receiver.getName()).call();
		sender.sendMessage("§8[§dMLG§8] §aВызов брошен игроку §e" + receiver.getDisplayName());
	}
}
