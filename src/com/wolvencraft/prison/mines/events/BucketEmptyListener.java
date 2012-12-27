package com.wolvencraft.prison.mines.events;

import java.util.List;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.Protection;
import com.wolvencraft.prison.mines.util.Message;

public class BucketEmptyListener implements Listener {
	public BucketEmptyListener(PrisonMine plugin) {
        Message.debug("Initiating BucketEmptyListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		if(event.isCancelled()) return;
        Message.debug("BucketEmptyEvent caught");
        
        Player player = event.getPlayer();
        
        if(player.hasPermission("prison.mine.bypass.place")) {
            Message.debug("The player has a permission to bypass the protection. Aborting . . .");
			return;
		}

        Message.debug("Retrieving the region list...");
        List<Mine> mines = PrisonMine.getMines();

        for (Mine mine : mines) {
			Message.debug("Checking mine " + mine.getId());
            
        	if(!mine.getProtectionRegion().isLocationInRegion(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation())) continue;
        	
	        if(!player.hasPermission("prison.mine.protection.place." + mine.getId()) && !player.hasPermission("prison.mine.protection.place")) {
	        	Message.debug("Player " + event.getPlayer().getName() + " does not have permission to empty buckets in the mine");
	        	Message.sendError(player, "You are not allowed to empty buckets in this area");
	        	event.setCancelled(true);
	          	return;
	        }
            
            if (!mine.getProtection().contains(Protection.BLOCK_PLACE)) {
                Message.debug("The mine doesn't have placement protection enabled, skipping rest of check...");
                continue;
            }

            Message.debug("Mine has a block placement protection enabled");
			if(mine.getPlaceBlacklist().getEnabled()) {
				Message.debug("Block placement blacklist detected");
				boolean found = false;
				for(MaterialData block : mine.getPlaceBlacklist().getBlocks()) {
					if(block.getItemType().equals(event.getBucket())) {
						found = true;
						break;
					}
				}
				
				if((mine.getPlaceBlacklist().getWhitelist() && !found) || (!mine.getPlaceBlacklist().getWhitelist() && found)) {
					Message.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
					Message.sendError(player, "You are not allowed to empty buckets in the mine");
					event.setCancelled(true);
					return;
				}
			}
			else {
				Message.debug("No block placement blacklist detected");
				Message.sendError(player, "You are not allowed to empty buckets in the mine");
				event.setCancelled(true);
			}
        }
    }
}