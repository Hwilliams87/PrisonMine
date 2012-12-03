package com.wolvencraft.prison.mines.cmd;

import org.bukkit.entity.Player;

import com.wolvencraft.prison.PrisonSuite;
import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.region.PrisonSelection;

public class DebugCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		if(args.length == 1) {
			Message.sendError(PrisonMine.getLanguage().ERROR_COMMAND);
			return false;
		}

		Player player = (Player) CommandManager.getSender();
		
		if(!player.isOp()) {
			Message.sendError(PrisonMine.getLanguage().ERROR_COMMAND);
			return false;
		}
		
		Mine curMine = Mine.get(args[1]);
		
		if(args[0].equalsIgnoreCase("debug")) {
			getHelp();
			return true;
		}
		if(args[0].equalsIgnoreCase("setregion")) {
			PrisonSelection sel = PrisonSuite.getSelection(player);
			curMine.setRegion(sel);
			Message.sendCustom("DEBUG", "Region set");
			return curMine.save();
		} else if(args[0].equalsIgnoreCase("tp")) {
			player.teleport(curMine.getRegion().getMaximum());
			Message.sendCustom("DEBUG", "Teleported to: " + curMine.getId());
			return true;
		} else {
			Message.sendError(PrisonMine.getLanguage().ERROR_COMMAND);
			return false;
		}
	}

	@Override
	public void getHelp() { 
		Message.formatHeader(20, "Debug");
		Message.formatHelp("setregion", "<id>", "Sets the reset region of a mine to the one specified");
		Message.formatHelp("tp", "<id>", "Teleports the sender to the specified mine");
	}

	@Override
	public void getHelpLine() { }

}
