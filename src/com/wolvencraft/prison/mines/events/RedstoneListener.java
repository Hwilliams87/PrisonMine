package com.wolvencraft.prison.mines.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.DisplaySign;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.routines.RedstoneResetRoutine;
import com.wolvencraft.prison.mines.util.Message;

public class RedstoneListener implements Listener {
    
	public RedstoneListener(PrisonMine plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		Message.debug("| + RedstoneListener Initialized");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockRedstone(BlockRedstoneEvent e) {
		Block triggeringBlock = e.getBlock();
		if(!(triggeringBlock.getType().equals(Material.STONE_BUTTON) || 
				triggeringBlock.getType().equals(Material.WOOD_BUTTON) || 
				triggeringBlock.getType().equals(Material.STONE_PLATE) || 
				triggeringBlock.getType().equals(Material.WOOD_PLATE) || 
				triggeringBlock.getType().equals(Material.REDSTONE_WIRE) || 
				triggeringBlock.getType().equals(Material.DIODE) ||
				triggeringBlock.getType().equals(Material.DIODE_BLOCK_ON) ||
				triggeringBlock.getType().equals(Material.REDSTONE_TORCH_ON) ||
				triggeringBlock.getType().equals(Material.LEVER))) return;
		
		if (e.getOldCurrent() != 0 || e.getNewCurrent() == 0) return;
		
		final BlockFace adjFaces[] = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN };
		
		for (BlockFace bf : adjFaces) {
			Block adjBlock = triggeringBlock.getRelative(bf);
			if (adjBlock.getType() == Material.WALL_SIGN) {
				if (adjBlock.isBlockPowered()) break;
				
				DisplaySign sign = DisplaySign.get(adjBlock.getLocation());
				if(sign != null && sign.getReset()) {
					Mine curMine = Mine.get(sign.getParent());
					if(curMine == null) return;
					
					RedstoneResetRoutine.run(curMine);
				}
				
			}
		}
	}
}
