package com.wolvencraft.prison.mines.util.variables;

import java.util.List;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

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

    @Override
    public void getHelp() {
        Message.send("+ Mine unique IDs");
        Message.send("|- " + ChatColor.GOLD + "<ID> " + ChatColor.WHITE + "Unique ID of the mine", false);
        Message.send("|- " + ChatColor.GOLD + "<IDS> " + ChatColor.WHITE + "ID of the mine and all of its children", false);
        Message.send("");
    }

}
