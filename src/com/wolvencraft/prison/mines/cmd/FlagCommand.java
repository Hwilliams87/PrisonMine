package com.wolvencraft.prison.mines.cmd;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.MineFlag;
import com.wolvencraft.prison.mines.settings.Language;
import com.wolvencraft.prison.mines.util.Message;

public class FlagCommand implements BaseCommand {

	@Override
	public boolean run(String[] args) {
		if(args.length == 1) {
			getHelp();
			return true;
		}
		
		Language language = PrisonMine.getLanguage();
		if(args.length != 2) {
			Message.sendFormattedError(language.ERROR_ARGUMENTS);
			return false;
		}
		
		Mine curMine = PrisonMine.getCurMine();
		if(curMine == null) {
			Message.sendFormattedError(language.ERROR_MINENOTSELECTED);
			return false;
		}
		
		MineFlag flag = MineFlag.get(args[1]);
		if(flag == null) {
			Message.sendFormattedError("This flag does not exist");
			return false;
		}
		
		if(curMine.hasFlag(flag)) {
			curMine.removeFlag(flag);
			Message.sendFormattedMine("Flag " + flag + " has been removed");
		} else {
			curMine.addFlag(flag);
			Message.sendFormattedMine("Flag " + flag + " has been added");
		}
		
		return curMine.saveFile();
	}

	@Override
	public void getHelp() {
		Message.formatHeader(20, "Flags");
		Message.formatHelp("flag", "<flag>", "Adds a flag value to the mine");
		MineFlag[] validFlags = MineFlag.values();
		String flagString = validFlags[0].getAlias();
		for(int i = 1; i < validFlags.length; i++) {
			flagString += ", " + validFlags[i].getAlias();
		}
		Message.send("Available flags: "+ flagString);
	}

	@Override
	public void getHelpLine() {
		Message.formatHelp("flag", "", "Shows the help page on mine flags", "prison.mine.edit");
	}
}
