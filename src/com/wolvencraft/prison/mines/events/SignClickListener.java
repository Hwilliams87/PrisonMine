package com.wolvencraft.prison.mines.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.DisplaySign;
import com.wolvencraft.prison.mines.util.Message;
public class SignClickListener implements Listener {
	public SignClickListener(PrisonMine plugin) {
        Message.debug("Initiating SignClickListener");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		if(!event.getPlayer().hasPermission("prison.mine.edit")) return;
		
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			
			if(block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
		        Message.debug("SignClickEvent passed");
				
		     	DisplaySign sign = DisplaySign.get(block.getLocation());
				if(sign == null) {
					Message.debug("No registered sign found at this location");
					if(block.getState() instanceof Sign) {
						Message.debug("Checking to see if the sign is valid");
						Sign s = (Sign) block.getState();
						String data = s.getLine(0);
						if(data.startsWith("<M|") && data.endsWith(">")) {
							Message.debug("Registering a new DisplaySign");
							PrisonMine.getSigns().add(new DisplaySign(s));
						}
						return;
					}
				} else {
					sign.initChildren();
				}
			}
			return;
		}
		else return;

	}
}
