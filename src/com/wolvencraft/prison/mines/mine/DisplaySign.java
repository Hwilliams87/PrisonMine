package com.wolvencraft.prison.mines.mine;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.exceptions.DisplaySignNotFoundException;
import com.wolvencraft.prison.mines.util.Message;
import com.wolvencraft.prison.mines.util.Util;

/**
 * A virtual representation of the dynamically-updated signs that are used to display information about mines
 * @author bitWolfy
 *
 */
@SerializableAs("DisplaySign")
public class DisplaySign implements ConfigurationSerializable  {
	private String signId;
	private String mineId;
	private Sign sign;
	private boolean reset;
	private boolean paid;
	
	private List<String> originalText;
	
	private double price;
	
	/**
	 * Standard constructor for new signs
	 * @param sign Sign object
	 */
	public DisplaySign(Sign sign) {
		this.signId = generateId();
		this.sign = sign;
		
		originalText = new ArrayList<String>();
		for(String line : sign.getLines()) { originalText.add(line); }
		
        parseFirstLine(originalText.get(0).substring(3, originalText.get(0).length() - 1));
		
		saveFile();
	}
	
	/**
	 * Constructor for mines that inherit information from their parents
	 * @param sign Sign object
	 * @param parentSignClass DisplaySign parent
	 */
	public DisplaySign(Sign sign, DisplaySign parentSignClass) {
		signId = generateId();
		this.sign = sign;
		originalText = new ArrayList<String>();
		for(String line : sign.getLines()) { originalText.add(line); }
		
		mineId = parentSignClass.getParent();
		paid = false;
		reset = false;
		price = -1;
		
		saveFile();
	}
	
    /**
     * Constructor for deserialization from a map
     * @param map Map to deserialize from
     */
	@SuppressWarnings("unchecked")
	public DisplaySign(Map<String, Object> me) throws DisplaySignNotFoundException {
		signId = (String) me.get("id");
        mineId = (String) me.get("parent");
        
        World world = Bukkit.getWorld((String) me.get("world"));
        Block signBlock = world.getBlockAt(((Vector) me.get("loc")).toLocation(world));
        if(!(signBlock.getState() instanceof Sign)) throw new DisplaySignNotFoundException("No sign found at the stored location");
        sign = (Sign) signBlock.getState();
        
        originalText = (List<String>) me.get("lines");
        
        parseFirstLine(originalText.get(0).substring(3, originalText.get(0).length() - 1));
	}
	
	/**
	 * Serialization method for sign data storage
	 * @return Serialization map
	 */
    public Map<String, Object> serialize() {
        Map<String, Object> me = new HashMap<String, Object>();
        me.put("id", signId);
        me.put("loc", sign.getLocation().toVector());
        me.put("world", sign.getLocation().getWorld().getName());
        me.put("parent", mineId);
        me.put("reset", reset);
        me.put("paid", paid);
        me.put("lines", originalText);
        me.put("price", price);
        return me;
    }
	
	private void parseFirstLine(String firstLine) {
		String[] data = firstLine.split(":");
		
		if(data.length == 1) {
			mineId = data[0];
			reset = false;
			paid = false;
			price = -1;
		} else if(data.length == 2) {
			mineId = data[0];
			reset = false;
			paid = false;
			price = -1;
			if(data[1].equalsIgnoreCase("R")) reset = true;
			else {
				reset = true;
				paid = true;
				price = Double.parseDouble(data[1]);
			}
		} else {
			mineId = data[0];
			reset = false;
			paid = false;
			price = -1;
		}
	}

    public String getId() 			{ return signId; }
    public Location getLocation() 	{ return sign.getLocation(); }
    @Deprecated
    public Block getBaseBlock()		{
    	org.bukkit.material.Sign signMat = (org.bukkit.material.Sign) sign.getBlock().getState();
    	return sign.getBlock().getRelative(signMat.getAttachedFace());
    }
    
