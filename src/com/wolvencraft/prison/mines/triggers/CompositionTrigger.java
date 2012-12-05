package com.wolvencraft.prison.mines.triggers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.wolvencraft.prison.mines.MineCommand;
import com.wolvencraft.prison.mines.mine.Mine;

public class CompositionTrigger implements BaseTrigger, ConfigurationSerializable {
	
	double percent;
	Mine mine;
	boolean canceled;
	
	public CompositionTrigger(Mine mine, double percent) {
		this.percent = percent;
		this.mine = mine;
		canceled = false;
	}
	
	public CompositionTrigger(Map<String, Object> map) {
		percent = (Double) map.get("percent");
		mine = Mine.get((String)map.get("mine"));
		canceled = false;
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("percent", percent);
		map.put("mine", mine.getId());
		return map;
	}
	
	public void run() {
		if(((double) mine.getBlocksLeft() / (double) mine.getTotalBlocks()) < percent) {
			MineCommand.RESET.run(mine.getName());
			mine.resetBlocksLeft();
		}
	}
	
	public void cancel() { canceled = true; }
	public boolean getExpired() { return canceled; }
	public String getName() { return "PrisonMine:" + this.getName() + ":" + mine.getId(); }
	public String getId() { return "composition"; }
	
	public double getPercent() { return percent; }
	public void setPercent(double percent) { this.percent = percent; }
}
