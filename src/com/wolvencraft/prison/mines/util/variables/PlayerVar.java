package com.wolvencraft.prison.mines.util.variables;

import com.wolvencraft.prison.mines.mine.Mine;

public class PlayerVar implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		return mine.getLastResetBy();
	}

}
