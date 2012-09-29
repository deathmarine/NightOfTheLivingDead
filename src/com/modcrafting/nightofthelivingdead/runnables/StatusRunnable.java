package com.modcrafting.nightofthelivingdead.runnables;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Zombie;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;

public class StatusRunnable implements Runnable{
	NightOfTheLivingDead plugin;
	public StatusRunnable(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@Override
	public void run() {
		plugin.getServer().broadcastMessage(ChatColor.BLUE+"-- Status of Worlds --");
		for(World world:plugin.getServer().getWorlds()){
			String name = world.getName();
			if(!name.contains("world")){
				int amtPlayers = world.getPlayers().size();
				int amtZombies = world.getEntitiesByClass(Zombie.class).size();
				StringBuilder sb = new StringBuilder();
				int size = name.length();
				sb.append(name);
				if(size!=18){
					for(int i=0; i<(18-size); i++){
						sb.append(" ");
					}
				}
				if(amtPlayers>0)plugin.getServer().broadcastMessage(ChatColor.AQUA+sb.toString()+"- "+ChatColor.DARK_AQUA+"Players:"+String.valueOf(amtPlayers)+" "+ChatColor.GREEN+"Zombies:"+String.valueOf(amtZombies));
			}
		}
		
	}

}
