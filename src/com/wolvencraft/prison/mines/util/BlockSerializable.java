package com.wolvencraft.prison.mines.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;

@SerializableAs("BlockSerializable")
public class BlockSerializable implements ConfigurationSerializable {
	int blockId;
	byte data;
	
	public BlockSerializable(MaterialData block) {
		blockId = block.getItemTypeId();
		data = block.getData();
	}
	
	public BlockSerializable(Map<String, Object> map) {
		blockId = ((Integer) map.get("blockId")).intValue();
		data = ((Integer) map.get("data")).byteValue();
	}
	
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("blockId", blockId);
		map.put("data", data);
		return map;
	}
	
	public MaterialData toMaterialData() {
		MaterialData block = new MaterialData(blockId);
		block.setData(data);
		return block;
	}
}
