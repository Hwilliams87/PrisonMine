package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.constants.MineVariable;

public class VariablesCommand implements BaseCommand {
	
	@Override
	public boolean run(String[] args) { getHelp(); return true; }

	@Override
	public void getHelp() {
		Message.formatHeader(20, "Variables");
		for(MineVariable cmd : MineVariable.values()) cmd.getHelp();
	}
	
	@Override
	public void getHelpLine() { Message.formatHelp("variables", "", "Displays all available variables"); }
}
