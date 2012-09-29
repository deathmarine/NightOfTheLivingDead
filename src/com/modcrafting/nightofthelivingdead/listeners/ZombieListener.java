package com.modcrafting.nightofthelivingdead.listeners;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.PathfinderGoalBreakDoor;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMeleeAttack;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalSelector;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.modcrafting.nightofthelivingdead.random.Drops;

public class ZombieListener implements Listener{
	NightOfTheLivingDead plugin;
	Drops drop = new Drops();
	public ZombieListener(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@EventHandler
	public void onWorldSpawn(CreatureSpawnEvent event){
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		if(event.getEntity() instanceof Creeper){
			if(config.getBoolean("PortalCreated", false)){
				if(plugin.gen.nextInt(50)==0){
					event.getLocation().getWorld().spawn(event.getLocation(), PigZombie.class);
				}
			}
			event.getEntity().remove();
			event.setCancelled(true);
		}
		if(event.getEntity() instanceof Enderman || 
				event.getEntity() instanceof Squid  || 
				event.getEntity() instanceof Skeleton || 
				event.getEntity() instanceof Spider || 
				event.getEntity() instanceof Slime ||
				event.getEntity() instanceof Ocelot || 
				event.getEntity() instanceof Wolf){
			event.getEntity().remove();
			event.setCancelled(true);
		}

		//Nether
		if(event.getEntity() instanceof Ghast){
			event.getEntity().remove();
			event.getLocation().getWorld().spawn(event.getLocation(), Giant.class);
		}
		if(event.getEntity() instanceof Blaze || event.getEntity() instanceof MagmaCube){
			event.getEntity().remove();
			event.setCancelled(true);
		}
		if(event.getEntity() instanceof PigZombie){
			if(event.getLocation().getWorld().equals(plugin.getServer().getWorld("world_nether"))){
				event.getLocation().getWorld().spawn(event.getLocation(), Zombie.class);
			}
		}

		if(event.getEntity() instanceof Zombie){
			if(plugin.gen.nextInt(50)==0){
				Zombie zom = (Zombie) event.getEntity();
				try {
					speedUpZombie(zom);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	@EventHandler
	public void onEntityDamagebyEntity(EntityDamageByEntityEvent event){
		if(event.getEntity() instanceof Zombie){
			if(event.getDamager() instanceof Player){
				event.setDamage(event.getDamage()*getDamage(((Player) event.getDamager()))/2);
			}
		}
		if(event.getEntity() instanceof Giant){
			if ((event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.LIGHTNING))){
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
	        }
			event.setDamage(event.getDamage()/4);
		}
		if(event.getEntity() instanceof Player){
			if(event.getDamager() instanceof Zombie){
				
				event.setDamage(event.getDamage()*getDamage(((Player) event.getEntity())));
				
			}
		}
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if(event.getEntity() instanceof Zombie || event.getEntity() instanceof PigZombie){
			if(event.getCause().equals(DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(DamageCause.BLOCK_EXPLOSION)){
				event.setDamage(event.getDamage()*10);
			}
			if ((event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.LIGHTNING))){
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
	        }
		}
		if(event.getEntity() instanceof Giant){
			if ((event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.FIRE) || event.getCause().equals(DamageCause.LIGHTNING))){
				event.setCancelled(true);
				event.getEntity().setFireTicks(0);
	        }
		}
		
		
	}
	@EventHandler
	public void onEntityDeath(final EntityDeathEvent event){
		int modifier = 1;
		event.setDroppedExp(0);
		String world = event.getEntity().getLocation().getWorld().getName();
		if(world.equalsIgnoreCase("oilrig")) modifier = 3;
		if(world.equalsIgnoreCase("city")) modifier = 2;	
		if(!(event.getEntity() instanceof Player)){
			List<ItemStack> list = event.getDrops();
			if(event.getEntity() instanceof Zombie || event.getEntity() instanceof PigZombie){	
				list.clear();		
				if(plugin.gen.nextInt(50)==0){
						Material random = drop.selector(plugin.gen.nextInt(2)+1, plugin.gen.nextInt(5)+1, plugin.gen.nextInt(4)+1, plugin.gen.nextInt(5)+1);
						ItemStack item = new ItemStack(random, 1);
						Integer enchamt = plugin.gen.nextInt(10);
						for(int i=0; i<enchamt; i++){
							int rand = plugin.gen.nextInt(21) + 1;
							Enchantment enchantment = drop.enchant(rand);
							int power = plugin.gen.nextInt(enchantment.getMaxLevel()) + 1;
							item.addUnsafeEnchantment(enchantment, power);
						}
						list.add(item);
				}
				if(plugin.gen.nextInt(25)==0){
					int[] iteml = {296,297,263,264,265,266,260,289,352,287,368};
					Integer random = plugin.gen.nextInt(iteml.length);
					ItemStack item = new ItemStack(iteml[random], plugin.gen.nextInt(5)+1);
					list.add(item);				
				}
				if(plugin.gen.nextInt(30)==0){
					int[] iteml = {372,376,375,377,370,378,369,262,341,367};
					Integer random = plugin.gen.nextInt(iteml.length);
					ItemStack item = new ItemStack(iteml[random], 1);
					list.add(item);				
				}
				if(event.getEntity() instanceof Zombie){
					ArrayList<Player> l = new ArrayList<Player>();
					for(Entity entity : event.getEntity().getNearbyEntities(10, 10, 10)){
						if(entity instanceof Player){
							Player killer = (Player) entity;
							killer.setLevel(killer.getLevel()+(1*modifier));
							l.add(killer);
						}
					}
					EntityDamageEvent e = event.getEntity().getLastDamageCause();
			        if(e instanceof EntityDamageByEntityEvent){
			            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
			            if(ev.getDamager() instanceof Arrow){
			                Arrow a = (Arrow) ev.getDamager();
			                if(a.getShooter() instanceof Player){
			                    Player killer = (Player) a.getShooter();
			                    if(!l.contains(killer)){
									killer.setLevel(killer.getLevel()+(1*modifier));			                    	
			                    }
			               }
			           }
			        }
			        l.clear();
				}

				if(event.getEntity() instanceof PigZombie){

					ArrayList<Player> l = new ArrayList<Player>();
					for(Entity entity : event.getEntity().getNearbyEntities(10, 10, 10)){
						if(entity instanceof Player){
							Player killer = (Player) entity;
							killer.setLevel(killer.getLevel()+(5*modifier));
							l.add(killer);
						}
					}
					EntityDamageEvent e = event.getEntity().getLastDamageCause();
			        if(e instanceof EntityDamageByEntityEvent){
			            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
			            if(ev.getDamager() instanceof Arrow){
			                Arrow a = (Arrow) ev.getDamager();
			                if(a.getShooter() instanceof Player){
			                    Player killer = (Player) a.getShooter();
			                    if(!l.contains(killer)){
									killer.setLevel(killer.getLevel()+(5*modifier));			                    	
			                    }
			               }
			           }
			        }
			        l.clear();
				}
			}

			if(event.getEntity() instanceof Giant){
				ItemStack item = new ItemStack(57, 10);
				list.add(item);	
				StringBuilder sb = new StringBuilder();
				for(Entity entity : event.getEntity().getNearbyEntities(15, 15, 15)){
					if(entity instanceof Player){
						Player killer = (Player) entity;
						String name = killer.getName();
						if(name.equalsIgnoreCase("deathmarin")){
							name = "Deathmarine";
						}
						sb.append(name+", ");
						killer.setLevel(killer.getLevel()+(50*modifier));
					}
					
				}

				plugin.getServer().broadcastMessage(ChatColor.AQUA+sb.toString()+"killed the giant.");
				
				for(int i=0; i<50; i++){
					event.getEntity().getLocation().getWorld().spawn(event.getEntity().getLocation(), Zombie.class);
				}
			}
			if(plugin.gen.nextInt(16)==0){
				ItemStack item = new ItemStack(90, 1);
				list.add(item);
			}
		}
	}	
	@EventHandler
	public void onEntityTarget(EntityTargetLivingEntityEvent event){

		if(event.getTarget() instanceof Player && event.getEntity() instanceof Zombie){
			Player player = (Player) event.getTarget();
			if(player.getLevel()<1000 && plugin.gen.nextInt(100)==0){
				event.getEntity().teleport(event.getTarget());
			}
			if(player.getLevel()>1000 && plugin.gen.nextInt(75)==0){
				event.getEntity().teleport(event.getTarget());
			}
			if(player.getLevel()>1500 && plugin.gen.nextInt(50)==0){
				event.getEntity().teleport(event.getTarget());
			}
			if(player.getLevel()>2000 && plugin.gen.nextInt(25)==0){
				event.getEntity().teleport(event.getTarget());
			}
			if(player.getLevel()>3000 && plugin.gen.nextInt(10)==0){
				event.getEntity().teleport(event.getTarget());
				
			}
		}

	
		if(event.getTarget() instanceof Player && event.getEntity() instanceof Giant){
			Player player = (Player) event.getTarget();
			if(plugin.gen.nextInt(50)==0 && !player.isDead()){
				player.damage(player.getLevel()/1000);
			}
		}
		
	}
	@EventHandler
	public void onEntityStruckbyLight(LightningStrikeEvent event){
		for(Entity entity : event.getLightning().getNearbyEntities(1, 1, 1)){
			if(entity instanceof Zombie){				
				plugin.getServer().broadcastMessage(ChatColor.RED+"Lightning Struck a Zombie!");			
				event.getLightning().getLocation().getWorld().spawn(event.getLightning().getLocation(), Giant.class);
				event.setCancelled(true);
			}
			if(entity instanceof Player || entity instanceof Chicken){
				event.getLightning().getLocation().getWorld().createExplosion(event.getLightning().getLocation(), 4.0F);
			}
		}
	}
	@EventHandler
	public void onPortalCreate(PortalCreateEvent event){
		YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
		config.set("PortalCreated", (Boolean) true);
		plugin.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE+"A portal was created!");
	}
	
	public Integer getDamage(Player player){
		int lvl = player.getLevel();
		int temp = (lvl*2/1000);
		if(temp > 0){
			return temp;
		}else{
			return 1;
		}
	}
	public static void speedUpZombie(Zombie z) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Location loc = z.getLocation();
        EntityZombie zombie = ((CraftZombie) z).getHandle();
        Field fGoalSelector = EntityLiving.class.getDeclaredField("goalSelector");
        fGoalSelector.setAccessible(true);
        Float speed = 0.5F;
        PathfinderGoalSelector gs = new PathfinderGoalSelector(((CraftWorld) loc.getWorld()).getHandle() != null && ((CraftWorld) loc.getWorld()).getHandle().methodProfiler != null ? ((CraftWorld) loc.getWorld()).getHandle().methodProfiler : null);
        gs.a(0, new PathfinderGoalFloat(zombie));
        gs.a(1, new PathfinderGoalBreakDoor(zombie));
        gs.a(2, new PathfinderGoalMeleeAttack(zombie, EntityHuman.class, speed, false));
        gs.a(3, new PathfinderGoalMeleeAttack(zombie, EntityVillager.class, speed, true));
        gs.a(4, new PathfinderGoalMoveTowardsRestriction(zombie, speed));
        gs.a(5, new PathfinderGoalMoveThroughVillage(zombie, speed, false));
        gs.a(6, new PathfinderGoalRandomStroll(zombie, speed));
        gs.a(7, new PathfinderGoalLookAtPlayer(zombie, EntityHuman.class, 15.0F));
        gs.a(7, new PathfinderGoalRandomLookaround(zombie));
        fGoalSelector.set(zombie, gs);
    }
}
