package com.wolvencraft.prison.mines.triggers;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.wolvencraft.prison.hooks.TimedTask;
import com.wolvencraft.prison.mines.util.constants.ResetTrigger;

@SerializableAs("BaseTrigger")
public interface BaseTrigger extends TimedTask, ConfigurationSerializable {
    public ResetTrigger getId();
    
}
