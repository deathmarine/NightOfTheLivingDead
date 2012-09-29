package com.modcrafting.nightofthelivingdead.listeners;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.modcrafting.nightofthelivingdead.runnables.NPCStareRunnable;
import com.modcrafting.nightofthelivingdead.runnables.TimeRunnable;
import com.modcrafting.nightofthelivingdead.runnables.WeatherRunnable;

public class WorldListener implements Listener{
	NightOfTheLivingDead plugin;
	public List<Integer> list = new ArrayList<Integer>();
	
	public WorldListener(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@EventHandler
	public void onWorldLoad(WorldLoadEvent event){
		World world = event.getWorld();
		startWorld(world);
	}

	@EventHandler
	public void onWorldUnload(WorldUnloadEvent event){
		for(Integer i:list){
			plugin.getServer().getScheduler().cancelTask(i);
		}
	}
	
	public void startWorld(World world){
		if(world.getEnvironment().equals(Environment.NORMAL)){
			if(!world.getName().equalsIgnoreCase("world")){
				plugin.getLogger().log(Level.INFO, world.getName()+": started runnables.");
				list.add(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,new TimeRunnable(world), 0, 600L));
				list.add(plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin,new WeatherRunnable(world), 0, 3600L));
			}else{
				list.add(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new NPCStareRunnable(plugin), 0, 1L));
			}
		}
		if(!world.getName().equalsIgnoreCase("city")&&!world.getName().equalsIgnoreCase("world")) world.setAutoSave(false);
	
	}
}
