package com.wolvencraft.prison.mines.settings;

import com.wolvencraft.prison.mines.PrisonMine;

public class Settings extends com.wolvencraft.prison.settings.Settings {
	public final boolean TPONRESET;
	
	public Settings(PrisonMine plugin) {
		super(PrisonMine.getPrisonSuite());
		TPONRESET = plugin.getConfig().getBoolean("misc.teleport-on-reset");
	}
}
