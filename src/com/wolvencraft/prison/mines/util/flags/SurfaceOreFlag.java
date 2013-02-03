package com.wolvencraft.prison.mines.util.flags;

public class SurfaceOreFlag implements BaseFlag {
	
	private String option ="1";
	
	@Override
	public String getName() { return "surfaceore"; }

	@Override
	public String getOption() { return option; }

	@Override
	public void setOption(String option) { this.option = option; }

}