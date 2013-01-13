package com.wolvencraft.prison.mines.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;

import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.generation.BaseGenerator;
import com.wolvencraft.prison.mines.generation.RandomGenerator;

public class ExtensionLoader {
	
	/**
	 * Loads all the generators from file. Always loads the default <b>RandomGenerator</b> first, then loads everything else.<br />
	 * Invalid generators will be skipped.
	 * @return
	 */
	public static List<BaseGenerator> loadAll() {
		List<BaseGenerator> generators = new ArrayList<BaseGenerator>();
		File dir = new File(PrisonMine.getInstance().getDataFolder() + "/extensions");

		if (!dir.exists()) { dir.mkdir(); }

	    String name = "";
	    try {
	    	generators.add((BaseGenerator) RandomGenerator.class.newInstance());
	    	name = "RandomGenerator";
			Message.log("Loaded extension: " + name);

			ClassLoader loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, BaseGenerator.class.getClassLoader());
			
			for (File file : dir.listFiles()) {
			    if (!file.getName().endsWith(".class")) continue;
			    
			    name = file.getName().substring(0, file.getName().lastIndexOf("."));
		
				Class<?> clazz = loader.loadClass(name);
				Object object = clazz.newInstance();
				if (object instanceof BaseGenerator) {
					BaseGenerator generator = (BaseGenerator) object;
					generators.add(generator);
				} else continue;
				Message.log("Loaded extension: " + name);
			}
	    }
	    catch (MalformedURLException ex) 			{ Message.log(Level.SEVERE, "Error while configuring generator class loader [MalformedURILException]"); }
	    catch (ClassNotFoundException cnfe) 		{ Message.log(Level.WARNING, "Error loading " + name + "! Generator disabled. [ClassNotFoundException]"); }
	    catch (InstantiationException ie) 			{ Message.log(Level.WARNING, "Error loading " + name + "! Generator disabled. [InstantiationException]"); }
	    catch (IllegalAccessException iae) 			{ Message.log(Level.WARNING, "Error loading " + name + "! Generator disabled. [IllegalAccessException]"); }
	    catch (Exception ex) 						{ Message.log(Level.WARNING, "Error loading " + name + "! Generator disabled. [Exception]"); }
	    catch (ExceptionInInitializerError eiie) 	{ Message.log(Level.WARNING, "Error loading " + name + "! Generator disabled. [ExceptionInInitializer]"); }
	    catch (Error ex) 							{ Message.log(Level.WARNING, "Error loading " + name + "! Generator disabled. [Error]"); }
		
	    
		return generators;
	}
	
	/**
	 * Returns the generator object that is represented by the name provided
	 * @param name Name of the generator, case insensitive
	 * @return Generator object, or null if none found
	 */
	public static BaseGenerator get(String name) {
		List<BaseGenerator> generators = PrisonMine.getGenerators();
		for(BaseGenerator generator: generators)
			if(generator.getName().equalsIgnoreCase(name)) return generator;
		if(name.equalsIgnoreCase("")) return generators.get(0);
		return null;
	}
	
	/**
	 * Returns a user-friendly string with a list of generator names
	 * @return List of generator names
	 */
	public static String list() {
		List<BaseGenerator> generators = PrisonMine.getGenerators();
		String list = "";
		for(BaseGenerator gen : generators) {
			list = list + ChatColor.GOLD + gen.getName() + ChatColor.WHITE + ", ";
		}
		list = list.substring(0, list.length() - 2);
		return list;
	}
}
