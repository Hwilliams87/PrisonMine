package com.wolvencraft.prison.mines.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.settings.Language;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

public class BlacklistCommand implements BaseCommand {
	
	public boolean run(String[] args) {

		if(args.length == 1) {
			getHelp();
			return true;
		}

		Language language = PrisonMine.getLanguage();
		
		Mine curMine = PrisonMine.getCurMine();
		if(curMine == null) {
			Message.sendError(language.ERROR_MINENOTSELECTED);
			return false;
		}
		
		if(args[1].equalsIgnoreCase("toggle")) {
			if(args.length != 2) {
				Message.sendError(language.ERROR_ARGUMENTS);
				return false;
			}
			if(curMine.getBlacklist().getEnabled()) {
				curMine.getBlacklist().setEnabled(false);
				Message.sendCustom(curMine.getId(), "Blacklist turned " + ChatColor.RED + "off");
			}
			else {
				curMine.getBlacklist().setEnabled(true);
				Message.sendCustom(curMine.getId(), "Blacklist turned " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("whitelist")) {
			if(curMine.getBlacklist().getWhitelist()) {
				curMine.getBlacklist().setWhitelist(false);
				Message.sendCustom(curMine.getId() + " Blacklist", "Whitelist mode " + ChatColor.RED + "off");
			}
			else {
				curMine.getBlacklist().setWhitelist(true);
				Message.sendCustom(curMine.getId() + " Blacklist", "Whitelist mode " + ChatColor.GREEN + "on");
			}
		}
		else if(args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("+")) {
			if(args.length != 3) {
				Message.sendError(language.ERROR_ARGUMENTS);
				return false;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null) {
				Message.sendError(language.ERROR_NOSUCHBLOCK.replaceAll("<BLOCK>", args[2]));
				return false;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.add(block);
			curMine.getBlacklist().setBlocks(blocks);
			Message.sendCustom(curMine.getId(), ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been added to the blacklistlist");
		}
		else if(args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("-")) {
			if(args.length != 3) {
				Message.sendError(language.ERROR_ARGUMENTS);
				return false;
			}
			
			MaterialData block = Util.getBlock(args[2]);
			if(block == null) {
				Message.sendError(language.ERROR_NOSUCHBLOCK.replaceAll("<BLOCK>", args[2]));
				return false;
			}
			
			List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
			blocks.remove(block);
			curMine.getBlacklist().setBlocks(blocks);
			Message.sendCustom(curMine.getId(), ChatColor.GREEN + Util.parseMaterialData(block) + ChatColor.WHITE + " has been removed from the list");
		}
		else {
			Message.sendError(language.ERROR_COMMAND);
			return false;
		}
		
		return curMine.saveFile();
	}
	
	public void getHelp() {
		Message.formatHeader(20, "Blacklist");
		Message.formatHelp("blacklist", "toggle", "Enables the use of blacklist for the mine");
		Message.formatHelp("blacklist", "whitelist", "Should the blacklist be treated as a whitelist?");
		Message.formatHelp("blacklist", "+ <block>", "Add <block> to the list");
		Message.formatHelp("blacklist", "- <block>", "Remove <block> from the list");
		return;
	}
	
	public void getHelpLine() { Message.formatHelp("blacklist", "", "More information on the reset blacklist", "prison.mine.edit"); }
}
