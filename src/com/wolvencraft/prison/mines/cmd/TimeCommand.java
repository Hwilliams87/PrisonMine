package com.wolvencraft.prison.mines.cmd;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.settings.Language;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

public class TimeCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		Mine curMine = null;
		Language language = PrisonMine.getLanguage();
		
		if(args.length == 1) {
			curMine = PrisonMine.getCurMine();
			if(curMine == null) {
				getHelp();
				return true;
			}
		}
		else curMine = Mine.get(args[1]);
		
		if(args.length > 2) {
			Message.sendFormattedError(language.ERROR_ARGUMENTS);
			return false;
		}
		
		if(curMine == null) {
			Message.sendFormattedError(language.ERROR_MINENAME.replaceAll("<ID>", args[1]));
			return false;
		}
		Mine parentMine = curMine.getSuperParent();
		
		String displayString = "---==[ " + ChatColor.GREEN + ChatColor.BOLD + curMine.getName() + ChatColor.WHITE + " ]==---";
		for(int i = 0; i < 25 - (curMine.getName().length() / 2); i++) displayString = " " + displayString;
		Message.send(displayString);
		Message.send("");
		
		if(parentMine.getAutomaticReset())
			Message.send("    Resets every ->  " + ChatColor.GREEN + Util.parseSeconds(parentMine.getResetPeriodSafe()) + "    " + ChatColor.GOLD + Util.parseSeconds(parentMine.getResetsInSafe()) + ChatColor.WHITE + "  <- Next Reset");
		else Message.send("   Mine has to be reset manually");
		
		return false;
	}

	@Override
	public void getHelp() {
		Message.formatHelp("time", "<name>", "Shows the time until the timed reset", "prison.mine.info.time");
		
	}

	@Override
	public void getHelpLine() { getHelp(); }

}
