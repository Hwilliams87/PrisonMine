package com.wolvencraft.prison.mines.util.variables;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class BlockStatVars implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		if(option.equalsIgnoreCase("tblocks")) return mine.getTotalBlocks() + "";
		else if(option.equalsIgnoreCase("rblocks")) return mine.getBlocksLeft() + "";
		else if(option.equalsIgnoreCase("pblocks")) return mine.getCurrentPercent() + "";
		return "";
	}

	@Override
	public void getHelp() {
		Message.send("+ Mine block statistics");
		Message.send("|- " + ChatColor.GOLD + "<TBLOCKS> " + ChatColor.WHITE + "Total number of blocks in the mine");
		Message.send("|- " + ChatColor.GOLD + "<RBLOCKS> " + ChatColor.WHITE + "Remaining blocks in the mine");
		Message.send("|- " + ChatColor.GOLD + "<PBLOCKS> " + ChatColor.WHITE + "Percentage of the mine that is not air");
		Message.send("");
	}

}
