package com.wolvencraft.prison.mines.settings;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;

public class MineData
{
	/**
	 * Saves the mine data to disc
	 * @param mines List of mines to save
	 */
	public static void saveAll() {
		List<Mine> mines = PrisonMine.getMines();
		for (Mine mine : mines) {
            File mineFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "mines"), mine.getId() + ".yml");
            FileConfiguration mineConf =  YamlConfiguration.loadConfiguration(mineFile);
            mineConf.set("mine", mine);
            try {
                mineConf.save(mineFile);
            } catch (IOException e) {
            	CommandManager.getPlugin().getLogger().severe("[MineReset] Unable to serialize mine '" + mine.getId() + "'!");
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * Loads the mine data from disc
	 * @param mines List of mines to write the data to
	 * @return Loaded list of mines
	 */
	public static List<Mine> loadAll() {
		List<Mine> mines = new ArrayList<Mine>();
		File mineFolder = new File(CommandManager.getPlugin().getDataFolder(), "mines");
        if (!mineFolder.exists() || !mineFolder.isDirectory()) {
            mineFolder.mkdir();
        }
        File[] mineFiles = mineFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });

        for (File mineFile : mineFiles) {
        	try {
	            FileConfiguration mineConf = YamlConfiguration.loadConfiguration(mineFile);
	            Object mine = mineConf.get("mine");
	            if (mine instanceof Mine) {
	            	mines.add((Mine) mine);
	            }
        	}
        	catch (IllegalArgumentException ex) {
        		Message.log(Level.SEVERE, ex.getMessage());
        	}
        }
        return mines;
	}
	
	/**
	 * Deletes the mine file from the data folder.
	 * <br /><b>Deprecated</b>
	 * @param curMine
	 * @return
	 */
	public static boolean delete(Mine curMine) {
		File mineFolder = new File(CommandManager.getPlugin().getDataFolder(), "mines");
		if(!mineFolder.exists() || !mineFolder.isDirectory()) return false;
		
		File[] mineFiles = mineFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });
		
		for(File mineFile : mineFiles) {
			if(mineFile.getName().equals(curMine.getId() + ".yml")) {
				return mineFile.delete();
			}
		}
		
		return false;
	}
}
