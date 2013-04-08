package com.wolvencraft.prison.mines.util.constants;

import java.util.logging.Level;

import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.variables.BaseVar;
import com.wolvencraft.prison.mines.util.variables.BlockStatVars;
import com.wolvencraft.prison.mines.util.variables.CompositionTriggerVars;
import com.wolvencraft.prison.mines.util.variables.IDVars;
import com.wolvencraft.prison.mines.util.variables.NameVars;
import com.wolvencraft.prison.mines.util.variables.PlayerVar;
import com.wolvencraft.prison.mines.util.variables.TimeTriggerVariables;

public enum MineVariable {
    
    ID (IDVars.class, "ID", null, true),
    IDS (IDVars.class, "IDS", "", false),
    NAME (NameVars.class, "NAME", null, true),
    NAMES (NameVars.class, "NAMES", "", false),
    PLAYER (PlayerVar.class, "PLAYER", null, true),
    TBLOCKS (BlockStatVars.class, "TBLOCKS", "tblocks", true),
    RBLOCKS (BlockStatVars.class, "RBLOCKS", "rblocks", false),
    PBLOCKS (BlockStatVars.class, "PBLOCKS", "pblocks", false),
    PPER (CompositionTriggerVars.class, "PPER", "pper", true),
    NPER (CompositionTriggerVars.class, "NPER", "nper", false),
    PHOUR (TimeTriggerVariables.class, "PHOUR", "phour", true),
    PMIN (TimeTriggerVariables.class, "PMIN", "pmin", false),
    PSEC (TimeTriggerVariables.class, "PSEC", "psec", false),
    PTIME (TimeTriggerVariables.class, "PTIME", "ptime", false),
    NHOUR (TimeTriggerVariables.class, "NHOUR", "nhour", true),
    NMIN (TimeTriggerVariables.class, "NMIN", "nmin", false),
    NSEC (TimeTriggerVariables.class, "NSEC", "nsec", false),
    NTIME (TimeTriggerVariables.class, "NTIME", "ntime", false);
    
    MineVariable(Class<?> clazz, String name, String option, boolean showHelp) {
        try {
            this.object = (BaseVar) clazz.newInstance();
            this.name = name;
            this.option = option;
            this.showHelp = showHelp;
        } catch (InstantiationException e) {
            Message.log(Level.SEVERE, "Error while instantiating a command! InstantiationException");
            return;
        } catch (IllegalAccessException e) {
            Message.log(Level.SEVERE, "Error while instantiating a command! IllegalAccessException");
            return;
        }
    }
    
    private BaseVar object;
    private String name;
    private String option;
    private boolean showHelp;
    
    public String getName() { return name; }
    public String parse(Mine mine) { return object.parse(mine, option); }
    public void getHelp() { if(showHelp) object.getHelp(); }
}
