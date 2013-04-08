package com.wolvencraft.prison.mines.util.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.util.constants.BlacklistState;
import com.wolvencraft.prison.util.Message;

@SerializableAs("Blacklist")
public class Blacklist implements ConfigurationSerializable {
    private BlacklistState type;
    private List<MaterialData> blocks;
    
    public Blacklist() {
        type = BlacklistState.DISABLED;
        blocks = new ArrayList<MaterialData>();
    }
    
    @SuppressWarnings("unchecked")
    public Blacklist(Map<String, Object> me) {
        type = BlacklistState.DISABLED;
        if(me.containsKey("enabled") && ((Boolean) me.get("enabled"))) type = BlacklistState.BLACKLIST;
        if(me.containsKey("whitelist") && ((Boolean) me.get("whitelist"))) type = BlacklistState.WHITELIST;
        if(me.containsKey("type")) type = BlacklistState.fromId((Integer) me.get("type"));
        
        Map<Integer, Byte> materials = (Map<Integer, Byte>) me.get("blocks");
        blocks = new ArrayList<MaterialData>();
        for(Map.Entry<Integer, Byte> entry : materials.entrySet()) {
            try {
                blocks.add(new MaterialData(Material.getMaterial(entry.getKey().intValue()), entry.getValue().byteValue()));
            } catch (ClassCastException cce) {
                blocks.add(new MaterialData(Material.getMaterial(entry.getKey().intValue())));
            }
        }
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("type", type.getId());
        Map<Integer, Byte> materials = new HashMap<Integer, Byte>();
        for(MaterialData block : blocks) {
            materials.put(block.getItemTypeId(), block.getData());
        }
        me.put("blocks", materials);
        return me;
    }
    
    public BlacklistState getState()     { return type; }
    
    public List<MaterialData> getBlocks() { 
        List<MaterialData> tempBlocks = new ArrayList<MaterialData>();
        for(MaterialData block : blocks) tempBlocks.add(block);
        return tempBlocks;
    }
    
    public void setState(BlacklistState type) { this.type = type; }
    
    public void setBlocks(List<MaterialData> newBlocks) {
        blocks.clear();
        for(MaterialData block : newBlocks) {
            blocks.add(block);
            Message.debug("Block added to the blacklist = " + block.getItemType());
        }
    }
}
