package com.wolvencraft.prison.mines.cmd;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.wolvencraft.prison.PrisonSuite;
import com.wolvencraft.prison.hooks.WorldEditHook;
import com.wolvencraft.prison.region.PrisonRegion;
import com.wolvencraft.prison.region.PrisonSelection;
import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class SaveCommand implements BaseCommand {
	
	@Override
	public boolean run(String[] args) {
		
		if(args.length == 1) { getHelp(); return true; }
		if(args.length > 3) { Message.sendFormattedError(PrisonMine.getLanguage().ERROR_ARGUMENTS); return false; }
		
		Player player = (Player) CommandManager.getSender();
		
		PrisonSelection sel = PrisonSuite.getSelection(player);
		if(!sel.locationsSet()) {
			if(!PrisonSuite.usingWorldEdit()) { Message.sendFormattedError("Make a selection first"); return false; }
			else {
				Location[] loc = WorldEditHook.getPoints(player);
				if(loc == null) { Message.sendFormattedError("Make a selection first"); return false; }
				sel.setCoordinates(loc);
			}
		}
		
		if(!sel.getPos1().getWorld().equals(sel.getPos2().getWorld())) { Message.sendFormattedError("Your selection points are in different worlds"); return false; }

		if(Mine.get(args[1]) != null) { Message.sendFormattedError("Mine '" + args[1] + "' already exists!"); return false; }
		
		for(String bannedName : PrisonMine.getSettings().BANNEDNAMES) {
			if(args[1].equalsIgnoreCase(bannedName)) { Message.sendFormattedError("This name is not valid"); return false; }
		}
		
		Mine newMine = new Mine(args[1], new PrisonRegion(sel), sel.getPos1().getWorld(), player.getLocation());
		
		PrisonMine.addMine(newMine);
		PrisonMine.setCurMine(newMine);
		
		Message.sendFormattedMine("Mine created successfully!");
		return newMine.saveFile();
	}
	
	@Override
	public void getHelp() { Message.formatHeader(20, "Mine creation"); getHelpLine(); }
	
	@Override
	public void getHelpLine() { Message.formatHelp("create", "<id>", "Saves the mine region to file", "prison.mine.edit"); }
}
