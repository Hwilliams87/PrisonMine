package com.wolvencraft.prison.mines.cmd;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.cmd.BaseCommand;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class ListCommand implements BaseCommand {

	public boolean run(String[] args) {
		
		if(args.length != 1) {
			Message.sendError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
			return false;
		}
		
		Message.send(ChatColor.DARK_RED + "                    -=[ " + ChatColor.GREEN + ChatColor.BOLD + "Public Mines" + ChatColor.DARK_RED + " ]=-");
		
		for(Mine mine : PrisonMine.getLocalMines()) {
			String displayName = mine.getName();
			if(displayName.equals(mine.getId()))
				Message.send(" - " + ChatColor.GREEN + mine.getId() + "");
			else
				Message.send(" - " + ChatColor.GREEN + displayName + ChatColor.WHITE + " (" + mine.getId() + ")");
		}
		
		return true;
	}

	public void getHelp() {}
	public void getHelpLine() { Message.formatHelp("list", "", "Lists all the available mines", "prison.mine.info.list"); }
}
