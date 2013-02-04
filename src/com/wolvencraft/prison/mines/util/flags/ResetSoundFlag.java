package com.wolvencraft.prison.mines.util.flags;

import org.bukkit.Sound;

public class ResetSoundFlag implements BaseFlag {
	
	String option;
	
	@Override
	public String getName() { return "ResetSound"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

	@Override
	public boolean isOptionValid(String option) { 
		try { Sound.valueOf(option); }
		catch (IllegalArgumentException iae) { return false; }
		catch (NullPointerException npe) { return false; }
		catch (Exception ex) { return false; }
		return true;
	}

}
