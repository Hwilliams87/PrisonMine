package com.wolvencraft.prison.mines.flags;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("EffectFlag")
public class EffectFlag implements BaseFlag {
	
	public String name = "effect";
	public String param;
	public String[] validValues = {"firework", "smoke", "none"};
	
	public EffectFlag() {
		param = "none";
	}
	
	public EffectFlag(Map<String, Object> map) {
		param = (String) map.get("param");
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", param);
		return null;
	}
	
	public String getName()				{ return name; }
	public Object getParam() 			{ return param; }
	public void setParam(String value) 	{ param = value; }
	public String[] getValidValues()	{ return validValues; }
	public boolean checkValue(String value) {
		for(int i = 0; i < validValues.length; i++) {
			if(validValues[i].equalsIgnoreCase(value)) return true;
		}
		return false;
	}

}
