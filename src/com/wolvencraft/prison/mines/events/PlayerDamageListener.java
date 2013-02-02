package com.wolvencraft.prison.mines.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.MineFlag;
import com.wolvencraft.prison.mines.util.Message;

public class PlayerDamageListener implements Listener {

	public PlayerDamageListener(PrisonMine plugin) {
		Message.debug("Initiating PlayerDamageListener");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDamage(EntityDamageEvent event) {
		if(event.isCancelled()) return;
		if(!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		for(Mine mine : PrisonMine.getLocalMines()) {
			if(!mine.getRegion().isLocationInRegion(player.getLocation())) continue;
			
			if(!mine.getFlags().contains(MineFlag.NoPlayerDamage)) continue;
			
			event.setCancelled(true);
		}
	}
}
