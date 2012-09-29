package com.modcrafting.nightofthelivingdead.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.modcrafting.nightofthelivingdead.utils.Errors;

public class Statsdb {
	NightOfTheLivingDead plugin;
	public Statsdb(NightOfTheLivingDead instance) {
		plugin = instance;
	}
	public String table = "joshmod_stats";
	public String createTable = "CREATE TABLE IF NOT EXISTS `joshmod_stats` (" +
			"`player` varchar(32) NOT NULL," +
			"`highestkillstreak` bigint(20)," + 
			"`lastlogin` bigint(20) NOT NULL," +
			"`playertime` bigint(20) NOT NULL," +
			"PRIMARY KEY (`player`)" + 
			") ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;";

	public void setJoin(Player player){
		Connection conn = null;
		PreparedStatement ps = null;
		int streak = getKillStreak(player.getName());
		long pltime = getPlayTime(player);
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("REPLACE INTO " + table + " (player,highestkillstreak,lastlogin,playertime) VALUES(?,?,?,?)");
			ps.setString(1, player.getName().toLowerCase());
			ps.setInt(2, streak);
			ps.setLong(3, System.currentTimeMillis()/1000);
			ps.setLong(4, pltime);
			ps.executeUpdate();
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
		return;	
		
	}
	public void setQuit(Player player){
		Connection conn = null;
		PreparedStatement ps = null;
		int streak = getKillStreak(player.getName());
		long pltime = getQuitTime(player);
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("REPLACE INTO " + table + " (player,highestkillstreak,lastlogin,playertime) VALUES(?,?,?,?)");
			ps.setString(1, player.getName().toLowerCase());
			ps.setInt(2, streak);
			ps.setLong(3, System.currentTimeMillis()/1000);
			ps.setLong(4, pltime);
			ps.executeUpdate();
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
		return;	
		
	}
	public long getQuitTime(Player player){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ps.setString(1, player.getName().toLowerCase());
			rs = ps.executeQuery();
			while (rs.next()){
				long time = rs.getLong("lastlogin");
				long pltime = rs.getLong("playertime");
				return pltime+((System.currentTimeMillis()/1000)-time);
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose());
			}
		}
		return 0;	
	}

	public long getPlayTime(Player player){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ps.setString(1, player.getName().toLowerCase());
			rs = ps.executeQuery();
			while (rs.next()){
				return rs.getLong("playertime");
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose());
			}
		}
		return 0;	
	}
	public void setKillStreak(Player player, Integer streak){
		if(streak<getKillStreak(player.getName()))return;
		Connection conn = null;
		PreparedStatement ps = null;
		long pltime = 0;
		if(player.hasPlayedBefore()) pltime = getPlayTime(player);
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("REPLACE INTO " + table + " (player,highestkillstreak,lastlogin,playertime) VALUES(?,?,?,?)");
			ps.setString(1, player.getName().toLowerCase());
			ps.setInt(2, streak);
			ps.setLong(3, System.currentTimeMillis()/1000);
			ps.setLong(4, pltime);
			ps.executeUpdate();
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
			}
		}
		return;	
		
	}

	public int getKillStreak(String string){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ps.setString(1, string.toLowerCase());
			rs = ps.executeQuery();
			while (rs.next()){
				return rs.getInt("highestkillstreak");
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose());
			}
		}
		return 0;	
	}
}
