package com.wolvencraft.prison.mines.triggers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.wolvencraft.prison.PrisonSuite;
import com.wolvencraft.prison.mines.MineCommand;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

public class TimeTrigger implements BaseTrigger, ConfigurationSerializable {
	
	private long period;
	private long next;
	private Mine mine;
	
	private boolean canceled;
	
	public TimeTrigger(Mine mine, long period) {
		this.mine = mine;
		this.period = period;
		this.next = period;
		
		canceled = false;
		
		PrisonSuite.addTask(this);
	}
	
	public TimeTrigger(Map<String, Object> map) {
		mine = (Mine) map.get(Mine.get("mine"));
		period = (Long)(map.get("period"));
		next = (Long)(map.get("next"));
		
		canceled = false;
		
		PrisonSuite.addTask(this);
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mine", mine.getId());
		map.put("period", period);
		map.put("next", next);
		return map;
	}
	
	/**
	 * Bulk of the task. All timed actions go here.
	 */
	public void run() {
		if(!mine.hasParent()) {
			List<Integer> warnTimes = mine.getWarningTimes();
			
			if(!mine.getSilent() && mine.getWarned() && warnTimes.indexOf(next) != -1)
				Message.broadcast(Util.parseVars(PrisonMine.getLanguage().RESET_WARNING, mine));
			
			if(next <= 0) {
				MineCommand.RESET.run(mine.getName());
				next = period;
			} else {
				next -= PrisonMine.getSettings().TICKRATE;
			}
		}
	
		if(mine.getCooldown() && mine.getCooldownEndsIn() > 0)
			mine.updateCooldown(PrisonMine.getSettings().TICKRATE);
	}
	
	/**
	 * Returns the name of the task for debug. Only useful outside of the mine class.
	 * @return Name of the trigger
	 */
	public String getName() { return "PrisonMine:" + this.getName() + ":" + mine.getId(); }
	
	/**
	 * Returns the internal ID of the reset trigger. Useless outside of the mine class
	 * @return ID of the trigger
	 */
	public String getId() { return "time"; }
	
	/**
	 * Checks if the task has expired
	 * @return true if the task has expired, false otherwise.
	 */
	public boolean getExpired() {
		return canceled;
	}
	
	/**
	 * Cancels the current task.
	 */
	public void cancel() { canceled = true; }
	
	public int getPeriod() { return (int)(period / 20); }
	public int getNext() { return (int)(next / 20); }
	
	public void setPeriod(int period) { this.period = period * 20; }
}
