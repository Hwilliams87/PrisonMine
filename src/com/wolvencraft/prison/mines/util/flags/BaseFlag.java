package com.wolvencraft.prison.mines.util.flags;

public interface BaseFlag {
    
    public String getName();
    public String getOption();
    public void setOption(String option);
    public boolean isOptionValid(String option);
    
}
