package com.modcrafting.nightofthelivingdead.runnables;

import org.bukkit.entity.Player;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.topcat.npclib.entity.HumanNPC;

public class NPCStareRunnable implements Runnable{
	NightOfTheLivingDead plugin;
	public NPCStareRunnable(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@Override
	public void run() {
		for(HumanNPC npc : plugin.npcs){
			for(Player player : npc.getBukkitEntity().getLocation().getWorld().getPlayers()){
				if(npc.getBukkitEntity().getLocation().distance(player.getLocation())<10 && !plugin.npcids.contains(player.getEntityId())){
					npc.lookAtPoint(player.getEyeLocation());
					if(plugin.gen.nextInt(100)==0){
						npc.animateArmSwing();
					}
				}
			}
		}
		
	}

}
