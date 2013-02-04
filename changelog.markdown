## PrisonMine 1.3 ##

### Compatibility ###

+ CraftBukkit 1.4.7-R1.0
+ PrisonSuite 1.3

### Changes ###

+ New commands:
    + `/mine variables` lists all available variables from an in-game command
    + `/mine setwarp` will set the TP point for the mine
    + `/mine reload` (former `/mine data load`) will reload the data from the file
+ Changes to commands:
    + Changed the `/mine flag` command to have flags save mine-specific options.
        + `/mine flag <flag>` is still the syntax for flags without an option
        + `/mine flag <flag> <option>` is the syntax for flags with an option
    + Block replacement rules:
        + `/mine blacklist help` is now used to receive help docs on the commands
        + `/mine blacklist` will toggle the blacklist on or off, or switch to blacklist if whitelist is enabled
        + `/mine whitelist` will toggle the whitelist on or off, or switch to whitelist if blacklist is enabled
    + Protection:
        + `/mine protection help` is now used to receive help docs on the commands
	    + Block breaking rules:
	        + `/mine prot break blacklist` will toggle the blacklist on or off, or switch to blacklist if whitelist is enabled
	        + `/mine prot break whitelist` will toggle the whitelist on or off, or switch to whitelist if blacklist is enabled
        + Block placement rules:
	        + `/mine prot place blacklist` will toggle the blacklist on or off, or switch to blacklist if whitelist is enabled
	        + `/mine prot place whitelist` will toggle the whitelist on or off, or switch to whitelist if blacklist is enabled
    + Editing:
        + `/mine edit help` is now used to receive help docs on the commands
        + `/mine edit` will now de-select the active mine, instead of `/mine edit none`
        + `/mine cooldown` will toggle the cooldown on and off, instead of `/mine cooldown toggle`
    + Triggers:
        + `/mine trigger help` is now used to receive help docs on the commands
        + `/mine trigger time` will now toggle the timer, instead of `/mine trigger time toggle`
        + `/mine trigger composition` will now toggle the composition reset, instead of `/mine trigger composition toggle`
    + Warnings:
        + `/mine warning toggle` was deprecated. Remove warnings with `/mine warning remove <time>` instead
    + Information:
        + Replaced the messy flag list with a cleaner one
    + Debug:
        + `/mine data save` is now listed as a debugging command, available as `/mine savedata`
    + About:
        + Credited `theangrytomato` for his participation in the development of the plugin
+ New flags:
    + MoneyReward : Pay the players a specified money for mining any blocks in the mine
    + MoneyRewardPlus : Same as above, but excludes the most common block
    + NoExpDrop : Prevents blocks in the mine from dropping experience orbs
    + NoHungerLoss : Players in the mine region will not lose hunger
    + NoPlayerDamage : Any damage dealt to the player will be cancelled
    + PlayerEffect : Add any potion and beacon effects to the players in the mine
    + ResetSound : Play any in-game sound once the mine resets
    + ToolReplace : Once a tool breaks while mining blocks in the mine, it will be replaced with a new one
+ Changes to flags:
    + SurfaceOre : Added an option to have a custom surface ore
    + SuperTools : Axes and spades will not be affected by the flag
    + NoToolDamage: Axes and spades will not be affected by the flag
+ Bug fixes:
    + Security bug fix: Missing permission when breaking a Display Sign
    + Fixed block chance percentages sometimes being invalid due to a rounding error
    + Fixed some variables not being rounded properly on signs
    + Fixed `teleport-players-out-of-the-mine` being set to `false` by default
    + Fixed `<PLAYER>` not reflecting the player who last reset the mine properly
    + Fixed `/mine warning remove` command not working
    + Fixed variables not being parsed in some messages
    + Removed rogue remains of non-existent commands
    + Fixed several mistakes and type-o's
    + General cleanup and optimization

### Known caveats ###

------------------------------

## PrisonMine 1.21 ##

### Compatibility ###

+ CraftBukkit 1.4.7-R0.1
+ PrisonSuite 1.3

### Changes ###

+ Fixed a critical bug that prevented signs from being updated after the server is restarted or plugins are reloaded
+ Fixed a bug that caused automatic resets to be marked as manual
+ Fixed a bug that caused resets triggered by button click to be marked as manual
+ Fixed numerous bugs that affected proper parsing of variables in some cases
+ Moved the option to silence the mine notifications to flags
+ Fixed a bug that prevented invalid tasks from being cancelled properly
+ Fixed flag information missing from the mine info screen
+ Debug commands now have a permission: `prison.mine.debug`

### Known caveats ###

+ While the mines are no longer prone to indefinite reset loops, their timers may still jam. The reset time will freeze and will not change until the server is restarted, or the plugins are reloaded through /reload. This is a known issue and is being worked on.

------------------------------

## PrisonMine 1.2 ##

### Compatibility ###

+ CraftBukkit 1.4.7-R0.1
+ PrisonSuite 1.3

### Changes ###

+ Updated to work with CB 1.4.7-R0.1.
+ Flags Three new options were added to the mines: `SurfaceOre`, `SuperTools`, `NoToolDamage`.
+ New command: `/mine time <mineId>`. Similar to `/mine info`, but this command will only output the time until the mine has to reset.
+ New configuration option: `reset-mines-on-restart`. False by default, this mine will cause all mines to reset when the plugin is being loaded (reset or reload)
+ Completely removed the unused extension system
+ Fixed a critical bug that caused timers to jam, resulting in mines being reset indefinitely
+ Permissions to build and destroy blocks in mines default to `true` now: server owners, take notice
+ Fixed a bug that prevented reset warnings from appearing in chat
+ Fixed a bug that caused duplicate timers to be added for one mine
+ Fixed a bug that caused errors to appear after deleting mines that had children
+ Fixed event listener issues that resulted in PrisonMine overwriting WorldGuard protection
+ Fixed an issue that caused incorrect name to be registered as CommandSender
+ Fixed a bug that caused "ghost timers" after deleting a mine

### Known caveats ###

+ While the mines are no longer prone to indefinite reset loops, their timers may still jam. The reset time will freeze and will not change until the server is restarted, or the plugins are reloaded through `/reload`. This is a known issue and is being worked on.
+ Display signs are bugged in this version and will not work! A fix will be released shortly.

------------------------------

## PrisonMine 1.1 ##

### Compatibility ###

+ CraftBukkit 1.4.6-R1.0
+ PrisonSuite 1.1

### Changes ###

+ Fixed issues on servers running Java 6
+ Fixed compatibility issues with CraftBukkit 1.4.6-R0.1
+ Fixed the critical bug that caused timers to crash when mine had a display name
+ Fixed the critical bug that prevented proper protection handling
+ Fixed ghost files sometimes appearing in the mines directory
+ Fixed internal error on `/mine` command
+ Temporarily removed the flag command to prevent confusion

### Known caveats ###

+ `/mine timer` is present in the help docs, even though this is not a valid command

------------------------------

## PrisonMine 1.0 ##

### Compatibility ###

+ CraftBukkit 1.4.5-R0.1
+ PrisonSuite 1.0a

### Changes ###

+ Initial public release
+ Added an option to import data from MineReset and MineResetLite
+ Added flag functionality. No flags yet available

### Known caveats ###

+ Incompatible with CraftBukkit 1.4.6-R1.0