package com.DarkZek.StaffManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
	Main main;
	
	Methods methods;
	
	ChatColor red = ChatColor.RED;
	
	ChatColor green = ChatColor.GREEN;
	
	
	public void setInstance(Main m) {
		main = m;
		methods = main.methods;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		switch (cmd.getName()) {
		case "admin": {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("StaffManager.admin")) {
					
					//Check if player wants to use /admin leave
					if (args.length > 0) {
						
						if (args[0].toLowerCase().equals("leave")) {
							if (main.adminMode.containsKey(player.getName())) {
								methods.leaveAdminMode(player);
								return true;
							} else {
								player.sendMessage("You arent in admin mode!");
								return true;
							}
						}
					}
					//Putting them into admin if they arent already
					if (!main.adminMode.containsKey(player.getName())) {
						methods.enterAdminMode(player);
						return true;
					}
					//Checks if the player it muted because they cant use the gui while muted
					if (main.muted.contains(player.getUniqueId().toString())) {
						player.sendMessage(red + "You cannot use /staff while muted!");
						return true;
					}
					methods.showGUI(player);
					return true;
				} else {
					player.sendMessage(red + "You don't have permission to use that command!");
					return true;
				}
			} else {
				sender.sendMessage("Sorry, Command not avalible from the console!");
				return true;
			}
		}
//		case "updateachievements": {
//			if (sender instanceof Player) {
//				methods.updatePlayerAchievements((Player)sender);
//				sender.sendMessage(ChatColor.GREEN + "Updated your achievements!");
//				return true;
//			} else {
//				sender.sendMessage("You have to be a player to run this command!");
//				return true;
//			}
//			
//		}
		case "warns": {
			String target = "";
			boolean anotherPlayer;
			if (sender == Bukkit.getConsoleSender()) {
				sender.sendMessage("No, you dont have any warnings console >:(");
				return true;
			}
			if (args.length == 0) {
				target = sender.getName();
				anotherPlayer = false;
			} else {
				if (!sender.hasPermission("StaffManager.admin")) {
					sender.sendMessage(red + "You don't have permission to use that command!");
					return true;
				}
				anotherPlayer = true;
				target = args[0];
			}
			target = target.toLowerCase();
			if (main.config.getString("warnings." + target + ".1") == null) {
				if (anotherPlayer) {
					sender.sendMessage(ChatColor.GREEN + target +" has no warnings!");
				} else {
					sender.sendMessage(ChatColor.GREEN + "You have no warnings!");
				}
				
				return true;
			} else if (main.config.getString("warnings." + target + ".2") == null) {
				if (anotherPlayer) {
					sender.sendMessage(ChatColor.DARK_RED + target +" has 1 warning!");
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You have 1 warning!");
				}
				sender.sendMessage(ChatColor.RED + "1. " + main.getConfig().getString("warnings." + target + ".1"));
				return true;
			} else if (main.config.getString("warnings." + target + ".3") == null) {
				if (anotherPlayer) {
					sender.sendMessage(ChatColor.DARK_RED + target +" has 2 warnings!");
				} else {
					sender.sendMessage(ChatColor.DARK_RED + "You have 2 warnings!");
				}
				sender.sendMessage(ChatColor.RED + "1. " + main.getConfig().getString("warnings." + target + ".1"));
				sender.sendMessage(ChatColor.RED + "2. " + main.getConfig().getString("warnings." + target + ".2"));
				return true;
			} else {
				if (anotherPlayer) {
					sender.sendMessage(ChatColor.DARK_RED + "They have 3 or more warnings, they should be banned!");
					sender.sendMessage(ChatColor.RED + "1. " + main.getConfig().getString("warnings." + target + ".1"));
					sender.sendMessage(ChatColor.RED + "2. " + main.getConfig().getString("warnings." + target + ".2"));
					sender.sendMessage(ChatColor.RED + "3. " + main.getConfig().getString("warnings." + target + ".3"));
				}else {
					sender.sendMessage(ChatColor.RED + "You have 3 or more warnings you should be banned! Consider yourself lucky");
				}
			}
			return true;
		}
		case "warn": {
			if (!sender.hasPermission("StaffManager.admin")) {
				sender.sendMessage(red + "You don't have permission to use that command!");
				return true;
			}
			if (args.length <= 1) {
				sender.sendMessage(red + "You forgot to put the player and/or reason");
				return true;
			}
			String target = args[0];
			String reason = "";
			for(int i = 1; i < args.length; i++) {
				reason += args[i] + " ";
			}
			if (reason == "") {
				sender.sendMessage("Please include a reason!");
				return true;
			}
			methods.giveWarning(sender, args[0], reason);
			return true;
		}
		case "unmute": {
    		if (!sender.hasPermission("StaffManager.admin")) {
    			//Checking if they have permission
    			sender.sendMessage(red + "You don't have permission to use that command!");
    			return true;
    		}
    		if (args.length != 1) {
    			//If incorrect args give palyer a error message
    			sender.sendMessage(red + "Error! Incorrect Args!");
    			return true;
    		}
    		
			String id = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
			methods.removeMutedPlayer(id);
			sender.sendMessage(green + "Unmuted " + args[0] + "!");
			return true;
		}
		case "mute": {
    		if (!sender.hasPermission("StaffManager.admin")) {
    			sender.sendMessage(red + "You don't have permission to use that command!");
    			return true;
    		}
    		
    		if (args.length != 1) {
    			sender.sendMessage(red + "Error! Incorrect Args!");
    			return true;
    		}
    		
			String id = Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString();
			methods.addMutedPlayer(id);
			sender.sendMessage(green + "Muted " + args[0] + "!");
			return true;
		}
		case "welcome": {
			if (sender instanceof Player) {
				((Player)sender).chat("Welcome to Sanctuary! Please apply at http://sanctuarymc.clanwebsite.com/");
			} else {
				main.getServer().broadcastMessage("Welcome to Sanctuary! Please apply at http://sanctuarymc.clanwebsite.com/");
			}
		}
		}
		return false;
	}
}
