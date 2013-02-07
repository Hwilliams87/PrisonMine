package com.wolvencraft.prison.mines.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.settings.Language;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;
import com.wolvencraft.prison.mines.util.constants.BlacklistState;
import com.wolvencraft.prison.mines.util.constants.Protection;
import com.wolvencraft.prison.mines.util.flags.BaseFlag;

public class InfoCommand  implements BaseCommand {
	
	@Override
	public boolean run(String[] args) {
		Mine curMine = null;
		Language language = PrisonMine.getLanguage();
		
		if(args.length == 1) {
			curMine = PrisonMine.getCurMine();
			if(curMine == null) {
				getHelp();
				return true;
			}
		}
		else curMine = Mine.get(args[1]);
		
		if(args.length > 2) {
			Message.sendFormattedError(language.ERROR_ARGUMENTS);
			return false;
		}
		
		if(curMine == null) {
			Message.sendFormattedError(language.ERROR_MINENAME.replaceAll("<ID>", args[1]));
			return false;
		}
		
		Mine parentMine = curMine.getSuperParent();
		if(Util.hasPermission("prison.mine.info.*")) {
			
			List<String> text = new ArrayList<String>();
			text.add("");
			try {
				String title = ""+ ChatColor.GREEN + ChatColor.BOLD + curMine.getName() + ChatColor.WHITE;
				if(curMine.getParent() != null) title += " (" + ChatColor.GREEN + curMine.getParent() + ChatColor.WHITE + ") ";
				
				text.add("!c---===[ " + title + " ]===---");
				text.add("");
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the title"); }
			
			// Protection
			
			try {				
				String line = "";
				
				if(curMine.getProtection().contains(Protection.BLOCK_BREAK)) {
					if(curMine.getBreakBlacklist().getState().equals(BlacklistState.WHITELIST)) line += "[ " + ChatColor.YELLOW + "Block Breaking" + ChatColor.WHITE + "]";
					else line += "[ " + ChatColor.GREEN + "Block Breaking" + ChatColor.WHITE + "]";
					line += "   ";
				}
				
				if(curMine.getProtection().contains(Protection.PVP)) {
					line += "[ " + ChatColor.GREEN + "PVP" + ChatColor.WHITE + " ]";
					line += "   ";
				}
				
				if(curMine.getProtection().contains(Protection.BLOCK_PLACE)) {
					if(curMine.getPlaceBlacklist().getState().equals(BlacklistState.WHITELIST)) line += "[ " + ChatColor.YELLOW + "Block Placement" + ChatColor.WHITE + " ]";
					else line += "[ " + ChatColor.GREEN + "Block Placement" + ChatColor.WHITE + " ]";
				}
				
				if(!line.equals("")) {
					text.add("!c" + line);
					text.add("");
				}
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the protection information"); }
			
			// Triggers
			try {
				boolean automaticReset = parentMine.getAutomaticReset();
				boolean compositionReset = curMine.getCompositionReset();
				
				String line = "";
				
				if(automaticReset) line += "[ " + ChatColor.GREEN + Util.parseSeconds(parentMine.getResetsInSafe()) + ChatColor.WHITE + " | " + ChatColor.RED + Util.parseSeconds(parentMine.getResetPeriodSafe()) + ChatColor.WHITE + " ]";
				if(automaticReset && compositionReset) line += "      ";
				if(compositionReset) line += "[ " + ChatColor.GREEN + Util.round(curMine.getCurrentPercent()) + "%" + ChatColor.WHITE + " | " + ChatColor.RED + curMine.getRequiredPercent() + "%" + ChatColor.WHITE + " ]";
				
				text.add("!c" + line);
				text.add("");
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the trigger information"); }
			
			// Warnings
			try {
				if(parentMine.hasWarnings()) {
					String line = "";
					for(Integer warning : parentMine.getLocalWarningTimes()) {
						if(!line.equals("")) line += ",";
						line += " " + Util.parseSeconds(warning);
					}
					line = ChatColor.YELLOW + "Warnings: " + ChatColor.WHITE + line;
					text.add("!c" + line);
					text.add("");
				}
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the warning information"); }
			
			// Children
			try {
				List<Mine> children = curMine.getChildren();
				if(children.size() != 0) {
					String line = "";
					line = ChatColor.YELLOW + "   Children:" + ChatColor.WHITE;
					line += " " + children.get(0);
					if(children.size() > 1) {
						for(int i = 1; i < children.size(); i++) {
							line += ", " + children.get(i).getId();
						}
					}
					text.add("!c" + line);
					text.add("");
				}
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the children"); }
			
			// Flags
			try {
				List<BaseFlag> flags = curMine.getAllFlags();
				if(flags.size() != 0) {
					text.add(ChatColor.YELLOW + "   Flags:" + ChatColor.WHITE);
					for(BaseFlag flag : flags) {
						String str = flag.getName();
						if(!flag.getOption().equals("")) str += " (" + flag.getOption() + ")";
						text.add("    " + str);
					}
					text.add("");
				}
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the flags"); }
			
			// Mine composition
			try {
				text.add(ChatColor.YELLOW + "   Composition:" + ChatColor.WHITE);
				List<String> finalList = curMine.getBlocksSorted();
				for(int i = 0; i < (finalList.size() - 1); i += 2) {
					int spaces = 10;
					String line = finalList.get(i);
					if(line.length() > 25) spaces -= (line.length() - 25);
					else if(line.length() < 25) spaces += (25 - line.length());
					
					line = "        " + line;
					for(int j = 0; j < spaces; j++) line += " ";
					line += finalList.get(i + 1);
					text.add(line);
				}
				if(finalList.size() % 2 != 0) text.add("        " + finalList.get(finalList.size() - 1));
				text.add("");
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the mine composition"); }

			try {
				text.add("");
				BlacklistState blState = curMine.getBlacklist().getState();
				List<MaterialData> blocks = curMine.getBlacklist().getBlocks();
				
				String line = "[ ";
				if(!blState.equals(BlacklistState.DISABLED)) line += "[ " + ChatColor.GREEN + "Blacklist" + ChatColor.WHITE + " ]";
				else line += ChatColor.RED + "Blacklist" + ChatColor.WHITE + " ]";
				
				line += "     ";
				
				if(blState.equals(BlacklistState.WHITELIST)) line += "[ " + ChatColor.GREEN + "Whitelist" + ChatColor.WHITE + " ]";
				else line += "[ " + ChatColor.RED + "Whitelist" + ChatColor.WHITE + " ]";
				
				text.add("!c" + line);
				if(!blocks.isEmpty()) {
					text.add(ChatColor.BLUE + "    Blacklist Composition: ");
					for(MaterialData block : blocks) {
						String[] parts = {block.getItemTypeId() + "", block.getData() + ""};
						text.add("        - " + Util.parseMetadata(parts, true) + " " + block.getItemType().toString().toLowerCase().replace("_", " "));
					}
				}
				text.add("");
			} catch (Exception e) { Message.log(Level.SEVERE, "An error occurred while displaying the blacklist status"); }
			
			for(String line : text) {
				if(line.startsWith("!c")) {
					line = line.substring(2);
					int spacing = (55 - line.length()) / 2;
					for(int i = 0; i < spacing; i++) line = " " + line;
				}
				Message.send(line);
			}
			
			return true;
			
		} else if(Util.hasPermission("prison.mine.info.time")) {
			String displayString = "---==[ " + ChatColor.GREEN + ChatColor.BOLD + curMine.getName() + ChatColor.WHITE + " ]==---";
			for(int i = 0; i < 25 - (curMine.getName().length() / 2); i++) displayString = " " + displayString;
			Message.send(displayString);
			Message.send("");
			
			if(parentMine.getAutomaticReset())
				Message.send("    Resets every ->  " + ChatColor.GREEN + Util.parseSeconds(parentMine.getResetPeriodSafe()) + "    " + ChatColor.GOLD + Util.parseSeconds(parentMine.getResetsInSafe()) + ChatColor.WHITE + "  <- Next Reset");
			else Message.send("   Mine has to be reset manually");
			
		} else {
			Message.sendFormattedError(language.ERROR_ACCESS);
			return false;
		}
		return false;
	}
	
	@Override
	public void getHelp() {
		Message.formatHelp("info", "<name>", "Shows the basic mine information", "prison.mine.info.time");
		if(Util.hasPermission("prison.mine.info.*")) Message.formatMessage("Displays information about mine composition and reset times");
	}
	
	@Override
	public void getHelpLine() { Message.formatHelp("info", "<name>", "Shows the basic mine information", "prison.mine.info.time"); }
}