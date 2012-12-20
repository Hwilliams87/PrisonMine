package com.wolvencraft.prison.mines.flags;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("BaseFlag")
public interface BaseFlag extends ConfigurationSerializable {
	public String getName();
	public Map<String, Object> getParams();
	public void setParam(String param, String value);
}
