package com.wolvencraft.prison.mines.cmd;

public interface BaseCommand {
	public boolean run(String[] args) throws Exception ;
	public void getHelp();
	public void getHelpLine();
}
