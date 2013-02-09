package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.util.Message;

import org.bukkit.ChatColor;

public class MetaCommand  implements BaseCommand {
	
	@Override
    public boolean run(String[] args) {
        Message.formatHeader(20, PrisonMine.getLanguage().GENERAL_TITLE);
        Message.send(ChatColor.GREEN + "PrisonMine version " + ChatColor.BLUE + PrisonMine.getInstance().getVersion());
        Message.send(ChatColor.YELLOW + "http://dev.bukkit.org/server-mods/prisonmine/");
        Message.send("Creator: " + ChatColor.AQUA + "bitWolfy");
        Message.send("Contributor: " + ChatColor.AQUA + "jjkoletar");
        Message.send("Testers: " + ChatColor.AQUA + "theangrytomato" + ChatColor.WHITE + ", " + ChatColor.AQUA + "Ramo909" + ChatColor.WHITE + ", " + ChatColor.AQUA + "Speedrookie");
        return true;
    }
    
	@Override
    public void getHelp() {}
	
	@Override
    public void getHelpLine() { Message.formatHelp("about", "", "Shows the basic information about the plugin"); }
}
