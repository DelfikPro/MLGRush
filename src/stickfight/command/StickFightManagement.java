package pro.delfik.lmao.stickfight.command;

import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.Command;
import pro.delfik.lmao.command.handle.ImplarioCommand;
import pro.delfik.lmao.permissions.Rank;

public class StickFightManagement extends ImplarioCommand {
	
	@Command(name = "sfm", rankRequired = Rank.KURATOR, description = "Управление StickFight")
	public void execute(CommandSender sender, String cmd, String[] args) {
	
	
	
	}
	
}
