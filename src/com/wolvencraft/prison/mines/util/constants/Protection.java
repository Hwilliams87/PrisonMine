package com.wolvencraft.prison.mines.util.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the three types of protection that can be enabled for a mine. The types are:<br /><br />
 * <b>PVP</b><br />
 * <b>BLOCK_PLACE</b><br />
 * <b>BLOCK_BREAK</b><br />
 * @author bitWolfy
 *
 */
public enum Protection {
    PVP("PVP"),
    BLOCK_PLACE("BLOCK_PLACE"),
    BLOCK_BREAK("BLOCK_BREAK");
    
    private String alias;
    
    Protection(String alias) { this.alias = alias; }
	
    public String getAlias() { return alias; }
    
    /**
     * Returns the enum constant based on its alias
     * @param alias Alias to test
     * @return <b>Protection</b>, or <b>null</b> if there isn't one
     */
    public static Protection get(String alias) {
    	for(Protection prot : values()) {
    		if(prot.getAlias().equalsIgnoreCase(alias)) return prot;
    	}
    	return null;
    }
    
    /**
     * Converts the list of Protection constants to a list of their aliases. This method is used during the serialization of Mine objects
     * @param source List of Protection constants
     * @return List of Strings
     */
	public static List<String> toStringList(List<Protection> source) {
		List<String> list = new ArrayList<String>();
		for(Protection prot : source) {
			list.add(prot.getAlias());
		}
		return list;
	}
	
	/**
	 * Converts the list of String protection aliases back to the list of Protection constants. This method is used during the deserialization of Mine objects.
	 * @param source List of String protection aliases
	 * @return List of Protection constants
	 */
	public static List<Protection> toProtectionList(List<String> source) {
		List<Protection> list = new ArrayList<Protection>();
		for(String string : source) {
			list.add(get(string));
		}
		return list;
	}
}
