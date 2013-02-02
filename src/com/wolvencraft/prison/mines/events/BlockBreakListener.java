package com.wolvencraft.prison.mines.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.MaterialData;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.BlacklistState;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.Protection;
import com.wolvencraft.prison.mines.util.Message;

public class BlockBreakListener implements Listener {
	public BlockBreakListener(PrisonMine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockbreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		Message.debug("BlockBreakEvent caught");
		
		Player player = event.getPlayer();
		
		Block b = event.getBlock();
		String errorString = PrisonMine.getLanguage().PROTECTION_BREAK;
		errorString.replaceAll("<BLOCK>", b.getType().name().toLowerCase().replace("_", " "));
		
		for(Mine mine : PrisonMine.getLocalMines()) {
			Message.debug("Checking mine " + mine.getId());
			
			if(!mine.getProtectionRegion().isLocationInRegion(b.getLocation())) continue;
			
			Message.debug("Location is in the mine protection region");
			
			if(!player.hasPermission("prison.mine.protection.break." + mine.getId()) && !player.hasPermission("prison.mine.protection.break") && !player.hasPermission("prison.mine.bypass.break")) {
				Message.debug("Player " + event.getPlayer().getName() + " does not have permission to break blocks in the mine");
				Message.sendFormattedError(player, errorString);
				event.setCancelled(true);
				return;
			}
				
			if(!mine.getProtection().contains(Protection.BLOCK_BREAK)) {
				Message.debug("Mine has no block breaking protection enabled");
				return;
			}
				
			Message.debug("Mine has a block breaking protection enabled");
			if(!mine.getBreakBlacklist().getState().equals(BlacklistState.DISABLED)) {
				Message.debug("Block breaking blacklist detected");
				boolean found = false;
				for(MaterialData block : mine.getBreakBlacklist().getBlocks()) {
					if(block.getItemType().equals(b.getType())) {
						found = true;
						break;
					}
				}
				
				if((mine.getBreakBlacklist().getState().equals(BlacklistState.WHITELIST) && !found) || (mine.getBreakBlacklist().getState().equals(BlacklistState.BLACKLIST) && found)) {
					Message.debug("Player " + player.getName() + " broke a black/whitelisted block in the mine!");
					Message.sendFormattedError(player, errorString);
					event.setCancelled(true);
					return;
				}
			}
			else {
				Message.debug("No block breaking blacklist detected");
				Message.sendFormattedError(player, errorString);
				event.setCancelled(true);
			}
			Message.debug("All checks passed, player is allowed to break blocks");
			return;
		}
		Message.debug("Broken block was not in the mine region");
		return;
	}
}
