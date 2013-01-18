package com.wolvencraft.prison.mines.mine;

import java.util.ArrayList;
import java.util.List;

public enum MineFlag {

	NoToolDamage("notooldamage"),
	Silent("silent"),
	SuperTools("supertools"),
	SurfaceOre("surfaceore");
	
	MineFlag(String alias) {
		this.alias = alias;
	}
	
	String alias;
	
	public String getAlias() { return alias; }
	
	public static MineFlag get(String alias) {
		for(MineFlag flag : MineFlag.values()) {
			if(flag.getAlias().equalsIgnoreCase(alias)) return flag;
		}
		return null;
	}
	
    /**
     * Converts the list of Protection constants to a list of their aliases. This method is used during the serialization of Mine objects
     * @param source List of Protection constants
     * @return List of Strings
     */
	public static List<String> toStringList(List<MineFlag> source) {
		List<String> list = new ArrayList<String>();
		for(MineFlag prot : source) {
			list.add(prot.getAlias());
		}
		return list;
	}
	
	/**
	 * Converts the list of String protection aliases back to the list of Protection constants. This method is used during the deserialization of Mine objects.
	 * @param source List of String protection aliases
	 * @return List of Protection constants
	 */
	public static List<MineFlag> toMineFlagList(List<String> source) {
		List<MineFlag> list = new ArrayList<MineFlag>();
		for(String string : source) {
			list.add(get(string));
		}
		return list;
	}
}
