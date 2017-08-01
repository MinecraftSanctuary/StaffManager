package com.DarkZek.StaffManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Listeners implements Listener{
	Main main;
	
	Methods methods;
	
	Player sleeper;
	
	ChatColor red = ChatColor.RED;
	
	ChatColor green = ChatColor.GREEN;
	
	public void setInstance(Main m) {
		main = m;
		methods = main.methods;
	}
//	@EventHandler
//	public void onPlayerAchevementGiven(PlayerAchievementAwardedEvent e) {
//		Player pl = e.getPlayer();
//		int ach = methods.getPlayerAchevements(pl);
//		
//		ach += 1;
//		if (ach == 1) {
//			pl.hasAchievement(Achievement.MINE_WOOD);
//			ach = methods.getPlayerAchevements()
//		}
//		
//		
//		
//		methods.setPlayerAchevements(pl, ach);
//	}
	
	@EventHandler
	public void onEntityKilled(EntityDeathEvent event) {
		if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
			Entity entity = event.getEntity().getKiller();
			if (entity instanceof Player) {
				((Player)entity).getInventory().addItem(new ItemStack(Material.ELYTRA));
				((Player)entity).sendMessage(ChatColor.GREEN + "Here is an elytra from killing the ender dragon");
			}
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreProcessEvent(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		String msg = e.getMessage();
		if (!main.muted.contains(player.getUniqueId().toString())) {
			return;
		}
		if (msg.toLowerCase().startsWith("/me")) {
			player.sendMessage(red + "You are muted!");
			e.setCancelled(true);
			return;
		}
		if (msg.toLowerCase().startsWith("/tell")) {
			player.sendMessage(red + "You are muted!");
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
    	methods.leaveAdminMode(e.getPlayer());
    	return;
    }
	
//	@EventHandler
//	public void onAchievementGiven(PlayerAchievementAwardedEvent e) {
//		Player pl = e.getPlayer();
//		methods.updatePlayerAchievements(pl);
//	}
	
	@EventHandler
	public void onPlayerClickPlayer(PlayerInteractAtEntityEvent e) {
		Player pl = e.getPlayer();
		if (e.getRightClicked() instanceof Player) {
			Player target = (Player) e.getRightClicked();
			if (main.adminMode.containsKey(pl.getName())) {
				pl.openInventory(target.getInventory());
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if (main.muted.contains(player.getUniqueId().toString())) {
			player.sendMessage(red + "You are muted!");
			e.setCancelled(true);
			return;
		}
		
		String command = (String) main.names.get(player.getName());
		if (command == null) {
			//Easter Egg
			if (e.getMessage().toLowerCase().contains("sheep")) {
				main.getServer().broadcastMessage("Baa means yes!");
				main.getServer().dispatchCommand(main.getServer().getConsoleSender(), "discord bcast Baa means yes");
				e.getMessage().replace("sheep", "best animals in the world");
			}
			return;
		}
		switch (command) {
		case "mute": {
			main.names.remove(player.getName());
			methods.mute(e.getMessage(), player);
			e.setCancelled(true);
			break;
		}
		case "ban": {
			main.names.remove(player.getName());
			main.getServer().dispatchCommand(main.getServer().getConsoleSender(), "ban " + e.getMessage());
			player.sendMessage("Banned " + e.getMessage());
			e.setCancelled(true);
			break;
		}
		case "unban": {
			main.names.remove(player.getName());
			main.getServer().dispatchCommand(main.getServer().getConsoleSender(), "pardon " + e.getMessage());
			player.sendMessage("Unbanned " + e.getMessage());
			e.setCancelled(true);
			break;
		}
		
		case "invsee": {
			main.names.remove(player.getName());
			Player pl = Bukkit.getPlayer(e.getMessage());
			if (pl == null) {
				player.sendMessage( green + "Player: " + e.getMessage() + " Has never joined!");
				e.setCancelled(true);
				return;
			}
			player.closeInventory();
			Inventory inv = pl.getInventory();
			player.openInventory(pl.getInventory());
			e.setCancelled(true);
			break;
		}
		
		case "unmute": {
			main.names.remove(player.getName());
			methods.unmute(e.getMessage(), player);
			e.setCancelled(true);
			return;
		}
		case "tpplayer": {
			main.names.remove(player.getName());
			Player pl = Bukkit.getPlayer(e.getMessage());
			if (pl == null) {
				player.sendMessage( green + "Player: " + e.getMessage() + " Is not online!");
				e.setCancelled(true);
				return;
			}

			player.teleport(pl);
			player.sendMessage("Teleported you to " + e.getMessage());
			e.setCancelled(true);
			break;
		}
		case "tp2u": {
			main.names.remove(player.getName());
			Player pl = Bukkit.getPlayer(e.getMessage());
			if (pl == null) {
				player.sendMessage( green + "Player: " + e.getMessage() + " Is not online!");
				e.setCancelled(true);
				return;
			}

			pl.teleport(player);
			player.sendMessage("Teleported " + e.getMessage() +" to you");
			e.setCancelled(true);
			break;
		}
		case "tploc": {
			
			String[] loc = e.getMessage().split(" ");
			main.names.remove(player.getName());
			
			
			
			if (loc.length < 3 || loc.length > 4) {
				player.sendMessage( green + "Incorrect args! " + e.getMessage());
				player.sendMessage( green + "It should be 'X Y Z (optional world 1 = overworld 2 = nether 3 = end)' ");
				return;
			}
			Location tpLoc = null;
			int x = 0;
			int y = 0;
			int z = 0;
			int world = 0;
			if (loc.length == 3) {
				try {
					x = Integer.parseInt(loc[0]);
					y = Integer.parseInt(loc[1]);
					z = Integer.parseInt(loc[2]);
				} catch (NumberFormatException ex) {
					player.sendMessage(red + "That was not numbers!");
					player.sendMessage( red + "It should be 'X Y Z (optional world)' ");
					return;
				}

				
				tpLoc = new Location(Bukkit.getServer().getWorld("world"), (float)x,(float)y,(float)z);
			}else {
				try {
					x = Integer.parseInt(loc[0]);
					y = Integer.parseInt(loc[1]);
					z = Integer.parseInt(loc[2]);
					world = Integer.parseInt(loc[3]);
				} catch (NumberFormatException ex) {
					player.sendMessage(red + "That was not numbers!");
					player.sendMessage( red + "It should be 'X Y Z (optional world)' ");
					return;
				}
			}

			if (world == 0) {
				
				tpLoc = new Location(Bukkit.getServer().getWorld("world"), (float)x,(float)y,(float)z);
			} else if (world == 1) {
				tpLoc = new Location(Bukkit.getServer().getWorld("world_nether"), (float)x,(float)y,(float)z);
			} else if (world == 2) {
				tpLoc = new Location(Bukkit.getServer().getWorld("world_the_end"), (float)x,(float)y,(float)z);
			}else {
				tpLoc = new Location(Bukkit.getServer().getWorld("world"), (float)x,(float)y,(float)z);
			}
			
			player.teleport(tpLoc);
			player.sendMessage(green + "Teleported you to " + e.getMessage());
			e.setCancelled(true);
			break;
		}
		}
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player player = (Player)e.getWhoClicked();
		if (e.getClickedInventory() == null) {
			return;
		}
		Inventory inv = e.getClickedInventory();
		ItemStack item = e.getCurrentItem();
		if (inv.getName() == "") {
			e.setCancelled(true);
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getItemMeta() == null) {
			return;
		}
		if (item.getItemMeta().getDisplayName() == null) {
			return;
		}
		if (!player.hasPermission("StaffManager.admin")) {
			return;
		}

		if (inv.getName() != main.GUIName) {
			if (ChatColor.stripColor(inv.getName()) == "Teleport Options") {
				return;
			}
		}
		switch (ChatColor.stripColor( item.getItemMeta().getDisplayName())) {
		case "Mute a player": {
			main.names.put(player.getName(),"mute");
			player.sendMessage("Type in the player to mute!");
			e.setCancelled(true);
			player.closeInventory();
			return;
		}
		case "::DarkZek::": {
			e.setCancelled(true);
			return;
		}
		case "Unmute a player": {
			main.names.put(player.getName(),"unmute");
			player.sendMessage("Type in the player to unmute!");
			e.setCancelled(true);
			player.closeInventory();
			return;
		}
		case "Ban": {
			main.names.put(player.getName(),"ban");
			e.setCancelled(true);
			player.closeInventory();
			player.sendMessage("Type in the name of the player to ban and optionally, the reason");
			return;
		}
		case "Unban": {
			main.names.put(player.getName(),"unban");
			e.setCancelled(true);
			player.closeInventory();
			player.sendMessage("Type in the name of the player to unban");
			return;
		}
		case "Inv See": {
			main.names.put(player.getName(),"invsee");
			e.setCancelled(true);
			player.closeInventory();
			player.sendMessage("Type in the name of the player to check the inv of");
			return;
		}
		case "Leave Admin Mode": {
			player.performCommand("v");
			player.setGameMode(GameMode.SURVIVAL);
			Location ad = (Location) main.adminMode.get(player.getName());
			player.teleport(ad);
			main.adminMode.remove(player.getName());
			e.setCancelled(true);
			
			player.closeInventory();
			return;
		}
		}
		
	}
	@EventHandler
	public void onSleep(PlayerBedEnterEvent e ) {
		sleeper = e.getPlayer();
		main.getServer().broadcastMessage( red + sleeper.getDisplayName() + " went to bed, day in 10 seconds!");

        // Create the task anonymously and schedule to run it once, after 20 ticks
        new BukkitRunnable() {
        
            @Override
            public void run() {
                // What you want to schedule goes here
            	if (sleeper != null) {
            		if (sleeper.isSleeping()) {
            			main.getServer().getWorld("world").setTime(0);
            			main.getServer().getWorld("world").setStorm(false);
            			main.getServer().broadcastMessage(red + sleeper.getDisplayName() + " made it day!");
            		}
            	}
            }
            
        }.runTaskLater(this.main, 200);
	}
	@EventHandler
	public void death(PlayerDeathEvent e) {
		Player killed = main.getServer().getPlayer(e.getEntity().getName());
		if (e.getEntity().getKiller() instanceof Player) {
			Player killer = main.getServer().getPlayer(e.getEntity().getKiller().getName());
			e.setDeathMessage(ChatColor.RED + killed.getName() + " has been beheaded by " + killer.getName());
			Player pl = (Player)e.getEntity();
			
			
	        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
	        skull.setDurability((short)3);
	        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
	        skullMeta.setOwner(pl.getName());
			skull.setItemMeta(skullMeta);
	        main.getServer().getWorld(killed.getLocation().getWorld().getName()).dropItemNaturally(killed.getLocation(), skull);
		}
	}
	
}
