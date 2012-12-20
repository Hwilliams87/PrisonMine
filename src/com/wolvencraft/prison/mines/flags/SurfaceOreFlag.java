package com.wolvencraft.prison.mines.flags;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("SurfaceOre")
public class SurfaceOreFlag implements BaseFlag {
	
	public String name;
	public Map<String, Object> params;
	
	public SurfaceOreFlag() {
		name = "SurfaceOre";
		params = new HashMap<String, Object>();
	}
	
	public SurfaceOreFlag(Map<String, Object> map) {
		name = "SurfaceOreFlag";
		params = new HashMap<String, Object>();
		params.put("block", map.get("block"));
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("block", params.get("block"));
		return map;
	}

	@Override
	public String getName() { return name; }

	@Override
	public Map<String, Object> getParams() { return params; }

	@Override
	public void setParam(String param, String value) { 
		if(params.containsKey(param)) params.remove(param);
		params.put(param, value);
	}
	
	public void setBlock(String block) { 
		if(params.containsKey("block")) params.remove("block");
		params.put("block", block);
	}
}
