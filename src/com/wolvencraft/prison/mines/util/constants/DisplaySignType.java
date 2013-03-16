package com.wolvencraft.prison.mines.util.constants;

public enum DisplaySignType {
	
	Display("display"),
	Reset("reset"),
	Paid("paid"),
	Output("output");
	
	DisplaySignType(String alias) {
		this.alias = alias;
	}
	
	String alias;
	
	public String getAlias() { return alias; }
	
	public static DisplaySignType get(String alias) {
		for(DisplaySignType signType : DisplaySignType.values()) {
			if(signType.getAlias().equalsIgnoreCase(alias)) return signType;
		}
		return null;
	}
}
