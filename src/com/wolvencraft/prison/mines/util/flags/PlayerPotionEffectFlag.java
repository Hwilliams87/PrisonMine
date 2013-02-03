package com.wolvencraft.prison.mines.util.flags;

public class PlayerPotionEffectFlag implements BaseFlag {
	
	String option;
	
	@Override
	public String getName() { return "PlayerPotionEffect"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}
