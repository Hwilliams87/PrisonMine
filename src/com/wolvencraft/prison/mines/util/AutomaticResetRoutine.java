package com.wolvencraft.prison.mines.util;

import java.util.logging.Level;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;

public class AutomaticResetRoutine {
	public static void run(Mine mine) {

		String generator = mine.getGenerator();
		
		if(ExtensionLoader.get(generator) == null) {
			Message.log(Level.SEVERE, "Invalid generator specified!");
			return;
		}
		
		if(mine.getAutomaticReset() && (mine.getResetsIn() <= 0 || PrisonMine.getSettings().RESET_FORCE_TIMER_UPDATE)) {
			Message.debug("| Resetting the timer (config)");
			mine.resetTimer();
		}
		
		if(!(mine.reset(generator))) {
			Message.debug("| Error while executing the generator! Aborting.");
			Message.debug("+---------------------------------------------");
			return;
		}
		
		if(mine.getCooldown()) mine.resetCooldown();
		
		String broadcastMessage = PrisonMine.getLanguage().RESET_AUTOMATIC;
		
		if(mine.getAutomaticReset() && mine.getResetsIn() <= 0)
			broadcastMessage = PrisonMine.getLanguage().RESET_TIMED;
		
		if(mine.getCompositionReset() && mine.getCurrentPercent() <= mine.getRequiredPercent())
			broadcastMessage = PrisonMine.getLanguage().RESET_COMPOSITION;
		
		if(mine.getParent() == null && !mine.getSilent()) {
			broadcastMessage = Util.parseVars(broadcastMessage, mine);
			Message.broadcast(broadcastMessage);
		}
		
		for(Mine childMine : mine.getChildren()) {
			Message.debug("+---------------------------------------------");
			Message.debug("| Mine " + childMine.getId() + " is resetting. Reset report:");
			Message.debug("| Reset cause: parent mine is resetting (" + mine.getId() + ")");
			CommandManager.RESET.run(childMine.getId());
			Message.debug("| Reached the end of the report for " + childMine.getId());
			Message.debug("+---------------------------------------------");
		}
	}
}
