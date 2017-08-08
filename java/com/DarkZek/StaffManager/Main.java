package com.DarkZek.StaffManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	//
	// If you are getting errors make sure you are using java 7 or above in your IDE!
	//

	public HashMap<String, String> names = new HashMap<String, String>();
	
	public HashMap<String, Location> adminMode = new HashMap<String, Location>();
	
	public ArrayList<String> muted;
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public String GUIName = ChatColor.GREEN + "Staff Menu";
	
	public String TPGUIName = ChatColor.RED + "Teleport Options";
	
	FileConfiguration config = this.getConfig();
	
	Methods methods = new Methods();
	
	Commands commands = new Commands();
	
	Listeners listeners = new Listeners();
	
	Plugin plugin;
	
	public void onEnable() {
		config = this.getConfig();
		//Initiate all classes
		methods.setInstance(this);
		commands.setInstance(this);
		listeners.setInstance(this);
		
		plugin = this;
		
		this.getCommand("admin").setExecutor(commands);
		this.getCommand("unmute").setExecutor(commands);
		this.getCommand("mute").setExecutor(commands);
		this.getCommand("warn").setExecutor(commands);
		this.getCommand("warns").setExecutor(commands);
		this.getCommand("updateachievements").setExecutor(commands);
		this.getCommand("welcome").setExecutor(commands);
		
		PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(listeners, this);
        muted = (ArrayList<String>)config.get("muted");
        if (muted == null) {
        	muted = new ArrayList<String>();
        	this.getLogger().info("Looks like you removed the muted players list! Generating a new one...");
        }
        //achievements = (HashMap<String, Integer>)config.get("achievements");
//        if (achievements == null) {
//        	achievements = new HashMap<String, Integer>();
//        	this.getLogger().info("Looks like you removed the achievements list! Generating a new one...");
//        }
//        if (this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
//        	this.logger.info("Hooked into protocollib");
//        	protocolManager = ProtocolLibrary.getProtocolManager();
//            protocolManager.addPacketListener(
//            		  new PacketAdapter(this, ListenerPriority.MONITOR, 
//            		          PacketType.Status.Server.SERVER_INFO) {
//            		    @Override
//            		    public void onPacketSending(PacketEvent event) {
//            		        // Item packets (id: 0x2D)
//            		        if (event.getPacketType() == PacketType.Status.Server.SERVER_INFO) {
//            		        	PacketConstructor playerListConstructor;
//            		        	ProtocolManager manager;
//            		        	playerListConstructor = manager.createPacketConstructor(PacketType.Play.Server.PLAYER_INFO, "", false, (int) 0);
//            		        	PacketContainer packet = playerListConstructor.createPacket(name, visible, getPlayerPing(player));
//            		            event.setCancelled(true);
//            		            
//            		        }
//            		    }
//            		});
//        }
        
		this.logger.info("StaffManager was Enabled!");
	}
	public void onDisable() {
		
		Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[Bukkit.getServer().getOnlinePlayers().size()]);
		
		for (int i = 0; i > players.length; i++) {
			if (adminMode.containsKey(players[i])) {
				players[i].teleport((Location) adminMode.get(players[i].getName()));
				players[i].setGameMode(GameMode.SURVIVAL);
			}
		}
		
		this.logger.info("StaffManager was Disabled!");
		if (!this.getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		config.set("muted", muted);
//		config.set("achievements", achievements);
		this.saveConfig();
	}
	



}


