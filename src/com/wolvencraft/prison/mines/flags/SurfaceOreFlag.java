package com.wolvencraft.prison.mines.flags;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.util.BlockSerializable;
import com.wolvencraft.prison.mines.util.Util;

@SerializableAs("SurfaceOreFlag")
public class SurfaceOreFlag implements BaseFlag {
	
	public String name = "surfaceore";
	public MaterialData param;
	public String[] validValues = {"<ID:DATA>", "none"};
	
	public SurfaceOreFlag() {
		param = new MaterialData(Material.AIR);
	}
	
	public SurfaceOreFlag(Map<String, Object> map) {
		param = ((BlockSerializable) map.get(param)).toMaterialData();
	}

	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", new BlockSerializable(param));
		return map;
	}
	
	public String getName()				{ return name; }
	public Object getParam() 			{ return param; }
	public void setParam(String value) 	{ param = Util.getBlock(value); }
	public String[] getValidValues()	{ return validValues; }
	public boolean checkValue(String value) {
		if(value.split(":").length == 2 || value.equalsIgnoreCase("none")) return true;
		return false;
	}
}
