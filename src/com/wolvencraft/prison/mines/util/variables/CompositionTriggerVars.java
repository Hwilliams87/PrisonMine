package com.wolvencraft.prison.mines.util.variables;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class CompositionTriggerVars implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		Mine curMine;
		if(mine.hasParent()) curMine = Mine.get(mine.getParent());
		else curMine = mine;
		if(!mine.getCompositionReset()) return "<...>";
		if(option.equalsIgnoreCase("nper")) return curMine.getCurrentPercent() + "";
		else if(option.equalsIgnoreCase("pper")) return curMine.getRequiredPercent() + "";
		return "";
	}

	@Override
	public void getHelp() {
		Message.send("+ Composition Trigger variables");
		Message.send("|- " + ChatColor.GOLD + "<NPER> " + ChatColor.WHITE + "Percentage of the mine taken by non-air blocks");
		Message.send("|- " + ChatColor.GOLD + "<PPER> " + ChatColor.WHITE + " Percentage of the mine that required for a reset");
		Message.send("");
	}

}
