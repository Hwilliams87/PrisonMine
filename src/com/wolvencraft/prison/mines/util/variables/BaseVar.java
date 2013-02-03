package com.wolvencraft.prison.mines.util.variables;

import com.wolvencraft.prison.mines.mine.Mine;

public interface BaseVar {
	public String parse(Mine mine, String option);
	public void getHelp();
}
