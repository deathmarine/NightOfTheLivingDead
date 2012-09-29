package com.modcrafting.nightofthelivingdead.listeners;

import java.util.Iterator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PotionSplashEvent;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;

public class PotionListener implements Listener{
	NightOfTheLivingDead plugin;
	public PotionListener(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@EventHandler
	public void onPotion(PotionSplashEvent event){
		LivingEntity entity = event.getPotion().getShooter();
		if(entity instanceof Player){
			Player player = (Player) entity;
			if(player.getLevel() >= 3000){
				event.getPotion().getLocation().getWorld().createExplosion(event.getPotion().getLocation(), 0F);
				Iterator<Entity> blownup = event.getPotion().getNearbyEntities(12, 12, 12).iterator();
				while(blownup.hasNext()){
					Entity entity1 = blownup.next();
					if(entity1 instanceof LivingEntity && !(entity1 instanceof Player)){
						((LivingEntity) entity1).damage(100, (Entity) player);
					}
				}
				return;
			}
			if(player.getLevel() >= 1500){
				event.getPotion().getLocation().getWorld().createExplosion(event.getPotion().getLocation(), 0F);
				Iterator<Entity> blownup = event.getPotion().getNearbyEntities(8, 8, 8).iterator();
				while(blownup.hasNext()){
					Entity entity1 = blownup.next();
					if(entity1 instanceof LivingEntity && !(entity1 instanceof Player)){
						((LivingEntity) entity1).damage(100, (Entity) player);
					}
				}
				return;	
			}
			if(player.getLevel() >= 500){
				event.getPotion().getLocation().getWorld().createExplosion(event.getPotion().getLocation(), 0F);
				Iterator<Entity> blownup = event.getPotion().getNearbyEntities(6, 6, 6).iterator();
				while(blownup.hasNext()){
					Entity entity1 = blownup.next();
					if(entity1 instanceof LivingEntity && !(entity1 instanceof Player)){
						((LivingEntity) entity1).damage(50, (Entity) player);
					}
				}
				return;	
			}
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
	   event.blockList().clear();
	}
}
