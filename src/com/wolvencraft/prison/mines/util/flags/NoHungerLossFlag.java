package com.wolvencraft.prison.mines.util.flags;

public class NoHungerLossFlag implements BaseFlag {
	
	private String option ="";
	
	@Override
	public String getName() { return "nohungerloss"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}
