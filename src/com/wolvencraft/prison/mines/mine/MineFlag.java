package com.wolvencraft.prison.mines.mine;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.flags.*;

public enum MineFlag {
	
	NoHungerLoss(NoHungerLossFlag.class, false),
	NoPlayerDamage(NoPlayerDamageFlag.class, false),
	NoToolDamage(NoToolDamageFlag.class, false),
	Silent(SilentFlag.class, false),
	SuperTools(SuperToolsFlag.class, false),
	SurfaceOre(SurfaceOreFlag.class, true),
	ToolReplace(ToolReplaceFlag.class, false);
	
	MineFlag(Class<?> clazz, boolean hasOptions) {
		try {
			object = (BaseFlag) clazz.newInstance();
			this.hasOptions = hasOptions;

		} catch (InstantiationException e) {
			Message.log(Level.SEVERE, "Error while instantiating a command! InstantiationException");
			return;
		} catch (IllegalAccessException e) {
			Message.log(Level.SEVERE, "Error while instantiating a command! IllegalAccessException");
			return;
		}
	}
	
	BaseFlag object;
	boolean hasOptions;
	
	public String getAlias() { return object.getName(); }
	public boolean hasOptions() { return hasOptions; }
	public String getOptions() { if(hasOptions) return object.getOption(); else return null; }
	public void setOptions(String newOption) { if(hasOptions) object.setOption(newOption); else return; }
	
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
		for(MineFlag flag : source) {
			if(flag.hasOptions) list.add(flag.getAlias() + ":" + flag.getOptions());
			else list.add(flag.getAlias());
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
			String[] parts = string.split(":");
			if(parts.length == 1) list.add(get(string));
			else {
				MineFlag flag = get(parts[0]);
				flag.setOptions(parts[1]);
				list.add(flag);
			}
		}
		return list;
	}
}
