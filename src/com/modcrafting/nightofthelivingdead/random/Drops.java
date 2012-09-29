package com.modcrafting.nightofthelivingdead.random;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class Drops {
	Random gen = new Random();
	public Material diamondArmor(int num){
		switch(num){
		case 1: return Material.DIAMOND_HELMET;
		case 2: return Material.DIAMOND_CHESTPLATE;
		case 3: return Material.DIAMOND_LEGGINGS;
		case 4: return Material.DIAMOND_BOOTS;
		default: return null;
		}
	}
	public Material ironArmor(int num){
		switch(num){
		case 1: return Material.IRON_HELMET;
		case 2: return Material.IRON_CHESTPLATE;
		case 3: return Material.IRON_LEGGINGS;
		case 4: return Material.IRON_BOOTS;
		default: return null;
		}
	}
	public Material chainmailArmor(int num){
		switch(num){
		case 1: return Material.CHAINMAIL_HELMET;
		case 2: return Material.CHAINMAIL_CHESTPLATE;
		case 3: return Material.CHAINMAIL_LEGGINGS;
		case 4: return Material.CHAINMAIL_BOOTS;
		default: return null;
		}
	}
	public Material goldArmor(int num){
		switch(num){
		case 1: return Material.GOLD_HELMET;
		case 2: return Material.GOLD_CHESTPLATE;
		case 3: return Material.GOLD_LEGGINGS;
		case 4: return Material.GOLD_BOOTS;
		default: return null;
		}
	}
	public Material leatherArmor(int num){
		switch(num){
		case 1: return Material.LEATHER_HELMET;
		case 2: return Material.LEATHER_CHESTPLATE;
		case 3: return Material.LEATHER_LEGGINGS;
		case 4: return Material.LEATHER_BOOTS;
		default: return null;
		}
	}
	public Material armorPicker(int five, int four){
		switch(five){
		case 1: return diamondArmor(four);
		case 2: return ironArmor(four);
		case 3: return chainmailArmor(four);
		case 4: return goldArmor(four);
		case 5: return leatherArmor(four);
		default: return null;
		}
	}
	public Material diamondWeapon(int num){
		switch(num){
		case 1: return Material.DIAMOND_SWORD;
		case 2: return Material.DIAMOND_PICKAXE;
		case 3: return Material.DIAMOND_SPADE;
		case 4: return Material.DIAMOND_AXE;
		case 5: return Material.DIAMOND_HOE;
		default: return null;
		}
	}
	public Material ironWeapon(int num){
		switch(num){
		case 1: return Material.IRON_SWORD;
		case 2: return Material.IRON_PICKAXE;
		case 3: return Material.IRON_SPADE;
		case 4: return Material.IRON_AXE;
		case 5: return Material.IRON_HOE;
		default: return null;
		}
	}
	public Material goldWeapon(int num){
		switch(num){
		case 1: return Material.GOLD_SWORD;
		case 2: return Material.GOLD_PICKAXE;
		case 3: return Material.GOLD_SPADE;
		case 4: return Material.GOLD_AXE;
		case 5: return Material.GOLD_HOE;
		default: return null;
		}
	}
	public Material woodWeapon(int num){
		switch(num){
		case 1: return Material.WOOD_SWORD;
		case 2: return Material.WOOD_PICKAXE;
		case 3: return Material.WOOD_SPADE;
		case 4: return Material.WOOD_AXE;
		case 5: return Material.WOOD_HOE;
		default: return null;
		}
	}
	public Material weaponPicker(int five, int fiv){
		switch(five){
		case 1: return diamondWeapon(fiv);
		case 2: return ironWeapon(fiv);
		case 3: return goldWeapon(fiv);
		case 4: return woodWeapon(fiv);
		case 5: return Material.BOW;
		default: return null;
		}
	}
	public Material selector(int two, int five, int four, int fiv){
		switch(two){
		case 1: return armorPicker(five,four);
		case 2: return weaponPicker(five, fiv);
		default: return null;
		}
	}
	public Enchantment enchant(int num){
		switch(num){
			case 1: return Enchantment.DAMAGE_ALL; //Weapons Only
			case 2: return Enchantment.DAMAGE_ARTHROPODS; //Weapons Only
			case 3: return Enchantment.DAMAGE_UNDEAD; //Weapons Only
			case 4: return Enchantment.DIG_SPEED; //Tools except Hoe.
			case 5: return Enchantment.DURABILITY; //Tools except Hoe.
			case 6: return Enchantment.FIRE_ASPECT; //Weapons Only
			case 7: return Enchantment.KNOCKBACK; //Weapons Only
			case 8: return Enchantment.LOOT_BONUS_BLOCKS; //Tools except Hoe.
			case 9: return Enchantment.LOOT_BONUS_MOBS; //Weapons Only
			case 10: return Enchantment.OXYGEN; //Armor Helmet Only
			case 11: return Enchantment.PROTECTION_ENVIRONMENTAL; //All Armor
			case 12: return Enchantment.PROTECTION_EXPLOSIONS; //All Armor
			case 13: return Enchantment.PROTECTION_FALL; //All Armor
			case 14: return Enchantment.PROTECTION_FIRE; //All Armor
			case 15: return Enchantment.PROTECTION_PROJECTILE; //All Armor
			case 16: return Enchantment.SILK_TOUCH; //Tools except Hoe
			case 17: return Enchantment.WATER_WORKER; //Armor Helmet Only
			case 18: return Enchantment.ARROW_DAMAGE;
			case 19: return Enchantment.ARROW_FIRE;
			case 20: return Enchantment.ARROW_KNOCKBACK;
			case 21: return Enchantment.ARROW_INFINITE;
			default: return null;
		}
	}
}
