package com.wolvencraft.prison.mines.util.variables;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

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

	@Override
	public void getHelp() {
		Message.send("+ Mine display names");
		Message.send("|- " + ChatColor.GOLD + "<NAME> " + ChatColor.WHITE + "Display name of the mine", false);
		Message.send("|- " + ChatColor.GOLD + "<NAMES> " + ChatColor.WHITE + " Display name of the mine and its children", false);
		Message.send("");
	}

}
