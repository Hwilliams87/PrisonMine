package com.wolvencraft.prison.mines.util.flags;

public class SilentFlag implements BaseFlag {
	
	private String option ="";
	
	@Override
	public String getName() { return "silent"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}
