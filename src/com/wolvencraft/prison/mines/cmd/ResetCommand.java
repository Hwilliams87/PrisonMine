package com.wolvencraft.prison.mines.cmd;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;
import com.wolvencraft.prison.mines.util.constants.MineFlag;

public class ResetCommand implements BaseCommand {
	
	@Override
	public boolean run(String[] args) {
		
		Mine curMine = null;
		
		if(args.length == 1) {
			curMine = PrisonMine.getCurMine();
			if(curMine == null) { getHelp(); return true; }
		} else if(args.length == 2) {
			if(args[1].equalsIgnoreCase("all")) {
				boolean success = true;
				for(Mine mine : PrisonMine.getStaticMines()) {
					if(!CommandManager.RESET.run(mine.getId())) success = false;
				}
				return success;
			} else if(args[1].equalsIgnoreCase("help")) { getHelp(); return true; }
			curMine = Mine.get(args[1]);
			if(curMine == null) { Message.sendFormattedError(PrisonMine.getLanguage().ERROR_ARGUMENTS); return false; }
		} else { Message.sendFormattedError(PrisonMine.getLanguage().ERROR_ARGUMENTS); return false; }
		
		String broadcastMessage = "";

		Message.debug("+---------------------------------------------");
		Message.debug("| Mine " + curMine.getId() + " is resetting. Reset report:");
		Message.debug("| Reset cause: MANUAL (command/sign)");
		
		if(!Util.hasPermission("prison.mine.reset.manual." + curMine.getId()) && !Util.hasPermission("prison.mine.reset.manual")) {
			Message.sendFormattedError(PrisonMine.getLanguage().ERROR_ACCESS);
			Message.debug("| Insufficient permissions. Cancelling...");
			Message.debug("| Reached the end of the report for " + curMine.getId());
			Message.debug("+---------------------------------------------");
			return false;
		}
		
		if(curMine.getCooldown() && curMine.getCooldownEndsIn() > 0 && !Util.hasPermission("prison.mine.bypass.cooldown")) {
			Message.sendFormattedError(Util.parseVars(PrisonMine.getLanguage().RESET_COOLDOWN, curMine));
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
		
		if(!(curMine.reset())) {
			Message.debug("| Error while executing the generator! Aborting.");
			Message.debug("+---------------------------------------------");
			return false;
		}
		
		broadcastMessage = Util.parseVars(broadcastMessage, curMine);
		
		if(!curMine.hasFlag(MineFlag.Silent)) Message.broadcast(broadcastMessage);
		else Message.sendFormattedSuccess(broadcastMessage);
		
		if(PrisonMine.getSettings().RESET_TRIGGERS_CHILDREN_RESETS) {
			for(Mine childMine : curMine.getChildren()) {
				Message.debug("+---------------------------------------------");
				Message.debug("| Mine " + childMine.getId() + " is resetting. Reset report:");
				Message.debug("| Reset cause: parent mine is resetting (" + curMine.getId() + ")");
				CommandManager.RESET.run(childMine.getId());
				Message.debug("| Reached the end of the report for " + childMine.getId());
				Message.debug("+---------------------------------------------");
			}
		}
		
		Message.debug("| Reached the end of the report for " + curMine.getId());
		Message.debug("+---------------------------------------------");
		
		if(CommandManager.getSender() instanceof ConsoleCommandSender) curMine.setLastResetBy("CONSOLE");
		else curMine.setLastResetBy(((Player) CommandManager.getSender()).getPlayerListName());
		
		return true;
	}
	
	@Override
	public void getHelp() { Message.formatHeader(20, "Manual Reset"); getHelpLine(); }
	
	@Override
	public void getHelpLine() { Message.formatHelp("reset", "<name>", "Resets the mine manually", "prison.mine.reset.manual"); }
}
