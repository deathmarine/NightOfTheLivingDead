package com.modcrafting.nightofthelivingdead.runnables;

import org.bukkit.World;

public class TimeRunnable implements Runnable{
	World night;
	public TimeRunnable(World world){
		night = world;
	}
	@Override
	public void run() {
		night.setTime(18000);
	}

}
