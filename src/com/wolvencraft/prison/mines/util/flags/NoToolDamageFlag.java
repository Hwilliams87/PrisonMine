package com.wolvencraft.prison.mines.util.flags;

public class NoToolDamageFlag implements BaseFlag {
	
	private String option ="";
	
	@Override
	public String getName() { return "notooldamage"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}
