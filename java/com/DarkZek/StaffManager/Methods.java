package com.DarkZek.StaffManager;

import java.util.ArrayList;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Methods extends BukkitRunnable {
	Main main;
	
	FileConfiguration config;
	
	ChatColor red = ChatColor.RED;
	ChatColor green = ChatColor.GREEN;
	public void setInstance(Main m) {
		main = m;
		config = main.getConfig();
	}
	
	public void saveMutedPlayers() {
		config.set("muted",main.muted);
		main.saveConfig();
	}
	

	
	public void giveWarning(CommandSender sender, String target, String reason) {
		target = target.toLowerCase();
		int warnings = getWarnings(target);
		
		config.set("warnings." + target + "." + (warnings + 1), reason);
		main.saveConfig();
		warnings++;

		Bukkit.getServer().broadcastMessage(ChatColor.GREEN + sender.getName() + " warned " + target + " for " + reason);
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "discord bcast " + sender.getName() + " warned " + target + " for " + reason);
		if (warnings >= 3) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"ban " + target);
			Bukkit.getServer().broadcastMessage(ChatColor.RED + "3 Strikes and your out!");
		}
	}
	
	int getWarnings(String target) {
		if (config.getString("warnings." + target + ".1") == null) {
			return 0;
		} 
		if (config.getString("warnings." + target + ".2") == null) {
			return 1;
		} 
		if (config.getString("warnings." + target + ".3") == null) {
		return 2;
		}
		if (config.getString("warnings." + target + ".3") != null) {
		return 3;
		} else {
			return -100;
		}
	}
	
