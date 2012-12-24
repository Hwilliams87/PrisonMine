package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.CommandHandler;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.ExtensionLoader;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

public class ResetCommand implements BaseCommand {	
	public boolean run(String[] args) {
		
		Mine curMine = null;
		String generator = "";
		if(args.length == 1) {
			getHelp();
			return true;
		} else if(args.length == 2) {
			if(args[1].equalsIgnoreCase("all")) {
				boolean success = true;
				for(Mine mine : PrisonMine.getMines()) {
					if(!CommandHandler.RESET.run(mine.getId())) success = false;
				}
				return success;
			} else curMine = Mine.get(args[1]);
		} else if(args.length == 3) {
			curMine = Mine.get(args[1]);
			generator = args[2];
		} else {
			Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
			return false;
		}
				
		if(curMine == null) {
			Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
			return false;
		}
		
		Message.debug("Resetting mine: " + curMine.getId());
		boolean automatic;
		if(CommandManager.getSender() == null) {
			automatic = true;
			Message.debug("Automatic reset!");
		} else {
			automatic = false;
			Message.debug("Manual reset!");
		}
		
		if(!automatic) {
			if(!Util.hasPermission("prison.mine.reset.manual." + curMine.getId()) && !Util.hasPermission("prison.mine.reset.manual")) {
				Message.sendError(PrisonMine.getLanguage().ERROR_ACCESS);
				return false;
			}
			
			if(curMine.getCooldown() && curMine.getCooldownEndsIn() > 0 && !Util.hasPermission("prison.mine.bypass.cooldown")) {
				Message.sendError(Util.parseVars(PrisonMine.getLanguage().RESET_COOLDOWN, curMine));
				return false;
			}
		}

		if(generator.equals("")) generator = curMine.getGenerator();
		
		if(curMine.getCooldown()) curMine.resetCooldown();
		
		if(curMine.getAutomaticReset() && PrisonMine.getSettings().RESETTIMER) curMine.resetTimer();
		
		if(!(curMine.reset(generator))) return false;
		
		String broadcastMessage;
		if(automatic) {
			for(Mine childMine : curMine.getChildren()) {
				Message.debug(childMine.getId() + " inherits from " + curMine.getId() + " and is scheduled for reset");
				CommandHandler.RESET.run(childMine.getId());
			}
			
			if(curMine.getAutomaticReset() && curMine.getResetsIn() <= 0)
				broadcastMessage = PrisonMine.getLanguage().RESET_TIMED;
			else if(curMine.getCompositionReset() && curMine.getPercent() <= curMine.getCompositionPercent())
				broadcastMessage = PrisonMine.getLanguage().RESET_COMPOSITION;
			else
				broadcastMessage = PrisonMine.getLanguage().RESET_AUTOMATIC;
		} else broadcastMessage = PrisonMine.getLanguage().RESET_MANUAL;
		
		if(curMine.getParent() == null) {
			broadcastMessage = Util.parseVars(broadcastMessage, curMine);
			
			if(!curMine.getSilent()) Message.broadcast(broadcastMessage);
			else if(!automatic) Message.sendSuccess(broadcastMessage);
		}
		
		curMine.recountBlocks();
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
