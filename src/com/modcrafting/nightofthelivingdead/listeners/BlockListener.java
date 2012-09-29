package com.modcrafting.nightofthelivingdead.listeners;

import java.util.Random;

import net.minecraft.server.Material;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;

public class BlockListener implements Listener{
	NightOfTheLivingDead plugin;
	Random generator = new Random();
	public BlockListener(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if(event.getBlock().getType().equals(Material.TNT)){
			if(event.getPlayer().getLevel()<1000){
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event){
		if(event.getPlayer().getLevel()>3000){
			if(event.getBlock().getType().getId()==7){
				event.setInstaBreak(true);
			}
		}
	}
}
