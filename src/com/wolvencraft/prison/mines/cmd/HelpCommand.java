package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.util.Message;

public class HelpCommand implements BaseCommand {
	public boolean run(String[] args) {
		Message.formatHeader(20, PrisonMine.getLanguage().GENERAL_TITLE);
		for(CommandManager cmd : CommandManager.values()) { cmd.getHelpLine(); }
		return true;
	}
	
	public void getHelp() {}
	public void getHelpLine() { Message.formatHelp("help", "", "Shows PrisonMine help"); };
}
