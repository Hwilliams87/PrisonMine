package com.wolvencraft.prison.mines.events;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.Protection;
import com.wolvencraft.prison.mines.util.Message;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class PVPListener implements Listener {
	public PVPListener(PrisonMine plugin) {
		Message.debug("Initiating PVPListener");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
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

		if (attacker.hasPermission("protection.bypass")) { return; }
		Player victim = (Player) event.getEntity();

		List<Mine> mines = PrisonMine.getMines();

		for (Mine mine : mines) {
			Message.debug("Checking mine " + mine.getName());
			
			if(!mine.getProtectionRegion().isLocationInRegion(victim.getLocation())) continue;
			
			if (!mine.getProtection().contains(Protection.PVP)) {
				Message.debug(mine + " doesn't have PvP protection on");
				continue;
			}
			
			Message.sendError(attacker, PrisonMine.getLanguage().PROTECTION_PVP);
			event.setCancelled(true);
			return;
		}
	}
}
