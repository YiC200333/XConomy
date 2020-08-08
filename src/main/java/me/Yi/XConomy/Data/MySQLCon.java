package me.Yi.XConomy.Data;

import java.sql.SQLException;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;

import me.Yi.XConomy.XConomy;

public class MySQLCon {
	private static String Driver = "com.mysql.jdbc.Driver";
	private static String Url = "jdbc:mysql://" + XConomy.config.getString("MySQL.host") + "/"
			+ XConomy.config.getString("MySQL.database") + "?characterEncoding=utf-8&useSSL=false";
	private static String User = XConomy.config.getString("MySQL.user");
	private static String Pass = XConomy.config.getString("MySQL.pass");
	private static Integer maxpsize = XConomy.config.getInt("Pool-Settings.maximum-pool-size");
	private static Integer minidle = XConomy.config.getInt("Pool-Settings.minimum-idle");
	private static Integer maxlife = XConomy.config.getInt("Pool-Settings.maximum-lifetime");
	private static Long idletime = XConomy.config.getLong("Pool-Settings.idle-timeout");
//============================================================================================
	private Connection conn = null;
	private HikariDataSource hikari = null;
	private static boolean secon = false;

	private void fnew() throws SQLException {
		hikari = new HikariDataSource();
		hikari.setPoolName("XConomy");
		hikari.setJdbcUrl(Url);
		hikari.setUsername(User);
		hikari.setPassword(Pass);
		hikari.addDataSourceProperty("cachePrepStmts", "true");
		hikari.addDataSourceProperty("prepStmtCacheSize", "250");
		hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		hikari.setMaximumPoolSize(maxpsize);
		hikari.setMinimumIdle(minidle);
		hikari.setMaxLifetime(maxlife);
		if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
			hikari.setIdleTimeout(idletime);
		} else if (hikari.getMinimumIdle() == hikari.getMaximumPoolSize()) {
			hikari.setIdleTimeout(0);
		}
	}

	public boolean con() {
		try {
			if (XConomy.config.getBoolean("MySQL.usepool")) {
				fnew();
				Connection co = getcon();
				closep(co);
			} else {
				Class.forName(Driver);
				conn = DriverManager.getConnection(Url, User, Pass);
			}
			if (secon) {
				XConomy.getInstance().logger("MySQL重新连接成功");
			} else {
				secon = true;
			}
			return true;
		} catch (SQLException e) {
			XConomy.getInstance().logger("无法连接到数据库-----");
			XConomy.getInstance().logger(e.getMessage());
			return false;
		} catch (ClassNotFoundException e) {
			XConomy.getInstance().logger("JDBC驱动加载失败");
		}
		return false;
	}

	public Connection getcon() {
		if (checkcon()) {
			try {
				if (XConomy.config.getBoolean("MySQL.usepool")) {
					return hikari.getConnection();
				} else {
					return conn;
				}
			} catch (SQLException e1) {
				if (XConomy.config.getBoolean("MySQL.usepool")) {
					hikari.close();
					if (checkcon()) {
						try {
							return hikari.getConnection();
						} catch (SQLException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
				}
				e1.printStackTrace();
			}
		}
		return null;
	}

	public boolean checkcon() {
		try {
			if (XConomy.config.getBoolean("MySQL.usepool")) {
				if (hikari == null) {
					return con();
				}
				if (hikari.isClosed() == true) {
					return con();
				}
			} else {
				if (conn == null) {
					return con();
				}
				if (conn.isClosed() == true) {
					return con();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void closep(Connection co) {
		if (XConomy.config.getBoolean("MySQL.usepool")) {
			try {
				hikari.evictConnection(co);
				co.close();
			} catch (SQLException e) {
				// // TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
		}
	}

	public void close() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (hikari != null) {
				hikari.close();
			}
		} catch (SQLException e) {
			XConomy.getInstance().logger("MySQL连接断开失败");
		}
	}
}
