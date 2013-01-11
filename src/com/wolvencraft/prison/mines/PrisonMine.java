package com.wolvencraft.prison.mines;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.wolvencraft.prison.PrisonSuite;
import com.wolvencraft.prison.hooks.PrisonPlugin;
import com.wolvencraft.prison.mines.events.*;
import com.wolvencraft.prison.mines.flags.*;
import com.wolvencraft.prison.mines.generation.BaseGenerator;
import com.wolvencraft.prison.mines.mine.*;
import com.wolvencraft.prison.mines.settings.*;
import com.wolvencraft.prison.mines.triggers.*;
import com.wolvencraft.prison.mines.upgrade.MRLMine;
import com.wolvencraft.prison.mines.upgrade.MRMine;
import com.wolvencraft.prison.mines.util.BlockSerializable;
import com.wolvencraft.prison.mines.util.ExtensionLoader;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.region.PrisonRegion;

public class PrisonMine extends PrisonPlugin {
	private static PrisonSuite prisonSuite;
	
	private static Settings settings;
	private FileConfiguration languageData = null;
	private File languageDataFile = null;
	private static Language language;
	
	private CommandManager commandManager;
	
	private static List<Mine> mines;
	private static List<DisplaySign> signs;
	private static List<BaseGenerator> generators;

	private static Map<CommandSender, Mine> curMines;
	
	public void onEnable() {
		prisonSuite = PrisonSuite.addPlugin(this);

		getConfig().options().copyDefaults(true);
		saveConfig();
		settings = new Settings(this);
		Message.debug("1. Established connection with PrisonCore");
		
		getLanguageData().options().copyDefaults(true);
		saveLanguageData();
		language = new Language(this);
		Message.debug("2. Loaded plugin configuration");

		commandManager = new CommandManager(this);
		getCommand("mine").setExecutor(commandManager);
		Message.debug("3. Started up the CommandManager");
		
		ConfigurationSerialization.registerClass(Mine.class, "pMine");
		ConfigurationSerialization.registerClass(MineBlock.class, "MineBlock");
		ConfigurationSerialization.registerClass(Blacklist.class, "Blacklist");
		ConfigurationSerialization.registerClass(DisplaySign.class, "DisplaySign");
		ConfigurationSerialization.registerClass(DataBlock.class, "DataBlock");
		ConfigurationSerialization.registerClass(SimpleLoc.class, "SimpleLoc");
		ConfigurationSerialization.registerClass(PrisonRegion.class, "PrisonRegion");
		ConfigurationSerialization.registerClass(BaseTrigger.class, "BaseTrigger");
		ConfigurationSerialization.registerClass(TimeTrigger.class, "TimeTrigger");
		ConfigurationSerialization.registerClass(CompositionTrigger.class, "CompositionTrigger");
		ConfigurationSerialization.registerClass(BaseFlag.class, "BaseFlag");
		ConfigurationSerialization.registerClass(SurfaceOreFlag.class, "SurfaceOreFlag");
		ConfigurationSerialization.registerClass(EffectFlag.class, "EffectFlag");
		ConfigurationSerialization.registerClass(BlockSerializable.class, "BlockSerializable");
		
		ConfigurationSerialization.registerClass(MRMine.class, "MRMine");
		ConfigurationSerialization.registerClass(MRLMine.class, "MRLMine");
		Message.debug("4. Registered serializable classes");
		
		mines = MineData.loadAll();
		signs = SignData.loadAll();
		generators = ExtensionLoader.loadAll();
		
		curMines = new HashMap<CommandSender, Mine>();
		
		Message.debug("5. Loaded data from file");
		
		new BlockBreakListener(this);
		new BlockPlaceListener(this);
		new BucketEmptyListener(this);
		new BucketFillListener(this);
		new SignClickListener(this);
		new SignBreakListener(this);
		new PVPListener(this);
		new ButtonPressListener(this);
		Message.debug("6. Started up event listeners");
		
		Message.log("PrisonMine started [ " + mines.size() + " mine(s) found ]");
		
		Message.debug("7. Sending sign task to PrisonCore");
		PrisonSuite.addTask(new DisplaySignTask());
	}
	
	
	public void onDisable() {
		MineData.saveAll();
		SignData.saveAll();
		
		getServer().getScheduler().cancelTasks(this);
		Message.log("Plugin stopped");
	}
	
	public void reloadLanguageData() {
		String lang = settings.LANGUAGE;
		if(lang == null) lang = "english";
		lang = lang + ".yml";
		Message.log("Language file used: " + lang);
		
		if (languageDataFile == null) languageDataFile = new File(getDataFolder(), lang);
		languageData = YamlConfiguration.loadConfiguration(languageDataFile);
		
		InputStream defConfigStream = getResource(lang);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			languageData.setDefaults(defConfig);
		}
	}
	
	public FileConfiguration getLanguageData() {
		if (languageData == null) reloadLanguageData();
		return languageData;
	}

	public void saveLanguageData() {
		if (languageData == null || languageDataFile == null) return;
		try { languageData.save(languageDataFile); }
		catch (IOException ex) { Message.log("Could not save config to " + languageDataFile); }
	}
	
	public static List<BaseGenerator> getGenerators() 	{ return generators; }
	public static Settings getSettings()				{ return settings; }
	public static Language getLanguage()				{ return language; }
	public static PrisonSuite getPrisonSuite() 			{ return prisonSuite; }
	public double getVersion()							{ return Double.parseDouble(this.getDescription().getVersion()); }
	public static Mine getCurMine(CommandSender sender) { return curMines.get(sender); }
	public static Mine getCurMine() 					{ return getCurMine(CommandManager.getSender()); }
	public static void setCurMine(Mine mine) 			{ setCurMine(CommandManager.getSender(), mine); }
	public void reloadSettings()						{ settings = null; settings = new Settings(this); }
	public void reloadLanguage()						{ language = null; language = new Language(this); }
	
	public static List<Mine> getLocalMines() { 
		List<Mine> temp = new ArrayList<Mine>();
		for(Mine mine : mines) temp.add(mine);
		return temp;
	}
	
	public static void setMines(List<Mine> newMines) {
		mines.clear();
		for(Mine mine : newMines) mines.add(mine);
	}
	
	public static void addMine(Mine mine) 				{ mines.add(mine); }
	public static void addMine(List<Mine> newMines) 	{ for(Mine mine : newMines) mines.add(mine); }
	public static void removeMine (Mine mine) 			{ mines.remove(mine); }
	
	public static List<DisplaySign> getLocalSigns() { 
		List<DisplaySign> temp = new ArrayList<DisplaySign>();
		for(DisplaySign sign : signs) temp.add(sign);
		return temp;
	}
	
	public static void setSigns(List<DisplaySign> newSigns) {
		signs.clear();
		for(DisplaySign sign : newSigns) signs.add(sign);
	}
	
	public static void setCurMine(CommandSender sender, Mine mine) {
		if(curMines.get(sender) != null) curMines.remove(sender);
		if(mine != null) curMines.put(sender, mine);
	}
	
	public static void addSign(DisplaySign sign) 				{ signs.add(sign); }
	public static void addSign(List<DisplaySign> newSigns) 		{ for(DisplaySign sign : newSigns) signs.add(sign); }
	public static void removeSign (DisplaySign sign) 			{ signs.remove(sign); }
}