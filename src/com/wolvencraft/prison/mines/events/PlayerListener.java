package com.wolvencraft.prison.mines.events;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.constants.Protection;

public class PlayerListener implements Listener {
	
	public PlayerListener(PrisonMine plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		Message.debug("| + PlayerListener Initialized");
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

		for (Mine mine : PrisonMine.getStaticMines()) {
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		if(!PrisonMine.getSettings().PLAYERS_TP_ON_RESET) return;
		Location loc = event.getPlayer().getLocation();
		for(Mine mine : PrisonMine.getStaticMines()) {
			if(mine.getRegion().isLocationInRegion(loc)) {
				event.getPlayer().teleport(mine.getTpPoint(), TeleportCause.PLUGIN);
				return;
			}
		}
	}
}
