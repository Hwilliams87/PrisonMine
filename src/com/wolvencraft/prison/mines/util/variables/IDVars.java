package com.wolvencraft.prison.mines.util.variables;

import java.util.List;

import com.wolvencraft.prison.mines.mine.Mine;

public class IDVars implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		if(option == null) return mine.getId();
		String mineIds = mine.getId();
		List<Mine> children = mine.getChildren();
		if(!children.isEmpty()) {
			for(Mine childMine : children) {
				mineIds += ", " + childMine.getId();
			}
		}
		return mineIds;
	}

}
