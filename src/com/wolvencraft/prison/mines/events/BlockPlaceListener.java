package com.wolvencraft.prison.mines.events;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.Protection;
import com.wolvencraft.prison.mines.util.Message;

public class BlockPlaceListener implements Listener
{
	public BlockPlaceListener(PrisonMine plugin) {
		Message.debug("Initiating BlockPlaceListener");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onBlockplace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		Message.debug("BlockPlaceEvent caught");
		
		Player player = event.getPlayer();
		
		if(player.hasPermission("protection.bypass.place")) {
			Message.debug("The player has bypass permission");
			return;
		}

		Message.debug("Retrieving the region list...");
		List<Mine> mines = PrisonMine.getMines();
		
		Block b = event.getBlock();
		String errorString = PrisonMine.getLanguage().PROTECTION_BREAK;
		errorString.replaceAll("<BLOCK>", b.getType().name().toLowerCase().replace("_", " "));
		
		for(Mine mine : mines) {
			Message.debug("Checking mine " + mine.getName());
			
			if(!mine.getProtectionRegion().isLocationInRegion(b.getLocation())) continue;
			
			Message.debug("Location is in the mine protection region");
			
			if(!player.hasPermission("protection.place." + mine.getName()) && !player.hasPermission("protection.place")) {
				Message.debug("Player " + event.getPlayer().getName() + " does not have permission to place blocks in the mine");
				Message.sendError(player, errorString);
				event.setCancelled(true);
				continue;
			}
				
			if(!mine.getProtection().contains(Protection.BLOCK_PLACE)) {
				Message.debug("Mine has no block placement protection enabled");
				continue;
			}
				
			Message.debug("Mine has a block placement protection enabled");
			if(mine.getPlaceBlacklist().getEnabled()) {
				Message.debug("Block placement blacklist detected");
				boolean found = false;
				for(MaterialData block : mine.getPlaceBlacklist().getBlocks()) {
					if(block.getItemType().equals(b.getType())) {
						found = true;
						break;
					}
				}
				
				if((mine.getPlaceBlacklist().getWhitelist() && !found) || (!mine.getPlaceBlacklist().getWhitelist() && found)) {
					Message.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
					Message.sendError(player, errorString);
					event.setCancelled(true);
					return;
				}
			}
			else {
				Message.debug("No block placement blacklist detected");
				Message.sendError(player, errorString);
				event.setCancelled(true);
			}
		}
		Message.debug("Placed block was not in the mine region");
		return;
	}
}
