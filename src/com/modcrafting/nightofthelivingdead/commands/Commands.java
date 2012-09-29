package com.modcrafting.nightofthelivingdead.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

public class Commands implements CommandExecutor{
	NightOfTheLivingDead plugin;
	Random generator = new Random();
	HashMap<Player, Integer> it = new HashMap<Player, Integer>(); 
	int count=0;
	public Commands(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, final String[] args) {
		if(sender instanceof Player){
			final Player player = (Player) sender;
			if(label.equalsIgnoreCase("backpack")){
				if(plugin.backpack.containsKey(player)){
					player.openInventory(plugin.backpack.get(player));
				}else{
					int row = 9;
					int size = 4;
					Inventory inventory = plugin.getServer().createInventory(player, row*size, "BackPack");
					plugin.backpack.put(player, inventory);
					player.openInventory(inventory);
				}	
			}

			if(label.equalsIgnoreCase("trash")){
				int row = 9;
				int size = 4;
				Inventory inventory = plugin.getServer().createInventory(player, row*size, "TrashCan");
				player.openInventory(inventory);
				
			}
			if(label.equalsIgnoreCase("decoy")){
				if(player.getLevel()>1500){
					player.setLevel(player.getLevel()-500);
					player.sendMessage(ChatColor.GOLD+"Villager summoned for 500 levels.");
					player.getLocation().getWorld().spawn(player.getLocation(), Villager.class);
				}else{
					player.sendMessage(ChatColor.RED+"You don't have enough levels!");
					
				}
			}
			if(label.equalsIgnoreCase("killall")){
				if(!player.isOp()) return true;
				for(World world: plugin.getServer().getWorlds()){
					for(Entity entity: world.getEntities()){
						if(entity instanceof LivingEntity && !(entity instanceof Player)){
							entity.remove();
						}
					}
				}
			}
			if(label.equalsIgnoreCase("resetall")){
				if(!player.isOp()) return true;
			}
			if(label.equalsIgnoreCase("banself")){
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ipban "+player.getName()+" IPBanned themself, its semi-honorable.");
			}
			if(label.equalsIgnoreCase("setItem")){
				YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
				if(!player.isOp()) return true;
				if(args.length ==1){
					if(plugin.selection!=null){
						int id = Integer.parseInt(args[0]);
						ItemStack item = new ItemStack(Material.getMaterial(id),1);
						//plugin.display.put(plugin.selection, item);
						String name = item.getType().toString();
						Location loc = plugin.selection;
						config.set(name+".World",(String) loc.getWorld().getName());
						config.set(name+".X",(double) loc.getX());
						config.set(name+".Y",(double) loc.getY());
						config.set(name+".Z",(double) loc.getZ());
						List<String> list = config.getStringList("ItemList");
						list.add(name);
						config.set("ItemList", (List<String>) list);
						plugin.saveConfig();
						
					}
				}
				
			}
			if(label.equalsIgnoreCase("setPlayer")){
				YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
				if(!player.isOp()) return true;
				if(args.length ==1){
					if(plugin.selection!=null){
						String name = args[0];
						Location loc = plugin.selection;
						config.set("NPC."+name+".World",(String) loc.getWorld().getName());
						config.set("NPC."+name+".X",(double) loc.getX());
						config.set("NPC."+name+".Y",(double) loc.getY());
						config.set("NPC."+name+".Z",(double) loc.getZ());
						List<String> list = config.getStringList("NPC.PlayerList");
						list.add(name);
						config.set("NPC.PlayerList", (List<String>) list);
						plugin.saveConfig();
						List<NPC> guys = plugin.npcman.getHumanNPCByName(name);
						if(guys.size() < 1){
							NPC npc = plugin.npcman.spawnHumanNPC(name, plugin.selection);
							if(!plugin.npcids.contains(npc.getBukkitEntity().getEntityId()))plugin.npcids.add(npc.getBukkitEntity().getEntityId());
						}
						
					}
				}
			}
			if(label.equalsIgnoreCase("xp")){
				if(args.length<1) return false;
				if(!player.isOp()) return true;
				int lvl=0;
				String name = null;
				if(args.length>1){
					try{
						lvl = Math.abs(Integer.parseInt(args[0]));
					}catch(NumberFormatException nfe){
						name = args[0];
						try{
							lvl = Math.abs(Integer.parseInt(args[1]));
						}catch(NumberFormatException nfe1){
							return false;
						}
					}
				}
				if(lvl!=0&&name==null){
					player.sendMessage(ChatColor.BLUE+"Gave "+args[0]+" levels.");
					player.setLevel(lvl);
					return true;
				}
				if(name!=null){
					player.sendMessage(ChatColor.BLUE+"Gave "+args[1]+" levels.");
					plugin.getServer().getPlayer(name).setLevel(lvl);
					return true;
				}
			}
			if(label.equalsIgnoreCase("merc")){
				if(it.containsKey(player)){
					return true;
				}
				if(player.getLevel()>100){
					player.setLevel(player.getLevel()-100);
				}else{
					return true;
				}
				final NPC npc = plugin.npcman.spawnHumanNPC(player.getName(), player.getLocation());
				final HumanNPC np = (HumanNPC) npc;
				np.setItemInHand(Material.DIAMOND_SWORD);
				int id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
					
					@Override
					public void run() {
						Zombie target = null;
						Player friend = null;
						if(target==null||target.isDead()){
							for(Entity e :npc.getBukkitEntity().getNearbyEntities(6, 6, 6)){
								if(e instanceof Zombie){
									target = (Zombie) e;
								}
								if(e instanceof Player){
									friend = (Player) e;
								}
							}	
						}
						if(target!=null&&!target.isDead()){
							np.walkTo(target.getLocation());
							np.animateArmSwing();
							target.damage(5);
						}else{
							if(friend!=null)np.walkTo(friend.getLocation());
						}
						count++;
						if(count==12000||np.getBukkitEntity().isDead()){
							plugin.getServer().getScheduler().cancelTask(it.remove(player));
						}
						
					}
					
				}, 0L, 1L);
				it.put(player, id);
			}
		}
		return false;
	}

}
