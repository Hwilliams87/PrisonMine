package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.util.Message;

public class FlagCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void getHelp() {
		Message.formatHeader(20, "Flags");
		Message.formatHelp("flag", "<flag> <value>", "Adds a flag value to the mine");
		String[] validFlags = PrisonMine.getSettings().VALIDFLAGS;
		String flagString = validFlags[0];
		for(int i = 1; i < validFlags.length; i++) {
			flagString += ", " + validFlags[i];
		}
		Message.send("Available flags: "+ flagString);
	}

	@Override
	public void getHelpLine() {
		Message.formatHelp("flag", "", "Shows the help page on mine flags", "prison.mine.edit");
	}
}
