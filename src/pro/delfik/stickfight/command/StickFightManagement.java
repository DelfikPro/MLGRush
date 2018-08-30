package pro.delfik.stickfight.command;

import implario.util.Rank;
import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.LmaoCommand;

public class StickFightManagement extends LmaoCommand {

	public StickFightManagement(String name, Rank required, String description) {
		super(name, required, description);
	}

	@Override
	protected void run(CommandSender sender, String command, String[] args) {

	}
}
