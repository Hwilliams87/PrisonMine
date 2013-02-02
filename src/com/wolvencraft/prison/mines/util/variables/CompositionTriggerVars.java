package com.wolvencraft.prison.mines.util.variables;

import com.wolvencraft.prison.mines.mine.Mine;

public class CompositionTriggerVars implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		Mine curMine;
		if(mine.hasParent()) curMine = Mine.get(mine.getParent());
		else curMine = mine;
		if(!mine.getCompositionReset()) return "<...>";
		if(option.equalsIgnoreCase("pper")) return curMine.getCurrentPercent() + "";
		else if(option.equalsIgnoreCase("nper")) return curMine.getRequiredPercent() + "";
		return "";
	}

}
