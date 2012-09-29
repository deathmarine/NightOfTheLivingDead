package com.modcrafting.nightofthelivingdead.runnables;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;

public class SpawnRateRunnable implements Runnable{
	NightOfTheLivingDead plugin;
	public SpawnRateRunnable(NightOfTheLivingDead instance){
		plugin = instance;
	}

	@Override
	public void run() {
		if(plugin.getServer().getOnlinePlayers().length>0){
			for(World world: plugin.getServer().getWorlds()){
				int rate = world.getPlayers().size()*1000;
				if(world.getName().equalsIgnoreCase("oilrig")){
					rate = world.getPlayers().size()*1000;
				}
				if(rate==0){
					if(world.getEntities().size()!=0){
						for(Entity entity: world.getEntities()){
							if(entity instanceof LivingEntity && !(entity instanceof Player)){
								entity.remove();
							}
						}
					}
				}
				if(rate>10000){
					rate=10000;
				}
				if(world.getName().equalsIgnoreCase("city")){
					rate = world.getPlayers().size()*100;
				}
				if(world.getEnvironment().equals(Environment.NORMAL)) world.setMonsterSpawnLimit(rate);
				
			}
		}
	}
}
