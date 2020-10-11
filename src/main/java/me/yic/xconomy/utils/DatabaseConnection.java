package me.yic.xconomy.utils;

import com.zaxxer.hikari.HikariDataSource;
import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class DatabaseConnection {
	private String driver = "com.mysql.jdbc.Driver";
	//============================================================================================
	private static final File dataFolder = new File(XConomy.getInstance().getDataFolder(), "playerdata");
	private static String url = "jdbc:mysql://" + XConomy.config.getString("MySQL.host") + "/"
			+ XConomy.config.getString("MySQL.database") + "?characterEncoding="
			+ XConomy.config.getString("MySQL.encoding") + "&useSSL=false";
	private static final String username = XConomy.config.getString("MySQL.user");
	private static final String password = XConomy.config.getString("MySQL.pass");
	private static final Integer maxPoolSize = XConomy.config.getInt("Pool-Settings.maximum-pool-size");
	private static final Integer minIdle = XConomy.config.getInt("Pool-Settings.minimum-idle");
	private static final Integer maxLife = XConomy.config.getInt("Pool-Settings.maximum-lifetime");
	private static final Long idleTime = XConomy.config.getLong("Pool-Settings.idle-timeout");
	private static boolean secon = false;
	//============================================================================================
	public static File userdata = new File(dataFolder, "data.db");
	//============================================================================================
	private Connection connection = null;
	private HikariDataSource hikari = null;
	private boolean isfirstry = true;

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
		hikari.addDataSourceProperty("userServerPrepStmts", "true");
		if (XConomy.ddrivers){
			hikari.setDriverClassName(driver);
		}
		if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
			hikari.setIdleTimeout(idleTime);
		} else {
			hikari.setIdleTimeout(0);
		}
	}

	private void setDriver() {
		if (XConomy.ddrivers){
			if (XConomy.config.getBoolean("Settings.mysql")) {
				driver = ("me.yic.libs.mysql.cj.jdbc.Driver");
			}else{
				driver = ("me.yic.libs.sqlite.JDBC");
			}
		}else{
			if (XConomy.config.getBoolean("Settings.mysql")) {
				driver = ("com.mysql.jdbc.Driver");
			}else{
				driver = ("org.sqlite.JDBC");
			}
		}
	}

	private void setTimezone() {
		if (!XConomy.config.getString("MySQL.timezone").equals("")){
			url = url+"&serverTimezone="+XConomy.config.getString("MySQL.timezone");
		}
	}

	public boolean setGlobalConnection() {
		setTimezone();
		setDriver();
		try {
			if (XConomy.allowHikariConnectionPooling()) {
				createNewHikariConfiguration();
				Connection connection = getConnection();
				closeHikariConnection(connection);
			} else {
				Class.forName(driver);
				if (XConomy.config.getBoolean("Settings.mysql")) {
					connection = DriverManager.getConnection(url, username, password);
				} else {
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

	public Connection getConnectionAndCheck() {
		if (!canConnect()) {
			return null;
		}
		try {
			return getConnection();
		} catch (SQLException e1) {
			if (isfirstry) {
				isfirstry = false;
				close();
				return getConnectionAndCheck();
			}else{
				isfirstry = true;
				XConomy.getInstance().logger("无法连接到数据库-----");
				close();
				e1.printStackTrace();
				return null;
			}
		}
	}

	public Connection getConnection() throws SQLException {
		if (XConomy.allowHikariConnectionPooling()) {
			return hikari.getConnection();
		} else {
			return connection;
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
