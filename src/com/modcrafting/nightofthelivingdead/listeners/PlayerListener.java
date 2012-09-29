package com.modcrafting.nightofthelivingdead.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.topcat.npclib.entity.NPC;

public class PlayerListener implements Listener{
	public String spamcheck;
	public int spamCount;
	public HashMap<Player, ItemStack> deadsword = new HashMap<Player, ItemStack>();
	public HashMap<String, Integer> relogs = new HashMap<String, Integer>();
	public HashSet<Player> anotherWorld = new HashSet<Player>();
	public HashSet<Player> cooldown = new HashSet<Player>();
	NightOfTheLivingDead plugin;
	Random generator = new Random();
	public PlayerListener(NightOfTheLivingDead instance){
		plugin = instance;
	}
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		if(event.getPlayer().getWorld().getEnvironment().equals(Environment.NETHER)){
			event.setCancelled(true);
			event.getPlayer().sendMessage(netherMessage(plugin.gen.nextInt(12)+1));
		}
	}
	public String netherMessage(int num){
		switch(num){
		case 1: return ChatColor.LIGHT_PURPLE+"[Server] Wierd I can't hear you for some reason?";
		case 2: return ChatColor.LIGHT_PURPLE+"[Server] Commands might be disabled, I'm unsure though.";
		case 3: return ChatColor.LIGHT_PURPLE+"[Server] Swag?";
		case 4: return ChatColor.LIGHT_PURPLE+"[Server] You going to cry?";
		case 5: return ChatColor.LIGHT_PURPLE+"[Server] You cries go unanswered.";
		case 6: return ChatColor.LIGHT_PURPLE+"[Server] You hate life yet?";
		case 7: return ChatColor.LIGHT_PURPLE+"[Server] Trolololololololololooo";
		case 8: return ChatColor.LIGHT_PURPLE+"[Server] Dude wheres my commands?";
		case 9: return ChatColor.LIGHT_PURPLE+"[Server] Could you turn around real quick. Thanks.";
		case 10: return ChatColor.LIGHT_PURPLE+"[Server] Wub wub wub... weeee woo.. I can do dubstep too.";
		case 11: return ChatColor.LIGHT_PURPLE+"[Server] Can't hear you way down there.";
		case 12: return ChatColor.LIGHT_PURPLE+"[Server] Who is your daddy and what does he do?";
		default: return null;
		}
	}
	@EventHandler
	public void onInventory(InventoryCloseEvent event){
		if(event.getInventory().getTitle().equalsIgnoreCase("BackPack")){
			plugin.backpack.put((Player) event.getPlayer(), event.getInventory());
		}
		if(event.getInventory().getTitle().equalsIgnoreCase("TrashCan")){
			event.getInventory().clear();
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player player = event.getEntity();
		if(player instanceof NPC){
			return;
		}
		event.setDeathMessage(ChatColor.DARK_RED+player.getName()+ChatColor.RED+" died with "+ChatColor.DARK_RED+String.valueOf(player.getLevel())+" kills.");
		plugin.stat.setKillStreak(player, player.getLevel());
		
		int current = plugin.kills.getKills(player.getName());
		int death = plugin.kills.getDeaths(player);
		plugin.kills.setKills(player, current, death+1);
		ArrayList<ItemStack> remove = new ArrayList<ItemStack>();
        for (ItemStack item : event.getDrops()){
        	if(item.getType().equals(Material.DIAMOND_SWORD)){
        		deadsword.put(event.getEntity(), item);
                remove.add(item);
        	}
        }
        for (ItemStack item : remove){
            event.getDrops().remove(item);
        }

	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 5), true);
		if(deadsword.containsKey(event.getPlayer())) event.getPlayer().getInventory().addItem(deadsword.get(event.getPlayer()));
	}
	@EventHandler
	public void onPlayerLevel(PlayerLevelChangeEvent event){
		int current = plugin.kills.getKills(event.getPlayer().getName());
		int death = plugin.kills.getDeaths(event.getPlayer());
		plugin.kills.setKills(event.getPlayer(), current+1, death);
		achievements(event.getPlayer(), event.getNewLevel());
		
	}
	@EventHandler
	public void onPlayerEnterWorld(final PlayerChangedWorldEvent event){
		final Player player = event.getPlayer();
		World from = event.getFrom();
		if(from.getName().equalsIgnoreCase("world") && !player.hasPlayedBefore() && !anotherWorld.contains(player)){
			anotherWorld.add(player);
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 5), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 400, 5), true);
			
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					player.sendMessage(ChatColor.DARK_GRAY+"You've just entered the beginning of this nightmare!");
					player.sendMessage(ChatColor.DARK_GRAY+"Survival is the only way out.");
					player.getInventory().addItem(new ItemStack(276,1));
					player.getInventory().addItem(new ItemStack(278,1));
				}
				
			}, 120L);
		}
		if(!event.getPlayer().getLocation().getChunk().isLoaded()){

			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					event.getPlayer().teleport(event.getPlayer().getLocation().getWorld().getSpawnLocation());
				}
				
			}, 10L);
		}
		
	}
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event){
		String player = event.getPlayer().getName();
		int count = 0;
		if(relogs.containsKey(player)) count = relogs.get(player);
		if(count>=5){
			event.disallow(Result.KICK_OTHER, "Continous Relogs: You must wait 60 secs before reloginning in");
		}
	}
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event){
		final Player player = event.getPlayer();
		if(player.getName().equalsIgnoreCase("scout890")||player.getName().equalsIgnoreCase("barely_mlg")|| player.getName().equalsIgnoreCase("deathmarin")){
			event.setJoinMessage(ChatColor.BLACK+"BlackUp has arrived!.");
		}
		if(!event.getPlayer().getLocation().getChunk().isLoaded()){

			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					event.getPlayer().teleport(event.getPlayer().getLocation().getWorld().getSpawnLocation());
				}
				
			}, 10L);
		}else{
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					if(player.getLocation().getWorld().getName().equalsIgnoreCase("village"))
					player.teleport(player.getLocation().getWorld().getSpawnLocation());
				}
				
			}, 10L);
		}
		plugin.stat.setJoin(player);
		String group = PermissionsEx.getPermissionManager().getUser(player.getName()).getGroupsNames("world")[0];
				if (player.getName().equalsIgnoreCase("deathmarin")){
					player.setPlayerListName(ChatColor.GOLD + "Deathmarine");
					ItemStack item = new ItemStack(Material.ENDER_CHEST, 1);
					item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1000);
					item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1000);
					item.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 1000);
					item.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, 1000);
					item.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, 1000);
					player.getInventory().setHelmet(item);
					return;
				}
				if (group.equalsIgnoreCase("admin")){
					player.setPlayerListName(ChatColor.GOLD + "* " + player.getName() + " *");

					ItemStack item = new ItemStack(Material.getMaterial(91), 1);
					player.getInventory().setHelmet(item);
					return;
				}
				if (group.equalsIgnoreCase("moderator")){
					player.setPlayerListName(ChatColor.RED + player.getName());
					return;
				}
				if (group.equalsIgnoreCase("vip")){
					player.setPlayerListName(ChatColor.GREEN + player.getName());
					return;
				}
				if (group.equalsIgnoreCase("donator")){
					player.setPlayerListName(ChatColor.BLUE + player.getName());
					return;
				}
				if (group.equalsIgnoreCase("regular")){
					player.setPlayerListName(ChatColor.AQUA + player.getName());
					return;
				}
				if (group.equalsIgnoreCase("default")){
					player.setPlayerListName(ChatColor.WHITE + player.getName());
					return;
				}	
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		final Player player = event.getPlayer();
		plugin.backpack.remove(player);
		deadsword.remove(player);
		plugin.stat.setQuit(player);
		int count = 1;
		if(relogs.containsKey(player.getName())) count = relogs.get(player.getName())+1;
		relogs.put(player.getName(), count);
		if(count>=5){
			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

				@Override
				public void run() {
					relogs.remove(player.getName());
				}
				
			}, 120L);
		}
	}
	@EventHandler
	public void onPlayerPickUp(PlayerPickupItemEvent event){
		if(event.getItem().getItemStack().getTypeId()==90){
			event.setCancelled(true);
			if(event.getPlayer().getHealth()<20){
				event.getPlayer().setHealth(20);
				event.getItem().remove();
			}
		}
		if(plugin.item.contains(event.getItem().getEntityId())){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerRightClickEntity(final PlayerInteractEvent event){
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR)||event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			ItemStack hand = event.getPlayer().getItemInHand();
			if(hand.getTypeId() == 367){
				if(hand.getAmount() >= 1){
					event.setCancelled(true);
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, event.getPlayer().getLevel()*10, 2));
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, event.getPlayer().getLevel()*10, 2));
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, event.getPlayer().getLevel()*10, 2));
					if(hand.getAmount() == 1){
						event.getPlayer().setItemInHand(null);
					}else{
						hand.setAmount(hand.getAmount() - 1);
					}
					return;
				}
			}
			if(hand.getTypeId() == 260){
				if(hand.getAmount() >= 1){
					Iterator<PotionEffect> cure = event.getPlayer().getActivePotionEffects().iterator();
					while(cure.hasNext()){
						PotionEffect effect = cure.next();
						event.getPlayer().removePotionEffect(effect.getType());
					}
				}
			}
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(event.getClickedBlock().getState() instanceof Sign){
					Sign sign = (Sign) event.getClickedBlock().getState();
					if(sign.getLine(0).contains("[Stats]")){
						int totalkills = plugin.kills.getKills(event.getPlayer().getName());
						String name = event.getPlayer().getName();
						if(name.equalsIgnoreCase("deathmarin")) name = "Deathmarine";
						sign.setLine(1, ChatColor.DARK_GRAY+name);
						sign.setLine(2, ChatColor.DARK_RED+"TotalKills:");
						sign.setLine(3, ChatColor.DARK_RED+String.valueOf(totalkills));
						sign.update();
					}
					if(sign.getLine(0).contains("[Highest]")){
						int totalkills = plugin.stat.getKillStreak(event.getPlayer().getName());
						String mes;
						if(totalkills==0){
							mes=String.valueOf(event.getPlayer().getLevel());
						}else{
							mes=String.valueOf(totalkills);
						}
						String name = event.getPlayer().getName();
						if(name.equalsIgnoreCase("deathmarin")) name = "Deathmarine";
						sign.setLine(1, ChatColor.DARK_GRAY+name);
						sign.setLine(2, ChatColor.DARK_RED+"HighestStreak:");
						sign.setLine(3, ChatColor.DARK_RED+mes);
						sign.update();
					}

					if(sign.getLine(0).contains("[Champion]")){
						YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
						int totalkills = config.getInt("Champion.Level");
						String mes = String.valueOf(totalkills);
						
						String name = config.getString("Champion.Player");
						if(name.equalsIgnoreCase("deathmarin")) name = "Deathmarine";
						sign.setLine(0, ChatColor.DARK_AQUA+"[Champion]");
						sign.setLine(1, ChatColor.DARK_BLUE+name);
						sign.setLine(2, ChatColor.DARK_BLUE+"TotalLevels:");
						sign.setLine(3, ChatColor.DARK_BLUE+mes);
						sign.update();
					}

					if(sign.getLine(0).contains("[Trade]") && event.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase("world")){
						Player player = event.getPlayer();
						String id = sign.getLine(2);
						String[] lvls = sign.getLine(3).split(" ");
						int lvl = Integer.parseInt(lvls[1].trim());
						if(event.getPlayer().getLevel()>=lvl){
							ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(id)), 1);
							player.setLevel(event.getPlayer().getLevel()-lvl);
							player.getLocation().getWorld().dropItemNaturally(event.getPlayer().getLocation(), item);
							//player.getInventory().addItem(item);
							player.sendMessage(ChatColor.GREEN+"Traded "+sign.getLine(1)+" for "+lvls[1].trim()+" Levels.");
						}else{
							player.sendMessage(ChatColor.RED+"You don't have enough levels to do that.");
						}
					}
				}
				if(event.getPlayer().getItemInHand().getType().equals(Material.WOOD_AXE) && event.getPlayer().isOp()){
					plugin.setLocation(event.getClickedBlock().getLocation().getBlock().getRelative(BlockFace.UP).getLocation());
					event.getPlayer().sendMessage(ChatColor.BLUE+"Block selected.");
				}
			}
		}
		if(event.getAction().equals(Action.LEFT_CLICK_AIR)){
			if(event.getPlayer().getItemInHand().getType().equals(Material.FISHING_ROD)&&event.getPlayer().getLevel()>=2000){
				if(cooldown.contains(event.getPlayer())){
				event.getPlayer().sendMessage(ChatColor.BLUE+"You must wait 15 Secs before using again");
				return;
				}
				if(event.getPlayer().getTargetBlock(null, 10).getType().equals(Material.AIR)){
					event.getPlayer().sendMessage("Block is too far away.");
				}
				Location newblock = event.getPlayer().getTargetBlock(null, 10).getRelative(BlockFace.UP).getLocation();
				event.getPlayer().teleport(new Location(newblock.getWorld(), newblock.getX(),newblock.getY(),newblock.getZ(),event.getPlayer().getLocation().getYaw(), event.getPlayer().getLocation().getPitch()));
				cooldown.add(event.getPlayer());
				plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

					@Override
					public void run() {
						if(cooldown.contains(event.getPlayer()))cooldown.remove(event.getPlayer());
					}
					
				}, 300L);
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent event){
		if(event.getRightClicked() instanceof Player){
			Player player = (Player) event.getRightClicked();
			event.getPlayer().sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+""+ChatColor.ITALIC+"--- Current Stats of "+player.getName()+" ---");
			int totalkills = plugin.stat.getKillStreak(player.getName());
			event.getPlayer().sendMessage(ChatColor.GOLD+"HighestStreak: "+String.valueOf(totalkills));
			int kills = plugin.kills.getKills(player.getName());
			event.getPlayer().sendMessage(ChatColor.GOLD+"TotalKills: "+String.valueOf(kills));
		}
	}
	public void achievements(Player player, Integer amt){
		if(amt==500){
			player.sendMessage(ChatColor.GOLD+"LVL500: Grenade use enable!");
			player.sendMessage(ChatColor.GOLD+"Splash potions are now explosive!");
		}
		if(amt==1000){
			player.sendMessage(ChatColor.GOLD+"LVL1000: SprintJump Boost, TNT Enable");
			player.sendMessage(ChatColor.GOLD+"You now get a boost to your run.");
			player.sendMessage(ChatColor.GOLD+"You can now use tnt.");
		}
		if(amt==1500){
			player.sendMessage(ChatColor.GOLD+"LVL1500: Villagers can be summoned!");
			player.sendMessage(ChatColor.GOLD+"Use /decoy to purchase for 500 levels.");
			player.sendMessage(ChatColor.GOLD+"Grenade power upgraded.");			
		}
		if(amt==2000){
			player.sendMessage(ChatColor.GOLD+"LVL2000: Prepare your arms. The Horde Rides Tonight!");
			player.sendMessage(ChatColor.GOLD+"You now inflict and receive double damage.");
			player.sendMessage(ChatColor.GOLD+"Fishing Polls can be used as a grapling hook.");
		}
		if(amt==3000){
			player.sendMessage(ChatColor.GOLD+"LVL3000: You can now instabreak bedrock.");
			player.sendMessage(ChatColor.GOLD+"Time to fortify.");
			player.sendMessage(ChatColor.GOLD+"Grenade power upgraded.");			
		}
		if(amt==4000){
			player.sendMessage(ChatColor.GOLD+"LVL4000: Woah. Ummm... Record maybe.");
			player.sendMessage(ChatColor.GOLD+"Best not be cheatin, we log this sorta thing.");
			player.sendMessage(ChatColor.GOLD+"You can now build in the city world.");		
		}
		if(amt==5000){
			player.sendMessage(ChatColor.GOLD+"LVL5000: HAX!!!!");
			player.sendMessage(ChatColor.GOLD+"Lies... all Lies... I've got to text the boss.");
		}
		if(amt>=10000){
			plugin.getServer().broadcastMessage(ChatColor.AQUA+player.getName()+" made it to 10000 levels!");
			player.sendMessage(ChatColor.GOLD+"LVL10000: Impossible Gameover!");
			player.sendMessage(ChatColor.GOLD+"Originally this would shutdown the server.");
			player.sendMessage(ChatColor.GOLD+"But I hope this sword will work instead.");
			PermissionsEx.getPermissionManager().getUser(player.getName()).setPrefix(ChatColor.GOLD+"{Champion} ", null);
			ItemStack item = new ItemStack(Material.DIAMOND_SWORD, 1);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 25);
			item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 25);
			item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 25);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 25);
			item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 25);
			player.setItemInHand(item);
			plugin.saveConfig();			
		}
	}
	@EventHandler
	public void onPlayerSprint(PlayerToggleSprintEvent event){
		if(event.isSprinting() && event.getPlayer().getLevel()>=1000){
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5000, 2), true);
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5000, 2), true);
		}else{
			event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
			event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
		}
		
	}
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event){
		if(event.getBlockClicked().getType().equals(Material.LAVA)||event.getBlockClicked().getType().equals(Material.STATIONARY_LAVA)){
			event.getPlayer().playEffect(event.getPlayer().getLocation(), Effect.SMOKE, 0);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		String message = event.getMessage();
		if(!message.equalsIgnoreCase(spamcheck)){
			 spamcheck = event.getMessage();
			 spamCount = 0;
		 }else{
			 event.setCancelled(true);
			 spamCount++;
		 }
		if(5 < spamCount){
			String fakecmd = "ban " + player.getName() + " " + " Ultrabans AutoMated: Spam";
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), fakecmd);
			
		}
	}
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event){
		if(event.getBucket().equals(Material.LAVA_BUCKET)){
			event.getPlayer().playEffect(event.getPlayer().getLocation(), Effect.SMOKE, 0);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.getTo().getY()<1){
			event.getPlayer().setFireTicks(0);
			event.getPlayer().teleport(plugin.getServer().getWorld("world").getSpawnLocation());
		}
	}
	@EventHandler
	public void onWorldChange(PlayerPortalEvent event){
		String test = event.getTo().getWorld().getName();
		if(test.equalsIgnoreCase("world")){
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			return;
		}
		if(test.equalsIgnoreCase("city")){
			if(event.getPlayer().getLevel()<4000){
				event.getPlayer().setGameMode(GameMode.ADVENTURE);
				return;
			}else{
				event.getPlayer().setGameMode(GameMode.SURVIVAL);
				return;				
			}
		}
		if(test.equalsIgnoreCase("oilrig")){
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			return;
		}
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
		
	}
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		String test = event.getTo().getWorld().getName();
		if(test.equalsIgnoreCase("world")){
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			return;
		}
		if(test.equalsIgnoreCase("city")){
			if(event.getPlayer().getLevel()<4000){
				event.getPlayer().setGameMode(GameMode.ADVENTURE);
				return;
			}else{
				event.getPlayer().setGameMode(GameMode.SURVIVAL);
				return;				
			}
		}
		if(test.equalsIgnoreCase("oilrig")){
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			return;
		}
		event.getPlayer().setGameMode(GameMode.SURVIVAL);
	}
}
