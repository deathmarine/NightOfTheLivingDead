package com.modcrafting.nightofthelivingdead.listeners;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {
	@EventHandler
	public void onWeather(WeatherChangeEvent event){
		event.setCancelled(true);
		World world = event.getWorld();
		//world.setWeatherDuration(1800);
		if(!world.hasStorm()){
			world.setStorm(true);
			world.setThundering(true);
		}else{
			world.setStorm(false);
			world.setThundering(false);
		}
	}
}
