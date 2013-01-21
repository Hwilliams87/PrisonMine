package com.wolvencraft.prison.mines;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.wolvencraft.prison.hooks.CommandHook;
import com.wolvencraft.prison.mines.cmd.*;
import com.wolvencraft.prison.mines.util.Message;

/**
 * bitWolfy's awkward way of registering all the subcommands.<br />
 * Wolfy <3 subcommands. They are kinky.
 * @author bitWolfy
 */
public enum CommandManager implements CommandHook {
	BLACKLIST (BlacklistCommand.class, "prison.mine.edit", true, "blacklist", "bl"),
	DATA (DataCommand.class, "prison.mine.admin", true, "data"),
	DEBUG(DebugCommand.class, "prison.mine.debug", true, "import", "debug", "setregion", "tp", "unload", "setwarp"),
	EDIT (EditCommand.class, "prison.mine.edit", true, "edit", "add", "+", "remove", "-", "delete", "del", "name", "silent", "generator", "link", "setparent", "cooldown", "setregion"),
	FLAG (FlagCommand.class, "prison.mine.edit", true, "flag"),
	HELP (HelpCommand.class, null, true, "help"),
	INFO (InfoCommand.class, "prison.mine.info.time", true, "info"),
	LIST (ListCommand.class, "prison.mine.info.list", true, "list"),
	META (MetaCommand.class, null, true, "meta", "about"),
	PROTECTION (ProtectionCommand.class, "prison.mine.edit", true, "protection", "prot"),
	RESET (ResetCommand.class, null, true, "reset"),
	SAVE (SaveCommand.class, "prison.mine.edit", false, "save", "create", "new"),
	TIME (TimeCommand.class, "prison.mine.info.time", true, "time"),
	TRIGGER (TriggerCommand.class, "prison.mine.edit", true, "trigger"),
	WARNING (WarningCommand.class, "prison.mine.edit", true, "warning");
	
	private static CommandSender sender = null;
	
	CommandManager(Class<?> clazz, String permission, boolean allowConsole, String... args) {
		try {
			this.clazz = (BaseCommand) clazz.newInstance();
			this.permission = permission;
			this.allowConsole = allowConsole;
			alias = new ArrayList<String>();
			for(String arg : args) {
				alias.add(arg);
			}
		} catch (InstantiationException e) {
			Message.log(Level.SEVERE, "Error while instantiating a command! InstantiationException");
			return;
		} catch (IllegalAccessException e) {
			Message.log(Level.SEVERE, "Error while instantiating a command! IllegalAccessException");
			return;
		}
	}
	
	private BaseCommand clazz;
	private List<String> alias;
	private String permission;
	private boolean allowConsole;
	
	public BaseCommand get() { return clazz; }
	public boolean isCommand(String arg) { return alias.contains(arg); }
	public void getHelp() { clazz.getHelp(); }
	public void getHelpLine() { clazz.getHelpLine(); }
	
	public List<String> getLocalAlias() {
		List<String> temp = new ArrayList<String>();
		for(String str : alias) temp.add(str);
		return temp;
	}

	public boolean run(String[] args) {
		if(sender != null) {
			if(sender instanceof Player) Message.debug("Command issued by player: " + sender.getName());
			else if(sender instanceof ConsoleCommandSender) Message.debug("Command issued by CONSOLE");
			else Message.debug("Command issued by GHOSTS and WIZARDS");
		}
		if(!allowConsole && !(sender instanceof Player)) { Message.sendFormattedError(PrisonMine.getLanguage().ERROR_SENDERISNOTPLAYER); return false; }
		if(permission != null && (sender instanceof Player) && !sender.hasPermission(permission)) { Message.sendFormattedError(PrisonMine.getLanguage().ERROR_ACCESS); return false; }
		return clazz.run(args);
	}

	public boolean run(String arg) {
		String[] args = {"", arg};
		return run(args);
	}
	
	public static CommandSender getSender() { return sender; }
	public static void setSender(CommandSender sender) { CommandManager.sender = sender; }
	public static void resetSender() { sender = null; }
	
}