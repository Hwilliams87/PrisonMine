package com.wolvencraft.prison.mines.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.mine.MineFlag;
import com.wolvencraft.prison.mines.util.Message;

public class ToolListener implements Listener {
	
	public ToolListener(PrisonMine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		
		Block b = event.getBlock();
		
		for(Mine mine : PrisonMine.getLocalMines()) {
			if(!mine.getRegion().isLocationInRegion(b.getLocation())) continue;
			
			if(mine.hasFlag(MineFlag.NoToolDamage)) {
				Player player = event.getPlayer();
				if(!player.hasPermission("prison.mine.flags.notooldamage." + mine.getId()) && !player.hasPermission("prison.mine.flags.notooldamage")) { continue; }

				Message.debug("NoToolDamage flag is in effect");
				ItemStack tool = player.getInventory().getItemInHand();
				player.getInventory().remove(tool);
				for(Material mat : PrisonMine.getSettings().TOOLS) {
					if(mat.equals(tool.getType())) {
						short durability = tool.getDurability();
						if(durability != 0) {
							Message.debug("Old durability: " + durability);
							tool.setDurability((short)(durability - 1));
							Message.debug("new durability: " + tool.getDurability());
						} else tool.setDurability((short) 0);
						break;
					}
				}
				player.setItemInHand(tool);
				player.updateInventory();
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockDamage(BlockDamageEvent event) {
		if(event.isCancelled()) return;
		
		Block b = event.getBlock();
		
		for(Mine mine : PrisonMine.getLocalMines()) {
			if(!mine.getRegion().isLocationInRegion(b.getLocation())) continue;
			
			if(mine.hasFlag(MineFlag.SuperTools)) {
				Player player = event.getPlayer();
				if(!player.hasPermission("prison.mine.flags.supertools." + mine.getId()) && !player.hasPermission("prison.mine.flags.supertools")) { continue; }
				Message.debug("SuperTools flag is in effect");
				b.breakNaturally(player.getItemInHand());
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onToolBreak(PlayerItemBreakEvent event) {
		

		for(Mine mine : PrisonMine.getLocalMines()) {
			if(!mine.getRegion().isLocationInRegion(event.getPlayer().getLocation())) continue;
			
			Player player = event.getPlayer();
			if(!player.hasPermission("prison.mine.flags.toolreplace." + mine.getId()) && !player.hasPermission("prison.mine.flags.toolreplace")) { continue; }
			
			ItemStack brokenItem = event.getBrokenItem();
			player.getInventory().addItem(new ItemStack(brokenItem.getType()));
			return;
		}
	}
}
