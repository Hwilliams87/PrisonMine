package com.wolvencraft.prison.mines.triggers;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.wolvencraft.prison.hooks.TimedTask;

@SerializableAs("BaseTrigger")
public interface BaseTrigger extends TimedTask, ConfigurationSerializable {
	public String getId();
	
}
