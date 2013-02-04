package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.settings.MineData;
import com.wolvencraft.prison.mines.settings.SignData;
import com.wolvencraft.prison.mines.util.Message;

public class DataCommand implements BaseCommand {
	public boolean run(String[] args) {
		
		if(args.length == 1) {
			getHelp();
			return true;
		}
		
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			PrisonMine.getInstance().reloadConfig();
			PrisonMine.getInstance().reloadSettings();
			PrisonMine.getInstance().reloadLanguageData();
			PrisonMine.getInstance().reloadLanguage();
			PrisonMine.setMines(MineData.loadAll());
			PrisonMine.setSigns(SignData.loadAll());
			Message.sendFormattedSuccess("Mine and sign data loaded from disk", false);
			return true;
		} else if(args.length == 2 && args[0].equalsIgnoreCase("data")) {
			if(args[1].equalsIgnoreCase("save")) {
				MineData.saveAll();
				SignData.saveAll();
				Message.sendFormattedSuccess("Mine and sign data saved to disk", false);
				return true;
			}
			else if(args[1].equalsIgnoreCase("load")) {
				PrisonMine.getInstance().reloadConfig();
				PrisonMine.getInstance().reloadSettings();
				PrisonMine.getInstance().reloadLanguageData();
				PrisonMine.getInstance().reloadLanguage();
				PrisonMine.setMines(MineData.loadAll());
				PrisonMine.setSigns(SignData.loadAll());
				Message.sendFormattedSuccess("Mine and sign data loaded from disk", false);
				return true;
			}
			else {
				Message.sendFormattedError(PrisonMine.getLanguage().ERROR_COMMAND);
				return false;
			}
		} else {
			Message.sendFormattedError(PrisonMine.getLanguage().ERROR_ARGUMENTS);
			return false;
		}
	}
	
	public void getHelp() {
		Message.formatHeader(20, "Data");
		Message.formatHelp("data", "save", "Saves the mine data to file");
		Message.formatHelp("data", "load", "Loads the mine data from file");
	}
	
	public void getHelpLine() { Message.formatHelp("data", "", "Shows information on data manipulation", "prison.mine.admin"); }
}
