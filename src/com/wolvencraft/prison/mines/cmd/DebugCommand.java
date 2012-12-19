package com.wolvencraft.prison.mines.cmd;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
		if(args.length == 1 && !args[0].equalsIgnoreCase("debug")) {
			Message.sendError(PrisonMine.getLanguage().ERROR_COMMAND);
			return false;
		}

		CommandSender player = CommandManager.getSender();
		
		if(!(player instanceof ConsoleCommandSender) && !player.isOp()) {
			Message.sendError(PrisonMine.getLanguage().ERROR_COMMAND);
			return false;
		}
		
		Message.debug("Checks passed, parsing the command");
		
		if(args[0].equalsIgnoreCase("debug")) {
			getHelp();
			return true;
		} else if(args[0].equalsIgnoreCase("setregion")) {
			Mine curMine = Mine.get(args[1]);
			PrisonSelection sel = PrisonSuite.getSelection((Player) player);
			curMine.setRegion(sel);
			Message.sendCustom("DEBUG", "Region set");
			return curMine.save();
		} else if(args[0].equalsIgnoreCase("tp")) {
			Mine curMine = Mine.get(args[1]);
			((Player) player).teleport(curMine.getRegion().getMaximum());
			Message.sendCustom("DEBUG", "Teleported to: " + curMine.getId());
			return true;
		} else if(args[0].equalsIgnoreCase("unload")){
			List<Mine> mines = PrisonMine.getMines();
			mines.remove(Mine.get(args[1]));
			PrisonMine.setMines(mines);
			Message.sendCustom("DEBUG", "Unloaded " + args[1] + " from memory");
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
		Message.formatHelp("unload", "<id>", "Unloads the mine from the memory. The mine will be loaded back on server restart");
	}

	@Override
	public void getHelpLine() { }

}
