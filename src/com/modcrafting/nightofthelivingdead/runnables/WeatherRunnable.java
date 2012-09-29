package com.modcrafting.nightofthelivingdead.runnables;

import org.bukkit.World;

public class WeatherRunnable implements Runnable{
	World world;
	public WeatherRunnable(World main){
		world = main;
	}
	@Override
	public void run() {
		if(world == null) return;
		if(!world.hasStorm()){
			world.setStorm(true);
			world.setThundering(true);
		}else{
			world.setStorm(false);
			world.setThundering(false);
		}
		
	}

}
