package com.wolvencraft.prison.mines.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.settings.Language;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;
import com.wolvencraft.prison.mines.util.constants.BlacklistState;

public class BlacklistCommand implements BaseCommand {
	
	@Override
	public boolean run(String[] args) {

		if(args.length == 1) { getHelp(); return true; }

		Language language = PrisonMine.getLanguage();
		
		Mine curMine = PrisonMine.getCurMine();
		if(curMine == null) { Message.sendFormattedError(PrisonMine.getLanguage().ERROR_MINENOTSELECTED); return false; }
		
		if(args.length == 2) {
			if(args[2].equalsIgnoreCase("blacklist")) {				
				if(curMine.getBlacklist().getState().equals(BlacklistState.BLACKLIST)) {
					curMine.getBlacklist().setState(BlacklistState.DISABLED);
					Message.sendFormattedSuccess("The replacement rules are now disabled");
				} else {
					curMine.getBlacklist().setState(BlacklistState.BLACKLIST);
					Message.sendFormattedSuccess("The replacement rules are now in blacklist mode");
				}
			} else if(args[2].equalsIgnoreCase("whitelist")) {				
				if(curMine.getBlacklist().getState().equals(BlacklistState.WHITELIST)) {
					curMine.getBlacklist().setState(BlacklistState.DISABLED);
					Message.sendFormattedSuccess("The replacement rules are now disabled");
				} else {
					curMine.getBlacklist().setState(BlacklistState.WHITELIST);
					Message.sendFormattedSuccess("The replacement rules are now in whitelist mode");
				}
			} 
		} else if(args.length == 3) {
			if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("+")) {
				if(args.length != 3) { Message.sendFormattedError(language.ERROR_ARGUMENTS); return false; }
				
				MaterialData block = Util.getBlock(args[2]);
				if(block == null) { Message.sendFormattedError(language.ERROR_NOSUCHBLOCK.replaceAll("<BLOCK>", args[2])); return false; }
				
				List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
				blocks.add(block);
				curMine.getBlacklist().setBlocks(blocks);
				Message.sendFormattedMine(ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been added to the blacklistlist");
			}
			else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("-")) {
				if(args.length != 3) { Message.sendFormattedError(language.ERROR_ARGUMENTS); return false; }
				
				MaterialData block = Util.getBlock(args[2]);
				if(block == null) { Message.sendFormattedError(language.ERROR_NOSUCHBLOCK.replaceAll("<BLOCK>", args[2])); return false; }
				
				List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
				blocks.remove(block);
				curMine.getBlacklist().setBlocks(blocks);
				Message.sendFormattedMine(ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been removed from the list");
			}
			else { Message.sendFormattedError(language.ERROR_COMMAND); return false; }
		} else { Message.sendFormattedError(language.ERROR_ARGUMENTS); return false; }
		
		return curMine.saveFile();
	}
	
	@Override
	public void getHelp() {
		Message.formatHeader(20, "Blacklist");
		Message.formatHelp("blacklist", "", "Toggles the use of the blacklist for the mine");
		Message.formatHelp("whitelist", "", "Toggles the use of the whitelist for the mine");
		Message.formatHelp("blacklist", "+ <block>", "Add <block> to the list");
		Message.formatHelp("blacklist", "- <block>", "Remove <block> from the list");
		return;
	}
	
	@Override
	public void getHelpLine() { Message.formatHelp("blacklist", "", "More information on the reset blacklist", "prison.mine.edit"); }
}
