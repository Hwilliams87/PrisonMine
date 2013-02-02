package com.wolvencraft.prison.mines.util.variables;

import com.wolvencraft.prison.mines.mine.Mine;

public class BlockStatVars implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		if(option.equalsIgnoreCase("tblocks")) return mine.getTotalBlocks() + "";
		else if(option.equalsIgnoreCase("rblocks")) return mine.getBlocksLeft() + "";
		else if(option.equalsIgnoreCase("pblocks")) return mine.getBlocksLeft() + "";
		return "";
	}

}
