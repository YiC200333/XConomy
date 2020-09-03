package me.YiC.XConomy.data;

import com.zaxxer.hikari.HikariDataSource;
import me.YiC.XConomy.XConomy;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseConnection {
	private static final String driverA = "com.mysql.jdbc.Driver";
	//============================================================================================
	private static final File dataFolder = new File(XConomy.getInstance().getDataFolder(), "playerdata");
	private static final String url = "jdbc:mysql://" + XConomy.config.getString("MySQL.host") + "/"
			+ XConomy.config.getString("MySQL.database") + "?characterEncoding=utf-8&useSSL=false";
	private static final String username = XConomy.config.getString("MySQL.user");
	private static final String password = XConomy.config.getString("MySQL.pass");
	private static final Integer maxPoolSize = XConomy.config.getInt("Pool-Settings.maximum-pool-size");
	private static final Integer minIdle = XConomy.config.getInt("Pool-Settings.minimum-idle");
	private static final Integer maxLife = XConomy.config.getInt("Pool-Settings.maximum-lifetime");
	private static final Long idleTime = XConomy.config.getLong("Pool-Settings.idle-timeout");
	private static boolean secon = false;
	//============================================================================================
	private static final String driverB = "org.sqlite.JDBC";
	public static File userdata = new File(dataFolder, "data.db");
	//============================================================================================
	private Connection connection = null;
	private HikariDataSource hikari = null;

	private void createNewHikariConfiguration() {
		hikari = new HikariDataSource();
		hikari.setPoolName("XConomy");
		hikari.setJdbcUrl(url);
		hikari.setUsername(username);
		hikari.setPassword(password);
		hikari.setMaximumPoolSize(maxPoolSize);
		hikari.setMinimumIdle(minIdle);
		hikari.setMaxLifetime(maxLife);
		hikari.addDataSourceProperty("cachePrepStmts", "true");
		hikari.addDataSourceProperty("prepStmtCacheSize", "250");
		hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
			hikari.setIdleTimeout(idleTime);
		} else {
			hikari.setIdleTimeout(0);
		}
	}

	public boolean setGlobalConnection() {
		try {
			if (XConomy.allowHikariConnectionPooling()) {
				createNewHikariConfiguration();
				Connection connection = getConnection();
				closeHikariConnection(connection);
			} else {
				if (XConomy.config.getBoolean("Settings.mysql")) {
					Class.forName(driverA);
					connection = DriverManager.getConnection(url, username, password);
				} else {
					Class.forName(driverB);
					connection = DriverManager.getConnection("jdbc:sqlite:" + userdata.toString());
				}
			}

			if (XConomy.config.getBoolean("Settings.mysql")) {
				if (secon) {
					XConomy.getInstance().logger("MySQL重新连接成功");
				} else {
					secon = true;
				}
			}
			return true;

		} catch (SQLException e) {
			XConomy.getInstance().logger("无法连接到数据库-----");
			e.printStackTrace();
			close();
			return false;

		} catch (ClassNotFoundException e) {
			XConomy.getInstance().logger("JDBC驱动加载失败");
		}

		return false;
	}

	public Connection getConnection() {
		if (!canConnect()) {
			return null;
		}

		try {
			if (XConomy.allowHikariConnectionPooling()) {
				return hikari.getConnection();
			} else {
				return connection;
			}
		} catch (SQLException e1) {
			XConomy.getInstance().logger("无法连接到数据库-----");
			close();
			e1.printStackTrace();
			return null;
		}
	}

	public boolean canConnect() {
		try {
			if (XConomy.allowHikariConnectionPooling()) {
				if (hikari == null) {
					return setGlobalConnection();
				}

				if (hikari.isClosed()) {
					return setGlobalConnection();
				}

			} else {
				if (connection == null) {
					return setGlobalConnection();
				}

				if (connection.isClosed()) {
					return setGlobalConnection();
				}
			}
		} catch (SQLException e) {
			Arrays.stream(e.getStackTrace()).forEach(d -> Bukkit.getLogger().info(d.toString()));
			return false;
		}
		return true;
	}

	public void closeHikariConnection(Connection connection) {
		if (!XConomy.allowHikariConnectionPooling()) {
			return;
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
			if (hikari != null) {
				hikari.close();
			}
		} catch (SQLException e) {
			if (XConomy.config.getBoolean("Settings.mysql")) {
				XConomy.getInstance().logger("MySQL连接断开失败");
			}
			e.printStackTrace();
		}
	}
}
