package com.wolvencraft.prison.mines.events;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.Protection;
import com.wolvencraft.prison.mines.util.Message;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class PVPListener implements Listener {
	
	public PVPListener(PrisonMine plugin) {
		Message.debug("Initiating PVPListener");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.isCancelled()) return;
		if (!(event.getDamager() instanceof Player || event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) { return; }
		Player attacker;
		if (event.getDamager() instanceof Arrow) {
			if ((((Arrow) event.getDamager()).getShooter() instanceof Player)) {
				attacker = (Player) ((Arrow) event.getDamager()).getShooter();
			} else { return; }
		} else {
			attacker = (Player) event.getDamager();
		}

		if (attacker.hasPermission("prison.mine.bypass.pvp")) { return; }
		Player victim = (Player) event.getEntity();

		for (Mine mine : PrisonMine.getLocalMines()) {
			Message.debug("Checking mine " + mine.getId());
			
			if(!mine.getProtectionRegion().isLocationInRegion(victim.getLocation())) continue;
        	
	        if(!attacker.hasPermission("prison.mine.protection.pvp." + mine.getId()) && !attacker.hasPermission("prison.mine.protection.pvp")) {
	        	Message.debug("Player " + attacker.getName() + " does not have permission to PvP in the mine");
	        	Message.sendFormattedError(attacker, "You are not in a No-PvP zone");
	        	event.setCancelled(true);
	          	return;
	        }
			
			if (!mine.getProtection().contains(Protection.PVP)) {
				Message.debug(mine + " doesn't have PvP protection on");
				continue;
			}
			
			Message.sendFormattedError(attacker, PrisonMine.getLanguage().PROTECTION_PVP);
			event.setCancelled(true);
			return;
		}
	}
}
