package com.wolvencraft.prison.mines.util.flags;

public class ResetSoundFlag implements BaseFlag {
	
	String option;
	
	@Override
	public String getName() { return "ResetSound"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}
