package com.wolvencraft.prison.mines.util.flags;

import org.bukkit.potion.PotionEffectType;

public class PlayerEffectFlag implements BaseFlag {
    
    String option;
    
    @Override
    public String getName() { return "PlayerEffect"; }

    @Override
    public String getOption() { return option; }

    @Override
    public void setOption(String option) { this.option = option; }

    @Override
    public boolean isOptionValid(String option) { 
        if(PotionEffectType.getByName(option) == null) return false;
        return true;
    }

}