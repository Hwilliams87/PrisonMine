package com.wolvencraft.prison.mines.util.flags;

import com.wolvencraft.prison.util.Util;

public class SurfaceOreFlag implements BaseFlag {
	
	private String option ="1";
	
	@Override
	public String getName() { return "SurfaceOre"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

	@Override
	public boolean isOptionValid(String option) { 
		if(Util.getBlock(option) == null) return false;
		return true;
	}

}