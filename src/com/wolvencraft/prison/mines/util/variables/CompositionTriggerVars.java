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
		if(option.equalsIgnoreCase("nper")) {
			String nper = curMine.getCurrentPercent() + "";
			if(nper.length() > 5) nper = nper.substring(0, 5);
			return nper;
		}
		else if(option.equalsIgnoreCase("pper")) {
			String pper = curMine.getRequiredPercent() + "";
			if(pper.length() > 5) pper = pper.substring(0, 5);
			return pper;
		}
		return "";
	}

	@Override
	public void getHelp() {
		Message.send("+ Composition Trigger variables");
		Message.send("|- " + ChatColor.GOLD + "<NPER> " + ChatColor.WHITE + "Percentage of the mine taken by non-air blocks", false);
		Message.send("|- " + ChatColor.GOLD + "<PPER> " + ChatColor.WHITE + " Percentage of the mine that required for a reset", false);
		Message.send("");
	}

}
