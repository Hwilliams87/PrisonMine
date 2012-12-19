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
import com.wolvencraft.prison.mines.mine.DisplaySign;
import com.wolvencraft.prison.mines.util.Message;

public class SignData {	
	/**
	 * Saves the mine data to disc
	 * @param mines List of mines to save
	 */
	public static void saveAll() {
		List<DisplaySign> signs = PrisonMine.getSigns();
		for (DisplaySign sign : signs) {
			File signFile = new File(new File(CommandManager.getPlugin().getDataFolder(), "signs"), sign.getId() + ".psign.yml");
	        FileConfiguration signConf =  YamlConfiguration.loadConfiguration(signFile);
	        signConf.set("displaysign", sign);
	        try {
	            signConf.save(signFile);
	        } catch (IOException e) {
	        	Message.log(Level.SEVERE, "Unable to serialize sign '" + sign.getId() + "'!");
	            e.printStackTrace();
	        }
        }
	}
	
	/**
	 * Loads the mine data from disc
	 * @param mines List of mines to write the data to
	 * @return Loaded list of mines
	 */
	public static List<DisplaySign> loadAll() {
		List<DisplaySign> signs = new ArrayList<DisplaySign>();
		File signFolder = new File(CommandManager.getPlugin().getDataFolder(), "signs");
        if (!signFolder.exists() || !signFolder.isDirectory()) {
            signFolder.mkdir();
        }
        File[] signFiles = signFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().contains(".psign.yml");
            }
        });
        
        signs.clear();
        for (File signFile : signFiles) {
            FileConfiguration mineConf = YamlConfiguration.loadConfiguration(signFile);
            Object sign = mineConf.get("displaysign");
            if (sign instanceof DisplaySign) {
                signs.add((DisplaySign) sign);
            }
        }
        return signs;
	}
}
