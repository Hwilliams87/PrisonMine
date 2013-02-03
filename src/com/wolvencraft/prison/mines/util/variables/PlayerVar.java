package com.wolvencraft.prison.mines.util.variables;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class PlayerVar implements BaseVar {

	@Override
	public String parse(Mine mine, String option) {
		return mine.getLastResetBy();
	}

	@Override
	public void getHelp() {
		Message.send("+ Player variables");
		Message.send("|- " + ChatColor.GOLD + "<PLAYER> " + ChatColor.WHITE + "The name of the player who last rese the mine", false);
		Message.send("");
	}

}
