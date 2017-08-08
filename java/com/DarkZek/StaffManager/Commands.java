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

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().toLowerCase().equals("admin")) {
            return admin(sender, cmd, args);
        } else if (cmd.getName().toLowerCase().equals("mute")) {
            return mute(sender, cmd, args);
        } else if (cmd.getName().toLowerCase().equals("welcome")) {
            if (sender instanceof Player) {
                ((Player)sender).chat("Welcome to Sanctuary! Please apply at http://sanctuarymc.clanwebsite.com/");
            } else {
                main.getServer().broadcastMessage("Welcome to Sanctuary! Please apply at http://sanctuarymc.clanwebsite.com/");
            }
        }
		return true;
	}

	boolean mute(CommandSender sender, Command cmd, String[] args) {
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

	boolean admin(CommandSender sender, Command cmd, String[] args) {
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
}
