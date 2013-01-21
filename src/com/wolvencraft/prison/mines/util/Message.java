package com.wolvencraft.prison.mines.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.prison.mines.CommandManager;
import com.wolvencraft.prison.mines.PrisonMine;
import com.wolvencraft.prison.mines.mine.Mine;

public class Message extends com.wolvencraft.prison.util.Message {
	private static Logger logger = PrisonMine.getInstance().getLogger();
	
	public static void send(CommandSender sender, String message, boolean parseVars, Mine curMine) {
		if(sender == null) sender = Bukkit.getServer().getConsoleSender();
		if(message == null) message = "";
		if(parseVars && curMine != null) message = Util.parseVars(message, curMine);
		else message = Util.parseChatColors(message);
		sender.sendMessage(message);
	}
	
	public static void send(CommandSender sender, String message, boolean parseVars) {
		send(sender, message, parseVars, PrisonMine.getCurMine(sender));
	}
	public static void send(CommandSender sender, String message) {
		send(sender, message, true);
	}
	public static void send(String message, boolean parseVars) {
		send(CommandManager.getSender(), message, parseVars);
	}
	public static void send(String message) {
		send(CommandManager.getSender(), message, true);
	}

	public static void sendFormatted(CommandSender sender, String title, String message, boolean parseVars, Mine curMine) {
		message = title + " " + ChatColor.WHITE + message;
		send(sender, message, parseVars, curMine);
	}
	public static void sendFormatted(CommandSender sender, String title, String message, boolean parseVars) {
		Message.debug("title = " + title);
		message = title + " " + ChatColor.WHITE + message;
		send(sender, message, parseVars);
	}
	public static void sendFormatted(CommandSender sender, String title, String message) {
		message = title + " " + ChatColor.WHITE + message;
		send(sender, message, true);
	}
	public static void sendFormatted(String title, String message, boolean parseVars) { 
		message = title + " " + ChatColor.WHITE + message;
		send(CommandManager.getSender(), message, parseVars);
	}
	public static void sendFormatted(String title, String message) {
		message = title + " " + ChatColor.WHITE + message;
		send(CommandManager.getSender(), message, true);
	}

	
	public static void sendFormattedSuccess(CommandSender sender, String message, boolean parseVars, Mine curMine) {
		sendFormatted(sender, PrisonMine.getLanguage().GENERAL_SUCCESS, message, parseVars, curMine);
	}
	public static void sendFormattedSuccess(CommandSender sender, String message, boolean parseVars) {
		sendFormatted(sender, PrisonMine.getLanguage().GENERAL_SUCCESS, message, parseVars);
	}
	public static void sendFormattedSuccess(CommandSender sender, String message) {
		sendFormatted(sender, PrisonMine.getLanguage().GENERAL_SUCCESS, message, true);
	}
	public static void sendFormattedSuccess(String message, boolean parseVars) {
		sendFormatted(CommandManager.getSender(), PrisonMine.getLanguage().GENERAL_SUCCESS, message, parseVars);
	}
	public static void sendFormattedSuccess(String message) {
		sendFormatted(CommandManager.getSender(), PrisonMine.getLanguage().GENERAL_SUCCESS, message, true);
	}
	

	public static void sendFormattedError(CommandSender sender, String message, boolean parseVars, Mine curMine) {
		sendFormatted(sender, PrisonMine.getLanguage().GENERAL_ERROR, message, parseVars, curMine);
	}
	public static void sendFormattedError(CommandSender sender, String message, boolean parseVars) {
		sendFormatted(sender, PrisonMine.getLanguage().GENERAL_ERROR, message, parseVars);
	}
	public static void sendFormattedError(CommandSender sender, String message) {
		sendFormatted(sender, PrisonMine.getLanguage().GENERAL_ERROR, message, false);
	}
	public static void sendFormattedError(String message, boolean parseVars) {
		sendFormatted(CommandManager.getSender(), PrisonMine.getLanguage().GENERAL_ERROR, message, parseVars);
	}
	public static void sendFormattedError(String message) {
		sendFormatted(CommandManager.getSender(), PrisonMine.getLanguage().GENERAL_ERROR, message, false);
	}

    /**
     * Broadcasts a message to all players on the server
     * @param message Message to be sent
     */
    public static void broadcast(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
        	if(p.hasPermission("prison.mine.reset.broadcast")) {
        		sendFormatted(p, PrisonMine.getLanguage().GENERAL_SUCCESS, message, false);
        	}
        }
        log(Util.parseChatColors(message));
    }
    
    /**
     * Sends a message into the server log if debug is enabled
     * @param message Message to be sent
     */
    public static void debug(String message) {
        if (PrisonMine.getSettings().DEBUG) log(message);
    }
    
	/**
	 * Sends a message into the server log
	 * @param message Message to be sent
	 */
	public static void log(String message) {
		logger.info(message);
	}
	
	/**
	 * Sends a message into the server log
	 * @param level Severity level
	 * @param message Message to be sent
	 */
	public static void log(Level level, String message) {
		logger.log(level, message);
	}
	
	public static void formatHelp(String command, String arguments, String description, String node) {
		if(!arguments.equalsIgnoreCase("")) arguments = " " + arguments;
		if(Util.hasPermission(node) || node.equals(""))
			send(ChatColor.GOLD + "/mine " + command + ChatColor.GRAY + arguments + ChatColor.WHITE + " " + description);
	}
	
	public static void formatMessage(String message) {
		send(" " + message);
	}
	
	public static void formatHelp(String command, String arguments, String description) {
		formatHelp(command, arguments, description, "");
		return;
	}
	
	public static void formatHeader(int padding, String name) {
		if(name == null) name = "";
		CommandSender sender = CommandManager.getSender();
		String spaces = "";
		for(int i = 0; i < padding; i++) { spaces = spaces + " "; }
		sender.sendMessage(spaces + "-=[ " + ChatColor.BLUE + name + ChatColor.WHITE + " ]=-");
	}
}
