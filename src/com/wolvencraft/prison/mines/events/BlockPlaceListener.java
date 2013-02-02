package com.wolvencraft.prison.mines.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.BlacklistState;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.Protection;
import com.wolvencraft.prison.mines.util.Message;

public class BlockPlaceListener implements Listener {
	public BlockPlaceListener(PrisonMine plugin) {
		Message.debug("Initiating BlockPlaceListener");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockplace(BlockPlaceEvent event) {
		if(event.isCancelled()) return;
		Message.debug("BlockPlaceEvent caught");
		
		Player player = event.getPlayer();
		
		if(player.hasPermission("prison.mine.bypass.place")) {
			Message.debug("The player has bypass permission");
			return;
		}

		Message.debug("Retrieving the region list...");
		
		Block b = event.getBlock();
		String errorString = PrisonMine.getLanguage().PROTECTION_BREAK;
		errorString.replaceAll("<BLOCK>", b.getType().name().toLowerCase().replace("_", " "));
		
		for(Mine mine : PrisonMine.getLocalMines()) {
			Message.debug("Checking mine " + mine.getId());
			
			if(!mine.getProtectionRegion().isLocationInRegion(b.getLocation())) continue;
			
			Message.debug("Location is in the mine protection region");
			
			if(!player.hasPermission("prison.mine.protection.place." + mine.getId()) && !player.hasPermission("prison.mine.protection.place")) {
				Message.debug("Player " + event.getPlayer().getName() + " does not have permission to place blocks in the mine");
				Message.sendFormattedError(player, errorString);
				event.setCancelled(true);
				continue;
			}
				
			if(!mine.getProtection().contains(Protection.BLOCK_PLACE)) {
				Message.debug("Mine has no block placement protection enabled");
				return;
			}
				
			Message.debug("Mine has a block placement protection enabled");
			if(!mine.getPlaceBlacklist().getState().equals(BlacklistState.DISABLED)) {
				Message.debug("Block placement blacklist detected");
				boolean found = false;
				for(MaterialData block : mine.getPlaceBlacklist().getBlocks()) {
					if(block.getItemType().equals(b.getType())) {
						found = true;
						break;
					}
				}
				
				if((mine.getPlaceBlacklist().getState().equals(BlacklistState.WHITELIST) && !found) || (!mine.getPlaceBlacklist().getState().equals(BlacklistState.BLACKLIST) && found)) {
					Message.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
					Message.sendFormattedError(player, errorString);
					event.setCancelled(true);
					return;
				}
			}
			else {
				Message.debug("No block placement blacklist detected");
				Message.sendFormattedError(player, errorString);
				event.setCancelled(true);
			}
			Message.debug("All checks passed, player is allowed to break blocks");
			return;
		}
		Message.debug("Placed block was not in the mine region");
		return;
	}
}
