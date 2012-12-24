package com.wolvencraft.prison.mines.triggers;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.wolvencraft.prison.PrisonSuite;
import com.wolvencraft.prison.mines.CommandHandler;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
@SerializableAs("CompositionTrigger")
public class CompositionTrigger implements BaseTrigger, ConfigurationSerializable {
	
	double percent;
	String mine;
	boolean canceled;
	
	public CompositionTrigger(Mine mineObj, double percent) {
		this.percent = percent;
		mine = mineObj.getId();
		canceled = false;

		PrisonSuite.addTask(this);
	}
	
	public CompositionTrigger(Map<String, Object> map) {
		percent = Double.parseDouble((String)map.get("percent"));
		mine = (String) map.get("mine");
		canceled = false;

		PrisonSuite.addTask(this);
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("percent", Double.toString(percent));
		map.put("mine", mine);
		return map;
	}
	
	public void run() {
		Mine mineObj = Mine.get(mine);
		if(mineObj == null) return;
		if(((double) mineObj.getBlocksLeft() / (double) mineObj.getTotalBlocks()) < percent) {
			Message.debug("Resetting mine " + mineObj.getName() + " based on composition");
			Message.debug("(" + mineObj.getBlocksLeft() + " / " + mineObj.getTotalBlocks() + ") < " + percent);
			CommandHandler.RESET.run(mineObj.getName());
			mineObj.recountBlocks();
		}
	}

	public void cancel() { canceled = true; }
	
	public boolean getExpired() { return canceled; }
	public String getName() { return "PrisonMine:CompositionTrigger:" + mine; }
	public String getId() { return "composition"; }
	
	public double getPercent() { return percent; }
	public void setPercent(double percent) { this.percent = percent; }
}
