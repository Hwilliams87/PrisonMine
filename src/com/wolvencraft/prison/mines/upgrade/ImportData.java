package com.wolvencraft.prison.mines.upgrade;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.mine.Mine;
import com.wolvencraft.prison.mines.util.Message;


/**
 * Loads data from MineReset and MineResetLite
 * @author bitWolfy
 *
 */
public class ImportData {
	
	/**
	 * Loads the mine data from disc
	 * @param mines List of mines to write the data to
	 * @return Loaded list of mines
	 */
	public static List<Mine> loadAll() {
		List<Mine> mines = new ArrayList<Mine>();
		
		File importFolder = new File(CommandManager.getPlugin().getDataFolder(), "import");
        if (!importFolder.exists() || !importFolder.isDirectory()) { return null; }
        File[] mrFiles = importFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".yml");
            }
        });

        for (File mineFile : mrFiles) {
        	try {
	            FileConfiguration mineConf = YamlConfiguration.loadConfiguration(mineFile);
	            Object mine = mineConf.get("mine");
	            if (mine instanceof MRMine) { mines.add(((MRMine) mine).importMine()); }
	            else if(mine instanceof MRLMine) { mines.add(((MRLMine) mine).importMine()); }
	            else Message.log(Level.WARNING, "Unknown file in the import directory: " + mineFile.getName());
        	}
        	catch (IllegalArgumentException ex) { Message.log(Level.SEVERE, ex.getMessage()); }
        }
        return mines;
	}
}
