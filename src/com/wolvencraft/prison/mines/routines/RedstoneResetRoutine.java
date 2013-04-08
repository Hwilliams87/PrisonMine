package com.wolvencraft.prison.mines.routines;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;
import com.wolvencraft.prison.mines.util.constants.MineFlag;

public class RedstoneResetRoutine {
    public static void run(Mine mine) {
            if(mine.getAutomaticReset() && (mine.getResetsIn() <= 0 || PrisonMine.getSettings().RESET_FORCE_TIMER_UPDATE)) {
            Message.debug("| Resetting the timer (config)");
            mine.resetTimer();
        }
        
        if(!(mine.reset())) {
            Message.debug("| Error while executing the generator! Aborting.");
            Message.debug("+---------------------------------------------");
            return;
        }
        
        if(mine.getCooldown()) mine.resetCooldown();
        
        String broadcastMessage = PrisonMine.getLanguage().RESET_MANUAL;
        broadcastMessage = Util.parseVars(broadcastMessage, mine);
        
        if(!mine.hasFlag(MineFlag.Silent)) {
            Message.broadcast(broadcastMessage);
        }
        
        if(!PrisonMine.getSettings().RESET_TRIGGERS_CHILDREN_RESETS) return;
        
        for(Mine childMine : mine.getChildren()) {
            Message.debug("+---------------------------------------------");
            Message.debug("| Mine " + childMine.getId() + " is resetting. Reset report:");
            Message.debug("| Reset cause: parent mine is resetting (" + mine.getId() + ")");
            run(childMine);
            Message.debug("| Reached the end of the report for " + childMine.getId());
            Message.debug("+---------------------------------------------");
        }
    }
}
