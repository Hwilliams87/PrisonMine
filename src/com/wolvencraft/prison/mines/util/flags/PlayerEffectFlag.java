package com.wolvencraft.prison.mines.util.flags;

public class PlayerEffectFlag implements BaseFlag {
	
	String option;
	
	@Override
	public String getName() { return "PlayerEffect"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}