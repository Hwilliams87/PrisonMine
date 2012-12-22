package com.wolvencraft.prison.mines.flags;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("BaseFlag")
public interface BaseFlag extends ConfigurationSerializable {
	public String getName();
	public Object getParam();
	public void setParam(String value);
	public String[] getValidValues();
	public boolean checkValue(String value);
}
