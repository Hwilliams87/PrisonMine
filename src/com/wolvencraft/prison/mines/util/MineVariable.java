package com.wolvencraft.prison.mines.util;

import java.util.logging.Level;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.variables.BaseVar;
import com.wolvencraft.prison.mines.util.variables.BlockStatVars;
import com.wolvencraft.prison.mines.util.variables.CompositionTriggerVars;
import com.wolvencraft.prison.mines.util.variables.IDVars;
import com.wolvencraft.prison.mines.util.variables.NameVars;
import com.wolvencraft.prison.mines.util.variables.PlayerVar;
import com.wolvencraft.prison.mines.util.variables.TimeTriggerVariables;

public enum MineVariable {
	
	ID (IDVars.class, "ID", null),
	IDS (IDVars.class, "IDs", ""),
	NAME (NameVars.class, "NAME", null),
	NAMES (NameVars.class, "NAMES", ""),
	PLAYER (PlayerVar.class, "PLAYER", null),
	TBLOCKS (BlockStatVars.class, "TBLOCKS", "tblocks"),
	RBLOCKS (BlockStatVars.class, "RBLOCKS", "rblocks"),
	PBLOCKS (BlockStatVars.class, "PBLOCKS", "pblocks"),
	PPER (CompositionTriggerVars.class, "PPER", "pper"),
	NPER (CompositionTriggerVars.class, "NPER", "nper"),
	PHOUR (TimeTriggerVariables.class, "PHOUR", "phour"),
	PMIN (TimeTriggerVariables.class, "PMIN", "pmin"),
	PSEC (TimeTriggerVariables.class, "PSEC", "psec"),
	PTIME (TimeTriggerVariables.class, "PTIME", "ptime"),
	NHOUR (TimeTriggerVariables.class, "NHOUR", "nhour"),
	NMIN (TimeTriggerVariables.class, "NMIN", "nmin"),
	NSEC (TimeTriggerVariables.class, "NSEC", "nsec"),
	NTIME (TimeTriggerVariables.class, "NTIME", "ntime");
	
	MineVariable(Class<?> clazz, String name, String option) {
		try {
			this.object = (BaseVar) clazz.newInstance();
			this.name = name;
			this.option = option;
		} catch (InstantiationException e) {
			Message.log(Level.SEVERE, "Error while instantiating a command! InstantiationException");
			return;
		} catch (IllegalAccessException e) {
			Message.log(Level.SEVERE, "Error while instantiating a command! IllegalAccessException");
			return;
		}
	}
	
	private BaseVar object;
	private String name;
	private String option;
	
	public String getName() { return name; }
	public String parse(Mine mine) { return object.parse(mine, option); }
}
