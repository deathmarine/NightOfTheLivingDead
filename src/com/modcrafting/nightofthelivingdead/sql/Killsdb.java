package com.modcrafting.nightofthelivingdead.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.modcrafting.nightofthelivingdead.utils.Errors;

public class Killsdb {
	NightOfTheLivingDead plugin;
	public Killsdb(NightOfTheLivingDead instance) {
		plugin = instance;
	}
	public String table = "joshmod_kills";
	public String createTable = "CREATE TABLE IF NOT EXISTS `joshmod_kills` (" +
			"`player` varchar(32) NOT NULL," +
			"`world` text NOT NULL," + 
			"`kills` int(11) NOT NULL," +
			"`deaths` int(11)," +
			"PRIMARY KEY (`player`)" + 
			") ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;";
	public Integer getKills(String string) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ps.setString(1, string.toLowerCase());
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("player").equalsIgnoreCase(string.toLowerCase())){
					return rs.getInt("kills");
				}
			}
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
		return 0;	
	}

	public void setKills(Player player, Integer kills, Integer deaths) {
		Location loc = player.getLocation();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("REPLACE INTO " + table + " (player,world,kills,deaths) VALUES(?,?,?,?)");
			ps.setString(1, player.getName().toLowerCase());
			ps.setString(2, loc.getWorld().getName());
			ps.setInt(3, kills);
			ps.setInt(4, deaths);
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
	public Integer getDeaths(Player player) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
			ps.setString(1, player.getName().toLowerCase());
			rs = ps.executeQuery();
			while(rs.next()){
				if(rs.getString("player").equalsIgnoreCase(player.getName().toLowerCase())){
					return rs.getInt("deaths");
				}
			}
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
		return 0;	
	}

	public void setDeaths(Player player, Integer kills, Integer deaths) {
		Location loc = player.getLocation();
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("REPLACE INTO " + table + " (player,world,kills,deaths) VALUES(?,?,?,?)");
			ps.setString(1, player.getName().toLowerCase());
			ps.setString(2, loc.getWorld().getName());
			ps.setInt(3, kills);
			ps.setInt(4, deaths);
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
	
}