//	public void updatePlayerAchievements(Player pl) {
//		int achievments = 0;
//		if (this.main.achievements.get(pl) == null) {
//			achievments = this.getPlayerAchevements(pl);
//		} else {
//			achievments = this.main.achievements.get(pl);
//		}
//		main.achievements.put(pl.getName(), achievments);
//		this.main.getServer().dispatchCommand(this.main.getServer().getConsoleSender(), "scoreboard players set " + pl.getName() +" Achievements " + achievments);
//	}
//	
//	public int getPlayerAmountAchievements(Player pl) {
//		int count = 0;
//		for(Achievement ach : Achievement.values()) {
//			if (pl.hasAchievement(ach)) {
//				count++;
//			}
//		}
//		
//		return count;
//	}
	
	void addMutedPlayer(String uuid) {
		main.muted.add(uuid);
		saveMutedPlayers();
	}
	
	void removeMutedPlayer(String uuid) {
		main.muted.remove(uuid);
		saveMutedPlayers();
	}
	
	public void setPlayerAchevements(Player pl, int achevements) {
		config.set(pl.getUniqueId() + ".achevements", achevements);
		main.saveConfig();
	}
	
	public int getPlayerAchevements(Player pl) {
		int ach = 0;
		

		
		
		return ach;
	}
	
	public void showGUI(Player player) {
		Inventory inv = Bukkit.createInventory(null, 9, main.GUIName);
		
		//Item #1 - Mute
		ItemStack mute = new ItemStack(Material.WRITTEN_BOOK,1);
		ItemMeta muteMeta = mute.getItemMeta();
		//Set name and lure..
		muteMeta.setDisplayName(red + "Mute a player");
		ArrayList<String> metaLore = new ArrayList<String>();
		metaLore.add("Mutes a player!");
		muteMeta.setLore(metaLore);
		mute.setItemMeta(muteMeta);
		inv.setItem(3,mute);
		
		
		
		//Item #2 - Unmute
		ItemStack unmute = new ItemStack(Material.BOOK_AND_QUILL,1);
		ItemMeta unmuteMeta = unmute.getItemMeta();
		//Set name and lure..
		unmuteMeta.setDisplayName(red + "Unmute a player");
		ArrayList<String> unmetaLore = new ArrayList<String>();
		unmetaLore.add("Unmutes a player!");
		unmuteMeta.setLore(unmetaLore);
		unmute.setItemMeta(unmuteMeta);
		inv.setItem(4,unmute);
		
		
		
		//Item #3 - Leave Admin Mode
		ItemStack spec = new ItemStack(Material.EYE_OF_ENDER,1);
		ItemMeta specMeta = spec.getItemMeta();
		//Set name and lure..
		specMeta.setDisplayName(red + "Leave Admin Mode");
		ArrayList<String> specLore = new ArrayList<String>();
		specLore.add("Leaves Admin Mode!");
		specMeta.setLore(specLore);
		spec.setItemMeta(specMeta);
		inv.setItem(5,spec);
		
		//Item #6 - Ban
        ItemStack skull = new ItemStack(Material.WOOD_AXE);
        ItemMeta sm = skull.getItemMeta();
    	ArrayList<String> skullLore = new ArrayList<String>();
		skullLore.add("Bans a player!");
		sm.setLore(skullLore);
        sm.setDisplayName(red + "Ban");
        skull.setItemMeta(sm);
        
        inv.setItem(1,skull);
        
		//Item #7 - Unban
        ItemStack skull2 = new ItemStack(Material.SKULL_ITEM);
        skull2.setDurability((short)3);
        SkullMeta sm2 = (SkullMeta) skull2.getItemMeta();
        sm2.setOwner("TheNabwoodKidd");
		ArrayList<String> skullLore2 = new ArrayList<String>();
		skullLore2.add("Unbans a player!");
		sm2.setLore(skullLore2);
        sm2.setDisplayName(red + "Unban");
        skull2.setItemMeta(sm2);

        inv.setItem(2,skull2);
        
		//Item #8 - CheckInv
		ItemStack invC = new ItemStack(Material.CHEST,1);
		ItemMeta invCMeta = invC.getItemMeta();
		//Set name and lure..
		invCMeta.setDisplayName(red + "Inv See");
		ArrayList<String> invCe = new ArrayList<String>();
		invCe.add("Check a players inventory!");
		invCMeta.setLore(invCe);
		invC.setItemMeta(invCMeta);
		inv.setItem(6,invC);
		
		//Item #9 - Credit
        ItemStack darkzek = new ItemStack(Material.SKULL_ITEM);
        darkzek.setDurability((short)3);
        SkullMeta darkzekMeta = (SkullMeta) darkzek.getItemMeta();
        darkzekMeta.setOwner("DarkZek");
		ArrayList<String> darkzekLore = new ArrayList<String>();
		darkzekLore.add("The creator of the plugin.");
		darkzekMeta.setLore(darkzekLore);
		darkzekMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.MAGIC + "::" + ChatColor.RED + "" + ChatColor.BOLD + "DarkZek" + ChatColor.DARK_RED + "" + ChatColor.MAGIC + "::");
        darkzek.setItemMeta(darkzekMeta);
        inv.setItem(7, darkzek);
        
		player.openInventory(inv);
	}
	
	public void enterAdminMode(Player pl) {
		main.adminMode.put(pl.getName(), pl.getLocation());
		pl.setGameMode(GameMode.SPECTATOR);
		pl.performCommand("vanish on");
	}
	
	public void leaveAdminMode(Player pl) {
    	if (!main.adminMode.containsKey(pl.getName())) {
    		return;
    	}
		pl.performCommand("vanish off");
		pl.setGameMode(GameMode.SURVIVAL);
		Location ad = (Location) main.adminMode.get(pl.getName());
		pl.teleport(ad);
		main.adminMode.remove(pl.getName());
	}
	
	void mute(String player, CommandSender sender) {
		String id = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
		if (id == null) {
			sender.sendMessage( green + "Cannot mute " + player + "! They have never joined the server!");
			return;
		}

		addMutedPlayer(id);
		sender.sendMessage(green + "Muted " + player + "!");
	}
	void unmute(String player, CommandSender sender) {
		String id = Bukkit.getOfflinePlayer(player).getUniqueId().toString();
		if (id == null) {
			sender.sendMessage( green + "Cannot unmute " + player + "! They have never joined the server!");
			return;
		}
		addMutedPlayer(id);
		sender.sendMessage(green + "Unmuted " + player + "!");
	}
}
