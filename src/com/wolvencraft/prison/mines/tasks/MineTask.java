package com.wolvencraft.prison.mines.tasks;

import java.util.List;

import com.wolvencraft.prison.hooks.TimedTask;
import com.wolvencraft.prison.mines.MineCommand;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

public class MineTask implements TimedTask {
	
	long period;
	boolean canceled;
	Mine mine;
	
	public MineTask(Mine mine) {
		period = PrisonMine.getSettings().TICKRATE;
		canceled = false;
		this.mine = mine;
	}
	
	public void run() {
		if(mine.getAutomatic() && Mine.get(mine.getParent()) == null) {
			int nextReset = mine.getResetsInSafe();
			List<Integer> warnTimes = mine.getWarningTimes();
			
			if(!mine.getSilent() && mine.getWarned() && warnTimes.indexOf(new Integer(nextReset)) != -1)
				Message.broadcast(Util.parseVars(PrisonMine.getLanguage().RESET_WARNING, mine));
			
			if(nextReset <= 0) {
				MineCommand.RESET.run(mine.getName());
			}
			mine.updateTimer(PrisonMine.getSettings().TICKRATE);
		}
	
		if(mine.getCooldown() && mine.getCooldownEndsIn() > 0)
			mine.updateCooldown(PrisonMine.getSettings().TICKRATE);
	}
	
	public String getName() 	{ return "MineTask:" + mine.getId(); }
	public boolean getExpired() { return canceled; }
	public void cancel() 		{ canceled = true; }
}
