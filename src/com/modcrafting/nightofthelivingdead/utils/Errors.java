package com.modcrafting.nightofthelivingdead.utils;

import org.bukkit.ChatColor;

public class Errors {
	public static String commandSyntax() {
		return ChatColor.RED+"ERROR: Command Syntax Invalid";
	}
	public static String playerNull() {
		return ChatColor.RED+"ERROR: Player Not Found";
	}
	public static String failSetHome() {
		return ChatColor.RED+"ERROR: Unable to set Home";
	}
	public static String sqlConnectionExecute(){
		return "Couldn't execute MySQL statement: ";
	}
	public static String sqlConnectionClose(){
		return "Failed to close MySQL connection: ";
	}
	public static String noSQLConnection(){
		return "Unable to retreive MYSQL connection: ";
	}
	public static String noTableFound(){
		return "Database Error: No Table Found";
	}
}
