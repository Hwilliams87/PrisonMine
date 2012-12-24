package com.wolvencraft.prison.mines.mine;

import java.util.ArrayList;
import java.util.List;

public enum Protection {
    PVP("PVP"),
    BLOCK_PLACE("BLOCK_PLACE"),
    BLOCK_BREAK("BLOCK_BREAK");
    
    private String alias;
    
    Protection(String alias) { this.alias = alias; }
	
    public String getAlias() { return alias; }
    
    public static Protection get(String alias) {
    	for(Protection prot : values()) {
    		if(prot.getAlias().equalsIgnoreCase(alias)) return prot;
    	}
    	return null;
    }
    
	public static List<String> toStringList(List<Protection> source) {
		List<String> list = new ArrayList<String>();
		for(Protection prot : source) {
			list.add(prot.getAlias());
		}
		return list;
	}
	
	public static List<Protection> toProtectionList(List<String> source) {
		List<Protection> list = new ArrayList<Protection>();
		for(String string : source) {
			list.add(get(string));
		}
		return list;
	}
}
