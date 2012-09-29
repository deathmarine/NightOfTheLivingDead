package com.modcrafting.nightofthelivingdead;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.modcrafting.nightofthelivingdead.commands.Commands;
import com.modcrafting.nightofthelivingdead.listeners.BlockListener;
import com.modcrafting.nightofthelivingdead.listeners.PlayerListener;
import com.modcrafting.nightofthelivingdead.listeners.PotionListener;
import com.modcrafting.nightofthelivingdead.listeners.WorldListener;
import com.modcrafting.nightofthelivingdead.listeners.ZombieListener;
import com.modcrafting.nightofthelivingdead.runnables.SpawnRateRunnable;
import com.modcrafting.nightofthelivingdead.runnables.StatsRunnable;
import com.modcrafting.nightofthelivingdead.runnables.StatusRunnable;
import com.modcrafting.nightofthelivingdead.sql.Killsdb;
import com.modcrafting.nightofthelivingdead.sql.SQL;
import com.modcrafting.nightofthelivingdead.sql.Statsdb;
import com.topcat.npclib.NPCManager;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

public class NightOfTheLivingDead extends JavaPlugin implements Listener{
	
	public SQL sql = new SQL(this);
	public Killsdb kills = new Killsdb(this);
	public Statsdb stat = new Statsdb(this);
	//public HashMap<Location, ItemStack> display = new HashMap<Location, ItemStack>();
	public HashMap<Location, String> displayPlayer = new HashMap<Location, String>();
	public HashSet<Integer> item = new HashSet<Integer>();
	public HashSet<Integer> npcids = new HashSet<Integer>();
	public HashMap<Player, Inventory> backpack = new HashMap<Player, Inventory>();
	public PlayerListener playerlistener = new PlayerListener(this);
	public WorldListener worldlistener = new WorldListener(this);
	public HashSet<HumanNPC> npcs = new HashSet<HumanNPC>();
	public NPCManager npcman = null;
	public Location selection;
	public Random gen = new Random();
	public void onDisable(){
		this.getServer().getScheduler().cancelAllTasks();
		npcs.clear();
	}
	public void onEnable(){
		npcman = new NPCManager(this);
		startSql();
		this.getServer().getPluginManager().registerEvents(playerlistener, this);
		this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		this.getServer().getPluginManager().registerEvents(new ZombieListener(this), this);
		this.getServer().getPluginManager().registerEvents(new PotionListener(this), this);
		this.getServer().getPluginManager().registerEvents(worldlistener, this);
		new File("plugins/NightOfTheLivingDead/").mkdir();
		createDefaultConfiguration("config.yml");
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this,new SpawnRateRunnable(this), 0, 1800L);
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this,new StatusRunnable(this), 300L, 4000L);
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this,new StatsRunnable(this), 600L, 6000L);
		for(World world: this.getServer().getWorlds()){
			if(checkloaded(world)) worldlistener.startWorld(world);	
			if(world.getName().equalsIgnoreCase("world")){
				loadDisplayPlayer();				
			}
		}
		
		setCommands();
		
		
		
	}
	
	public void loadDisplayPlayer() {
		YamlConfiguration config = (YamlConfiguration) this.getConfig();
		List<String> list = config.getStringList("NPC.PlayerList");
		for(String name: list){
			String world = config.getString("NPC."+name+".World");
			double x = config.getDouble("NPC."+name+".X");
			double y = config.getDouble("NPC."+name+".Y");
			double z = config.getDouble("NPC."+name+".Z");
			Location loc = new Location(this.getServer().getWorld(world), x, y, z);
			NPC npc = npcman.spawnHumanNPC(name, loc);
			if(npc instanceof HumanNPC){
				HumanNPC human = (HumanNPC) npc;
				human.setItemInHand(Material.DIAMOND_SWORD);
				npcs.add(human);
				if(!npcids.contains(npc.getBukkitEntity().getEntityId()))npcids.add(npc.getBukkitEntity().getEntityId());
				
			}
		}
		
	}
	public void createDefaultConfiguration(String name) {
		File actual = new File(getDataFolder(), name);
		if (!actual.exists()) {

			InputStream input =
				this.getClass().getResourceAsStream("/" + name);
			if (input != null) {
				FileOutputStream output = null;

				try {
					output = new FileOutputStream(actual);
					byte[] buf = new byte[8192];
					int length = 0;
					while ((length = input.read(buf)) > 0) {
						output.write(buf, 0, length);
					}

					System.out.println(getDescription().getName()
							+ ": Default configuration file written: " + name);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (IOException e) {}

					try {
						if (output != null)
							output.close();
					} catch (IOException e) {}
				}
			}
		}
	}
	
	public void setCommands(){
		Commands command = new Commands(this);
		this.getCommand("backpack").setExecutor(command);
		this.getCommand("decoy").setExecutor(command);
		this.getCommand("trash").setExecutor(command);
		this.getCommand("killall").setExecutor(command);
		this.getCommand("resetall").setExecutor(command);
		this.getCommand("banself").setExecutor(command);
		this.getCommand("setitem").setExecutor(command);
		this.getCommand("setplayer").setExecutor(command);
		this.getCommand("xp").setExecutor(command);
	}
	public void startSql(){
		sql.initialize(kills.table, kills.createTable);
		sql.initialize(stat.table, stat.createTable);
	}
	public void setLocation(Location location){
		selection = location;
	}
	public boolean checkloaded(World world){
			if(world.getLoadedChunks().length>0){
				return true;
			}
			return false;
	}
}
