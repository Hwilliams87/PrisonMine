package com.wolvencraft.prison.mines.mine;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * @author jjkoletar
 */

@SerializableAs("Protection")
public enum Protection implements ConfigurationSerializable {
    PVP("PVP"),
    BLOCK_PLACE("BLOCK_PLACE"),
    BLOCK_BREAK("BLOCK_BREAK");
    
    private String alias;
    
    Protection(String alias) {
    	this.alias = alias;
    }

	Protection (Map<String, Object> map) {
		alias = (String) map.get("alias");
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("alias", alias);
		return map;
	}
	
}
