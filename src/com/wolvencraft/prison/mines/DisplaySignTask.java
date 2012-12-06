package com.wolvencraft.prison.mines;

import com.wolvencraft.prison.hooks.TimedTask;
import com.wolvencraft.prison.mines.mine.DisplaySign;

public class DisplaySignTask implements TimedTask {
	public boolean canceled;
	
	public DisplaySignTask() { canceled = false; }
	
	public void run() { DisplaySign.updateAll(); }

	public String getName() 	{ return "SignTask"; }
	public boolean getExpired() { return canceled; }
	public void cancel()		{ canceled = true; }
}
