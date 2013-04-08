/*
 * SignData.java
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

package com.wolvencraft.prison.mines.settings;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.exceptions.DisplaySignNotFoundException;
import com.wolvencraft.prison.mines.mine.DisplaySign;
import com.wolvencraft.prison.mines.util.Message;

public class SignData {    
    /**
     * Saves all the sign data to disc
     */
    public static void saveAll() {
        for (DisplaySign sign : PrisonMine.getStaticSigns()) sign.saveFile();
    }
    
    /**
     * Loads the sign data from disc
     * @return Loaded list of signs
     */
    public static List<DisplaySign> loadAll() throws DisplaySignNotFoundException {
        List<DisplaySign> signs = new ArrayList<DisplaySign>();
        File signFolder = new File(PrisonMine.getInstance().getDataFolder(), "signs");
        if (!signFolder.exists() || !signFolder.isDirectory()) {
            signFolder.mkdir();
            return signs;
        }
        File[] signFiles;
        if(PrisonMine.getInstance().getVersion() < 1.3) {
            signFiles = signFolder.listFiles(new FileFilter() {
                public boolean accept(File file) { return file.getName().contains(".yml"); }
            });
        } else {
            signFiles = signFolder.listFiles(new FileFilter() {
                public boolean accept(File file) { return file.getName().contains(".psign.yml"); }
            });
        }
        
        for (File signFile : signFiles) {
            try {
                FileConfiguration mineConf = YamlConfiguration.loadConfiguration(signFile);
                Object sign = mineConf.get("displaysign");
                if (sign instanceof DisplaySign) signs.add((DisplaySign) sign);
            } catch (IllegalArgumentException ex) {
                Message.log(Level.SEVERE, ex.getMessage());
                continue;
            }
        }
        return signs;
    }
}
