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
import com.wolvencraft.prison.mines.util.constants.DisplaySignType;

public class RedstoneListener implements Listener {
    
	public RedstoneListener(PrisonMine plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		Message.debug("| + RedstoneListener Initialized");
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockRedstone(BlockRedstoneEvent event) {
		if (event.getOldCurrent() != 0 || event.getNewCurrent() == 0) return;
		
		Block triggeringBlock = event.getBlock();
		if(!(triggeringBlock.getType().equals(Material.DIODE_BLOCK_OFF) ||
				triggeringBlock.getType().equals(Material.DIODE_BLOCK_ON) ||
				triggeringBlock.getType().equals(Material.REDSTONE_TORCH_OFF) ||
				triggeringBlock.getType().equals(Material.REDSTONE_TORCH_ON))) return;
		
		final BlockFace adjFaces[] = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN };
		
		for (BlockFace bf : adjFaces) {
			Block adjBlock = triggeringBlock.getRelative(bf);
			if (adjBlock.getType().equals(Material.IRON_BLOCK)) {
				if (adjBlock.isBlockPowered()) break;
				for(DisplaySign sign : PrisonMine.getStaticSigns()) {
					if(!sign.getType().equals(DisplaySignType.Reset) || sign.getAttachedBlock() == null || !sign.getAttachedBlock().getLocation().equals(adjBlock.getLocation())) continue;
					Mine curMine = Mine.get(sign.getParent());
					if(curMine == null) return;
					RedstoneResetRoutine.run(curMine);
				}
				
			}
		}
	}
}
