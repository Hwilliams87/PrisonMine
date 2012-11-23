package com.wolvencraft.prison.mines.cmd;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;


public class TimerCommand  implements BaseCommand {
	public boolean run(String[] args) {
		
		if(args.length == 1) {
			getHelp();
			return true;
		}

		Mine curMine = CommandManager.getCurrentMine();
		if(curMine == null) {
			Message.sendError(PrisonMine.getLanguage().ERROR_MINENOTSELECTED);
			return false;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 2) {
				Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
				return false;
			}
			
			if(curMine.getAutomatic()) {
				curMine.setResetAutomatically(false);
				Message.sendCustom(curMine.getName(), "Automatic mine reset is " + ChatColor.RED + "off");
			}
			else {
				curMine.setResetAutomatically(true);
				if(curMine.getResetPeriod() == 0) curMine.setResetPeriod(900);
				Message.sendCustom(curMine.getName(), "Automatic mine reset is " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("set")) {
			if(args.length != 3) {
				Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
				return false;
			}
			int time = Util.parseTime(args[2]);
			if(time <= 0) {
				Message.sendError("Invalid time provided");
				return false;
			}
			curMine.setResetPeriod(time);
			String parsedTime = Util.parseSeconds(time);
			Message.sendCustom(curMine.getName(), "Mine will now reset every " + ChatColor.GOLD + parsedTime + ChatColor.WHITE + " minute(s)");
		}
		else if(args[1].equalsIgnoreCase("warning")) {
			if(args.length < 3) {
				Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
				return false;
			}
			
			if(args[2].equalsIgnoreCase("toggle")) {
				if(args.length != 3) {
					Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
					return false;
				}
				
				if(curMine.getWarned()) {
					curMine.setWarned(false);
					Message.sendCustom(curMine.getName(), "Reset warnings are " + ChatColor.RED + "off");
				}
				else {
					curMine.setWarned(true);
					Message.sendCustom(curMine.getName(), "Reset warnings are " + ChatColor.GREEN + "on");
				}
			}
			else if(args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("+")) {
				if(args.length != 4) {
					Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
					return false;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					Message.sendError("Invalid time provided");
					return false;
				}
				if(time > curMine.getResetPeriod()) {
					Message.sendError("Time cannot be set to a value greater then the reset time");
					return false;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				warnList.add(time);
				String parsedTime = Util.parseSeconds(time);
				Message.sendSuccess(curMine.getName() + " will now send warnings " + ChatColor.GOLD + parsedTime + ChatColor.WHITE + " minute(s) before the reset");
			}
			else if(args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("-")) {
				if(args.length != 4) {
					Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
					return false;
				}
				
				int time = Util.parseTime(args[3]);
				if(time <= 0) {
					Message.sendError("Invalid time provided");
					return false;
				}
				
				List<Integer> warnList = curMine.getWarningTimes();
				int index = warnList.indexOf(time);
				if(index == -1) {
					Message.sendError("'" + curMine.getName() + "' does not send a warning " + ChatColor.GOLD + Util.parseSeconds(time) + ChatColor.WHITE + " minute(s) before the reset");
					return false;
				}
				
				warnList.remove(index);
				Message.sendSuccess(curMine.getName() + " will no longer send a warning " + ChatColor.GOLD + Util.parseSeconds(time) + ChatColor.WHITE + " minute(s) before the reset");
			}
			else {
				Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
				return false;
			}
		}
		else {
			Message.sendError(PrisonMine.getLanguage().ERROR_COMMAND);
			return false;
		}
		
		return curMine.save();
	}

	public void getHelp() {
		Message.formatHeader(20, "Timer");
		Message.formatHelp("timer", "toggle", "Toggles the automatic resets on and off");
		Message.formatHelp("timer", "set <time>", "Sets the reset time");
		Message.formatHelp("timer", "warning toggle", "Toggles reset warnings on and off");
		Message.formatHelp("timer", "warning + <time>", "Adds a warning at time specified");
		Message.formatHelp("timer", "warning - <time>", "Adds a warning at time specified");
		return;
	}
	
	public void getHelpLine() { Message.formatHelp("timer", "", "Shows the automatic reset options", "mcprison.mine.edit"); }
}
