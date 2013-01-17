package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.ExtensionLoader;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

public class ResetCommand implements BaseCommand {	
	public boolean run(String[] args) {
		
		if(args.length == 1) {
			getHelp();
			return true;
		}

		Mine curMine = null;
		String generator = "";
		
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("all")) {
				boolean success = true;
				for(Mine mine : PrisonMine.getLocalMines()) {
					if(!CommandManager.RESET.run(mine.getId())) success = false;
				}
				return success;
			}
			
			curMine = Mine.get(args[1]);
			generator = curMine.getGenerator();
		} else {
			curMine = Mine.get(args[1]);
			generator = args[2];
		} 
				
		if(curMine == null || ExtensionLoader.get(generator) == null) {
			Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
			return false;
		}
		
		String broadcastMessage = "";
		

		Message.debug("+---------------------------------------------");
		Message.debug("| Mine " + curMine.getId() + " is resetting. Reset report:");
		Message.debug("| Reset cause: MANUAL (command/sign)");
		
		if(!Util.hasPermission("prison.mine.reset.manual." + curMine.getId()) && !Util.hasPermission("prison.mine.reset.manual")) {
			Message.sendError(PrisonMine.getLanguage().ERROR_ACCESS);
			Message.debug("| Insufficient permissions. Cancelling...");
			Message.debug("| Reached the end of the report for " + curMine.getId());
			Message.debug("+---------------------------------------------");
			return false;
		}
		
		if(curMine.getCooldown() && curMine.getCooldownEndsIn() > 0 && !Util.hasPermission("prison.mine.bypass.cooldown")) {
			Message.sendError(Util.parseVars(PrisonMine.getLanguage().RESET_COOLDOWN, curMine));
			Message.debug("| Cooldown is in effect. Checking for bypass...");
			Message.debug("| Failed. Cancelling...");
			Message.debug("| Reached the end of the report for " + curMine.getId());
			Message.debug("+---------------------------------------------");
			return false;
		}
		
		if(curMine.getAutomaticReset() && PrisonMine.getSettings().RESET_FORCE_TIMER_UPDATE) {
			Message.debug("| Resetting the timer (config)");
			curMine.resetTimer();
		}
		
		broadcastMessage = PrisonMine.getLanguage().RESET_MANUAL;
		
		if(curMine.getCooldown()) curMine.resetCooldown();
		
		if(!(curMine.reset(generator))) {
			Message.debug("| Error while executing the generator! Aborting.");
			Message.debug("+---------------------------------------------");
			return false;
		}
		
		broadcastMessage = Util.parseVars(broadcastMessage, curMine);
		
		if(!curMine.getSilent()) Message.broadcast(broadcastMessage);
		else Message.sendSuccess(broadcastMessage);
		
		Message.debug("| Reached the end of the report for " + curMine.getId());
		Message.debug("+---------------------------------------------");
		
		return true;
	}
	
	public void getHelp() {
		Message.formatHeader(20, "Reset");
		Message.formatHelp("reset", "<name> [generator]", "Resets the mine manually");
		Message.formatMessage("Resets the mine according to the generation rules");
		Message.formatMessage("The following generators are supported: ");
		Message.formatMessage(ExtensionLoader.list());
		return;
	}
	
	public void getHelpLine() { Message.formatHelp("reset", "<name> [generator]", "Resets the mine manually", "prison.mine.reset.manual"); }
}