    public Block getAttachedBlock() {
    	Block signBlock = sign.getBlock();
    	BlockFace[] directions = {BlockFace.WEST, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
    	for(BlockFace dir : directions) {
        	if(signBlock.getRelative(dir).getType().equals(Material.IRON_BLOCK)) return signBlock.getRelative(dir);
    	}
    	return null;
    }
    
    public String getParent()	 	{ return mineId; }
    public boolean getReset() 		{ return reset; }
    public boolean getPaid()		{ return paid; }
    public double getPrice()		{ return price; }
    public List<String> getLines() 	{ return originalText; }
    
    /**
     * Updates the DisplaySign's lines with the appropriate variables
     * @return <b>true</b> if the update was successful, <b>false</b> otherwise
     */
    public boolean update() {
		BlockState b = sign.getBlock().getState();
		if(b instanceof Sign) {
			Sign signBlock = (Sign) b;
			for(int i = 0; i < originalText.size(); i++) { signBlock.setLine(i, Util.parseVars(originalText.get(i), Mine.get(mineId))); }
			signBlock.update();
			return true;
		}
		return false;
    }
    
    /**
     * Parses through surrounding blocks in search of children to initialize. The search is executed as follows:<br /><br />
     * <b>positive Y — negative Y<br />
     * positive X — negative X<br />
     * positive Z — negative Z</b><br /><br />
     * The initialization is executed via <b>initChild()</b>
     */
    public void initChildren() {
    	Location loc = sign.getLocation();
    	Location locNearby = loc.clone();
    	locNearby.setY(loc.getBlockY() + 1);
    	initChild(locNearby, this);
    	locNearby.setY(loc.getBlockY() - 1);
    	initChild(locNearby, this);
    	locNearby.setY(loc.getBlockY());

    	locNearby.setX(loc.getBlockX() + 1);
    	initChild(locNearby, this);
    	locNearby.setX(loc.getBlockX() - 1);
    	initChild(locNearby, this);
    	locNearby.setX(loc.getBlockX());
    	
    	locNearby.setZ(loc.getBlockZ() + 1);
    	initChild(locNearby, this);
    	locNearby.setZ(loc.getBlockZ() - 1);
    	initChild(locNearby, this);
    	locNearby.setZ(loc.getBlockZ());
    	return;
    }
    
    /**
     * Initializes a child sign at the specified location
     * @param location Location to check
     * @param sign Parent sign
     */
    private static void initChild(Location location, DisplaySign sign) {
    	if(!exists(location)) {
    		BlockState b = location.getBlock().getState();
    		if((b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) && (b instanceof Sign)) {
				String data = ((Sign) b).getLine(0);
				if(data.startsWith("<M>")) {
					Message.debug("Registering a new DisplaySign");
					PrisonMine.addSign(new DisplaySign((Sign) b, sign));
				}
    		}
    	}
    }
    
	/**
	 * Saves the sign data to file.
	 * @return <b>true</b> if the save was successful, <b>false</b> if an error occurred
	 */
	public boolean saveFile() {
		File signFile = new File(new File(PrisonMine.getInstance().getDataFolder(), "signs"), signId + ".psign.yml");
        FileConfiguration signConf =  YamlConfiguration.loadConfiguration(signFile);
        signConf.set("displaysign", this);
        try {
            signConf.save(signFile);
        } catch (IOException e) {
        	Message.log(Level.SEVERE, "Unable to serialize sign '" + signId + "'!");
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	/**
	 * Deletes the sign data file.<br />
	 * <b>Warning:</b> invoking this method will not remove the sign from the list of active signs
	 * @return <b>true</b> if the deletion was successful, <b>false</b> if an error occurred
	 */
	public boolean deleteFile() {
		File signFolder = new File(PrisonMine.getInstance().getDataFolder(), "signs");
		if(!signFolder.exists() || !signFolder.isDirectory()) return false;
		
		File[] signFiles = signFolder.listFiles(new FileFilter() {
            public boolean accept(File file) { return file.getName().contains(".psign.yml"); }
        });
		
		for(File signFile : signFiles) {
			if(signFile.getName().equals(signId+ ".psign.yml")) {
				PrisonMine.removeSign(this);
				return signFile.delete();
			}
		}
		return false;
	}
	
	/**
	 * Creates a pseudo-random ID with a 32-bit key. ID is guaranteed to be unique
	 * @return <b>String</b> random ID
	 */
	private static String generateId() {
		boolean unique = false;
		String id = "";
		do {
			id = Long.toString(Math.abs(new Random().nextLong()), 32);
			if(!exists(id)) unique = true;
		} while (!unique);
		return Long.toString(Math.abs(new Random().nextLong()), 32);
	}
	
	/**
	 * Checks if a DisplaySign exists at the specified location
	 * @param loc Location to check
	 * @return <b>true</b> if there is an initialized DisplaySign at the location, <b>false</b> otherwise
	 */
	public static boolean exists(Location loc) {
		for(DisplaySign sign : PrisonMine.getStaticSigns()) { if(sign.getLocation().equals(loc)) return true; }
		return false;
	}
	
	/**
	 * Checks if a DisplaySign with the specified ID exists
	 * @param id ID to check
	 * @return <b>true</b> if there is a DisplaySign with the specified, <b>false</b> otherwise
	 */
	public static boolean exists(String id) {
		for(DisplaySign sign : PrisonMine.getStaticSigns()) { if(sign.getId().equals(id)) return true; }
		return false;
	}
	
	/**
	 * Returns the DisplaySign at the following location, if it exists
	 * @param loc Location to check
	 * @return <b>DisplaySign</b>, if there is one at the specified location, <b>null</b> otherwise
	 */
	public static DisplaySign get(Location loc) {
		for(DisplaySign sign : PrisonMine.getStaticSigns()) { if(sign.getLocation().equals(loc)) return sign; }
		return null;
	}
	
	/**
	 * Returns the DisplaySign associated with the specified Sign object
	 * @param sign Sign to check
	 * @return <b>DisplaySign</b>, if one is associated with the specified Sign, <b>null</b> otherwise
	 */
	public static DisplaySign get(Sign sign) { return get(sign.getLocation()); }
	
	/**
	 * Returns the DisplaySign with the specified ID, if there is one
	 * @param id ID to check
	 * @return <b>DisplaySign</b>, if there is one with the specified ID, <b>null</b> otherwise
	 */
	public static DisplaySign get(String id) { 
		for(DisplaySign sign : PrisonMine.getStaticSigns()) { if(sign.getId().equals(id)) return sign; }
		return null;
	}
	
	/**
	 * Updates all the DisplaySigns with appropriate variables
	 */
	public static void updateAll() {
		for(DisplaySign sign : PrisonMine.getStaticSigns()) {
			sign.update();
		}
	}
	
	/**
	 * Looks for initializable signs around the block
	 * @param original Location to search around
	 * @return
	 */
	public static List<Block> searchForSign(Location original) {
		List<Block> signs = new ArrayList<Block>();
		
		original.setY(original.getBlockY() + 1);
		Block testBlock = original.getBlock();
		if(testBlock.getType() == Material.WALL_SIGN) signs.add(testBlock);
		
		original.setY(original.getBlockY() - 1);
		testBlock = original.getBlock();
		if(testBlock.getType() == Material.WALL_SIGN) signs.add(testBlock);
		
		original.setX(original.getBlockX() + 1);
		testBlock = original.getBlock();
		if(testBlock.getType() == Material.WALL_SIGN) signs.add(testBlock);
		
		original.setX(original.getBlockX() - 1);
		testBlock = original.getBlock();
		if(testBlock.getType() == Material.WALL_SIGN) signs.add(testBlock);
		
		original.setZ(original.getBlockZ() + 1);
		testBlock = original.getBlock();
		if(testBlock.getType() == Material.WALL_SIGN) signs.add(testBlock);
		
		original.setZ(original.getBlockZ() - 1);
		testBlock = original.getBlock();
		if(testBlock.getType() == Material.WALL_SIGN) signs.add(testBlock);
		
		return signs;
	}
}
