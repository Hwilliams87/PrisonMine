package com.wolvencraft.prison.mines.triggers;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.util.Util;

public class Failsafe {
	private long start;
	private long end;
	
	public Failsafe() {
		start = Util.getCurrentTime().getTime();
		end = 0;
	}
	
	public void tick() {
		start = Util.getCurrentTime().getTime();
	}
	
	public boolean check() {
		end = Util.getCurrentTime().getTime();
		if((end - start) > PrisonMine.getSettings().FAILSAFETIME) return true;
		return false;
	}
}
