package com.modcrafting.nightofthelivingdead.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import com.modcrafting.nightofthelivingdead.NightOfTheLivingDead;
import com.modcrafting.nightofthelivingdead.utils.Errors;

public class SQL {
	NightOfTheLivingDead plugin;
	public SQL(NightOfTheLivingDead instance){
		plugin = instance;
	}
	public Connection getSQLConnection() {
			YamlConfiguration config = (YamlConfiguration) plugin.getConfig();
			String mysqlDatabase = config.getString("mysql-database","jdbc:mysql://localhost:3306/minecraft");
			String mysqlUser = config.getString("mysql-user","root");
			String mysqlPassword = config.getString("mysql-password","root");
			try {
				return DriverManager.getConnection(mysqlDatabase + "?autoReconnect=true&user=" + mysqlUser + "&password=" + mysqlPassword);
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.noSQLConnection(), ex);
			}
			return null;
	}
	public void initialize(String table, String create){
		Connection conn = getSQLConnection();
		if(conn != null){
			try{
				Statement s = conn.createStatement();
				s.executeUpdate (create);
				s.close ();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.noSQLConnection(), ex);
            }
		}else{
			plugin.getLogger().log(Level.SEVERE, Errors.noSQLConnection());
		}
	}
}
