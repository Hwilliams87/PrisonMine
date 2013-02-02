package com.wolvencraft.prison.mines.util.variables;

import java.util.List;

import com.wolvencraft.prison.mines.mine.Mine;

public class NameVars implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		if(option == null) return mine.getName();
		String mineNames = mine.getName();
		List<Mine> children = mine.getChildren();
		if(!children.isEmpty()) {
			for(Mine childMine : children) {
				mineNames += ", " + childMine.getName();
			}
		}
		return mineNames;
	}

}
