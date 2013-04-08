package com.wolvencraft.prison.mines.util.variables;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class BlockStatVars implements BaseVar {

    @Override
    public String parse(Mine mine, String option) {
        if(option.equalsIgnoreCase("tblocks")) {
            String tblocks = mine.getTotalBlocks() + "";
            if(tblocks.length() > 5) tblocks = tblocks.substring(0, 5);
            return tblocks;
        }
        else if(option.equalsIgnoreCase("rblocks")) {
            String rblocks = mine.getBlocksLeft() + "";
            if(rblocks.length() > 5) rblocks = rblocks.substring(0, 5);
            return rblocks;
        }
        else if(option.equalsIgnoreCase("pblocks")) {
            String pblocks = mine.getCurrentPercent() + "";
            if(pblocks.length() > 5) pblocks = pblocks.substring(0, 5);
            return pblocks;
        }
        return "";
    }

    @Override
    public void getHelp() {
        Message.send("+ Mine block statistics");
        Message.send("|- " + ChatColor.GOLD + "<TBLOCKS> " + ChatColor.WHITE + "Total number of blocks in the mine", false);
        Message.send("|- " + ChatColor.GOLD + "<RBLOCKS> " + ChatColor.WHITE + "Remaining blocks in the mine", false);
        Message.send("|- " + ChatColor.GOLD + "<PBLOCKS> " + ChatColor.WHITE + "Percentage of the mine that is not air", false);
        Message.send("");
    }

}
