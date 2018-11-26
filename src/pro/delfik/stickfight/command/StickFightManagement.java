package pro.delfik.lmao.stickfight.command;

import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.LmaoCommand;
import pro.delfik.util.Rank;

public class StickFightManagement extends LmaoCommand {
	
	
	public StickFightManagement() {
		super("sfm", Rank.KURATOR, "Управление StickFight");
	}
	
	@Override
	protected void run(CommandSender commandSender, String s, String[] strings) {
	
	}
}
