package com.wolvencraft.prison.mines.util.constants;

public enum BlacklistState {
	DISABLED (0),
	WHITELIST (1),
	BLACKLIST (2);
	
	BlacklistState(int id) {
		this.id = id;
	}
	
	private int id;
	
	public int getId() { return id; }
	
	public static BlacklistState fromId(int id) {
		for(BlacklistState type : BlacklistState.values()) {
			if(type.getId() == id) return type;
		}
		return null;
	}
	
}
