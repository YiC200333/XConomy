package me.yic.xconomy.data;

import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.NonPlayerCache;
import me.yic.xconomy.message.Messages;
import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;

public class SQL {

	public static String tableName = "xconomy";
	public static String tableNonPlayerName = "xconomynon";
	public final static DatabaseConnection database = new DatabaseConnection();
	private static final Double ibal = XConomy.config.getDouble("Settings.initial-bal");

	public static boolean con() {
		return database.setGlobalConnection();
	}

	public static void close() {
		database.close();
	}

	public static void createTable() {
		try {
			Connection connection = database.getConnection();
			Statement statement = connection.createStatement();

			if (statement == null) {
				return;
			}

			String query1;
			String query2;
			if (XConomy.config.getBoolean("Settings.mysql")) {
				query1 = "CREATE TABLE IF NOT EXISTS " + tableName
						+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (UID)) DEFAULT CHARSET = utf8;";
				query2 = "CREATE TABLE IF NOT EXISTS " + tableNonPlayerName
						+ "(account varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (account)) DEFAULT CHARSET = utf8;";
			} else {
				query1 = "CREATE TABLE IF NOT EXISTS " + tableName
						+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (UID));";
				query2 = "CREATE TABLE IF NOT EXISTS " + tableNonPlayerName
						+ "(account varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (account));";
			}

			statement.executeUpdate(query1);
			if (XConomy.config.getBoolean("Settings.non-player-account")) {
				statement.executeUpdate(query2);
			}
			statement.close();
			database.closeHikariConnection(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void newPlayer(Player player) {
		Connection connection = database.getConnection();
		checkUser(player, connection);
		selectUser(player.getUniqueId().toString(), player.getName(), connection);
		database.closeHikariConnection(connection);
	}

	private static void createAccount(String UID, String user, Double amount, Connection co_a) {
		try {
			String query;
			if (XConomy.config.getBoolean("Settings.mysql")) {
				query = "INSERT INTO " + tableName + "(UID,player,balance) values(?,?,?) "
						+ "ON DUPLICATE KEY UPDATE UID = ?";
			} else {
				query = "INSERT INTO " + tableName + "(UID,player,balance) values(?,?,?) ";
			}

			PreparedStatement statement = co_a.prepareStatement(query);
			statement.setString(1, UID);
			statement.setString(2, user);
			statement.setDouble(3, amount);

			if (XConomy.config.getBoolean("Settings.mysql")) {
				statement.setString(4, UID);
			}

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void createNonPlayerAccount(String account, Double bal, Connection co) {
		try {
			String query;
			if (XConomy.config.getBoolean("Settings.mysql")) {
				query = "INSERT INTO " + tableNonPlayerName + "(account,balance) values(?,?) "
						+ "ON DUPLICATE KEY UPDATE account = ?";
			} else {
				query = "INSERT INTO " + tableNonPlayerName + "(account,balance) values(?,?)";
			}

			PreparedStatement statement = co.prepareStatement(query);
			statement.setString(1, account);
			statement.setDouble(2, bal);

			if (XConomy.config.getBoolean("Settings.mysql")) {
				statement.setString(3, account);
			}

			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void updateUser(String UID, String user, Connection co_a) {
		try {
			PreparedStatement statement = co_a.prepareStatement("update " + tableName + " set player = ? where UID = ?");
			statement.setString(1, user);
			statement.setString(2, UID);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void save(String UID, Double amount, Boolean isAdd) {
		try {
			Connection connection = database.getConnection();
			String query;

			if (isAdd == null) {
				query = " set balance = " + amount + " where UID = ?";
			} else if (isAdd) {
				query = " set balance = balance + " + amount + " where UID = ?";
			} else {
				query = " set balance = balance - " + amount + " where UID = ?";
			}

			PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
			statement.setString(1, UID);
			statement.executeUpdate();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveNonPlayer(String account, Double amount, Boolean isAdd) {
		try {
			Connection connection = database.getConnection();
			String query;
			if (isAdd) {
				query = " set balance = balance + " + amount + " where account = ?";
			} else {
				query = " set balance = balance - " + amount + " where account = ?";
			}
			PreparedStatement statement = connection.prepareStatement("update " + tableNonPlayerName + query);
			statement.setString(1, account);
			statement.executeUpdate();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void select(UUID uuid) {
		try {
			Connection connection = database.getConnection();
			PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where UID = ?");
			statement.setString(1, uuid.toString());

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				BigDecimal cacheThisAmt = DataFormat.formatString(rs.getString(3));
				Cache.insertIntoCache(uuid, cacheThisAmt);
			}

			rs.close();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void selectNonPlayer(String playerName) {
		try {
			Connection connection = database.getConnection();
			String query;

			if (XConomy.config.getBoolean("Settings.mysql")) {
				query = "select * from " + tableNonPlayerName + " where binary account = ?";
			} else {
				query = "select * from " + tableNonPlayerName + " where account = ?";
			}

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, playerName);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				NonPlayerCache.insertIntoCache(playerName, DataFormat.formatString(rs.getString(2)));
			} else {
				createNonPlayerAccount(playerName, 0.0, connection);
				NonPlayerCache.insertIntoCache(playerName, DataFormat.formatString("0.0"));
			}

			rs.close();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void checkUser(Player player, Connection connection) {
		try {
			String query;

			if (XConomy.config.getBoolean("Settings.mysql")) {
				query = "select * from " + tableName + " where binary player = ?";
			} else {
				query = "select * from " + tableName + " where player = ?";
			}

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, player.getName());

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				if (!player.getUniqueId().toString().equals(rs.getString(1))) {
					if (player.isOnline()) {
						Bukkit.getScheduler().runTask(XConomy.getInstance(), () ->
								player.kickPlayer("[XConomy] The player with the same name exists on the server"));
					}
				}
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void selectUser(String UID, String name, Connection connection) {
		String user = "#";

		try {
			PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " where UID = ?");
			statement.setString(1, UID);
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				user = rs.getString(2);
			} else {
				user = name;
				createAccount(UID, user, ibal, connection);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!user.equals(name)&&!user.equals("#")) {
			updateUser(UID, name, connection);
			XConomy.getInstance().logger(name + Messages.systemMessage(" 名称已更改!"));
		}
	}

	public static void selectUID(String name) {
		try {
			Connection connection = database.getConnection();
			String query;

			if (XConomy.config.getBoolean("Settings.mysql")) {
				query = "select * from " + tableName + " where binary player = ?";
			} else {
				query = "select * from " + tableName + " where player = ?";
			}

			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, name);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				Cache.cacheUUID(name, id);
				Cache.insertIntoCache(id, DataFormat.formatString(rs.getString(3)));
			}

			rs.close();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void getBaltop() {
		try {
			Connection connection = database.getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"select * from " + tableName + " where length(player) < 20" + " order by balance desc limit 10");

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Cache.baltop.put(rs.getString(2), DataFormat.formatString(rs.getString(3)));
				Cache.baltop_papi.add(rs.getString(2));
			}

			rs.close();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String sumBal() {
		String bal = "0.0";

		try {
			Connection connection = database.getConnection();
			PreparedStatement statement = connection.prepareStatement("select SUM(balance) from " + tableName);

			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				bal = rs.getString(1);
			}

			rs.close();
			statement.close();
			database.closeHikariConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bal;
	}

	public static void convertData(String UID, String name, Double amount) {
		Connection co = database.getConnection();
		createAccount(UID, name, amount, co);
	}

	public static void convertNonPlayerData(String acc, Double amount) {
		Connection co = database.getConnection();
		createNonPlayerAccount(acc, amount, co);
	}
}
