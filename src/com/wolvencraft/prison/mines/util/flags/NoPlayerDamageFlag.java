package com.wolvencraft.prison.mines.util.flags;

public class NoPlayerDamageFlag implements BaseFlag {
	
	private String option ="";
	
	@Override
	public String getName() { return "NoPlayerDamage"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

	@Override
	public boolean isOptionValid(String option) { return true; }

}
