/*
 * DisplaySignListener.java
 * 
 * PrisonMine
 * Copyright (C) 2013 bitWolfy <http://www.wolvencraft.com> and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.wolvencraft.prison.mines.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.wolvencraft.prison.hooks.EconomyHook;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.DisplaySign;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.routines.ButtonResetRoutine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.constants.DisplaySignType;

public class DisplaySignListener implements Listener {
    
    public DisplaySignListener(PrisonMine plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        Message.debug("| + DisplaySignListener Initialized");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onButtonPress(PlayerInteractEvent event) {
        if(event.isCancelled()) return;
        
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            
            Block block = event.getClickedBlock();
            
            if(block.getType() == Material.STONE_BUTTON || block.getType() == Material.WOOD_BUTTON) {
                Message.debug("ButtonPressEvent passed");
                
                List<Block> signBlocks = DisplaySign.searchForSign(block.getLocation());
                
                for(Block signBlock : signBlocks) {
                    DisplaySign sign = DisplaySign.get(signBlock.getLocation());
                    if(sign != null && sign.getType().equals(DisplaySignType.Reset)) {
                        Mine curMine = Mine.get(sign.getParentMine());
                        if(curMine == null) return;
                        Player player = event.getPlayer();
                        
                        if(!player.hasPermission("prison.mine.reset.sign." + curMine.getId()) && !player.hasPermission("prison.mine.reset.sign")) {
                            Message.sendFormattedError(player, PrisonMine.getLanguage().ERROR_ACCESS);
                            return;
                        }
                        
                        if(curMine.isCooldownEnabled() && curMine.getCooldownEndsIn() > 0 && !player.hasPermission("prison.mine.bypass.cooldown")) {
                            Message.sendFormattedError(player, PrisonMine.getLanguage().RESET_COOLDOWN, true, curMine);
                            return;
                        }
                        
                        if(EconomyHook.usingVault() && sign.getType().equals(DisplaySignType.Paid) && sign.getPrice() != -1) {
                            Message.debug("Withdrawing " + sign.getPrice() + " from " + player.getName());
                            if(!EconomyHook.withdraw(player, sign.getPrice())) {
                                Message.sendFormattedError(player, PrisonMine.getLanguage().SIGN_FUNDS.replaceAll("<PRICE>", sign.getPrice() + ""));
                                return;
                            }
                            Message.debug("Successfully withdrawn the money. New balance: " + EconomyHook.getBalance(player));
                            Message.sendFormattedSuccess(player, PrisonMine.getLanguage().SIGN_WITHDRAW.replaceAll("<PRICE>", sign.getPrice() + ""));
                        } else Message.debug("Vault not found");
                        
                        ButtonResetRoutine.run(curMine, player);
                        
                        curMine.setLastResetBy(player.getPlayerListName());
                    }
                }
            }
        }
        return;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
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
                        for(String line : s.getLines()) {
                            if(line.startsWith("<M:") && line.endsWith(">")) {
                                Message.debug("Registering a new DisplaySign");
                                PrisonMine.addSign(new DisplaySign(s));
                                return;
                            }
                        }
                    }
                } else {
                    sign.initChildren();
                }
            }
            return;
        }
        else return;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignBreak(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        BlockState b = event.getBlock().getState();
        if(b instanceof Sign) {
            Message.debug("Checking for defined signs...");
            DisplaySign sign = DisplaySign.get((Sign) b);
            if(sign == null) return;

            if(!event.getPlayer().hasPermission("prison.mine.edit")) {
                event.setCancelled(true);
                Message.sendFormattedError(event.getPlayer(), PrisonMine.getLanguage().ERROR_ACCESS, false);
                return;
            }
            
            if(sign.deleteFile()) {
                PrisonMine.removeSign(sign);
                Message.sendFormattedSuccess(event.getPlayer(), "Sign successfully removed", false);
            }
            else {
                Message.sendFormattedError(event.getPlayer(), "Error removing sign!", false);
                event.setCancelled(true);
            }
            return;
        }
        else return;
    }
}
