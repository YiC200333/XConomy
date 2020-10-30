package me.yic.xconomy.data;

import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.NonPlayerCache;
import me.yic.xconomy.message.Messages;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.DatabaseConnection;
import me.yic.xconomy.utils.RecordData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.UUID;

public class SQL {

	public static String tableName = "xconomy";
	public static String tableNonPlayerName = "xconomynon";
	public static String tableRecordName = "xconomyrecord";
	public final static DatabaseConnection database = new DatabaseConnection();
	private static final String encoding = XConomy.config.getString("MySQL.encoding");
	private static final Double ibal = XConomy.config.getDouble("Settings.initial-bal");

	public static boolean con() {
		return database.setGlobalConnection();
	}

	public static void close() {
		database.close();
	}

	public static void getwaittimeout() {
		if (XConomy.config.getBoolean("Settings.mysql") && !XConomy.allowHikariConnectionPooling()) {
			try {
				Connection connection = database.getConnectionAndCheck();

				String query = "show variables like 'wait_timeout'";

				PreparedStatement statement = connection.prepareStatement(query);

				ResultSet rs = statement.executeQuery();
				if (rs.next()) {
					Integer waittime = rs.getInt(2);
					if (waittime > 20){
						DatabaseConnection.waittimeout = waittime - 10;
					}

				}

				rs.close();
				statement.close();
				database.closeHikariConnection(connection);

			} catch (SQLException ignored) {
				XConomy.getInstance().logger("Get 'wait_timeout' error");
			}
		}
	}

	public static void createTable() {
		try {
			Connection connection = database.getConnectionAndCheck();
			Statement statement = connection.createStatement();

			if (statement == null) {
				return;
			}

			String query1;
			String query2;
			String query3 = "CREATE TABLE IF NOT EXISTS " + tableRecordName
					+ "(id int(20) not null auto_increment, type varchar(50) not null, uid varchar(50) not null, player varchar(50) not null,"
					+ "balance double(20,2), amount double(20,2) not null, operation varchar(50) not null,"
					+ " date varchar(50) not null, command varchar(50) not null,"
					+ "primary key (id)) DEFAULT CHARSET = "+encoding+";";
			if (XConomy.config.getBoolean("Settings.mysql")) {
				query1 = "CREATE TABLE IF NOT EXISTS " + tableName
						+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, hidden int(5) not null, "
						+ "primary key (UID)) DEFAULT CHARSET = "+encoding+";";
				query2 = "CREATE TABLE IF NOT EXISTS " + tableNonPlayerName
						+ "(account varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (account)) DEFAULT CHARSET = "+encoding+";";
			} else {
				query1 = "CREATE TABLE IF NOT EXISTS " + tableName
						+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, hidden int(5) not null, "
						+ "primary key (UID));";
				query2 = "CREATE TABLE IF NOT EXISTS " + tableNonPlayerName
						+ "(account varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (account));";
			}

			statement.executeUpdate(query1);
			if (XConomy.config.getBoolean("Settings.non-player-account")) {
				statement.executeUpdate(query2);
			}
			if (XConomy.config.getBoolean("Settings.mysql") && XConomy.config.getBoolean("Settings.transaction-record")) {
				statement.executeUpdate(query3);
			}
			statement.close();
			database.closeHikariConnection(connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void updataTable() {
		Connection connection = database.getConnectionAndCheck();
		try {

			PreparedStatement statementa = connection.prepareStatement("select * from " + tableName + " where hidden = '1'");

			statementa.executeQuery();
			statementa.close();
			database.closeHikariConnection(connection);

		} catch (SQLException e) {
			try {
			XConomy.getInstance().logger("升级数据库表格。。。");

			PreparedStatement statementb = connection.prepareStatement("alter table " + tableName + " add column hidden int(5) not null default '0'");

			statementb.executeUpdate();
			statementb.close();
			database.closeHikariConnection(connection);

			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
	}

	public static void newPlayer(Player player) {
		Connection connection = database.getConnectionAndCheck();
		checkUser(player, connection);
		selectUser(player.getUniqueId().toString(), player.getName(), connection);
		database.closeHikariConnection(connection);
	}

	private static void createAccount(String UID, String user, Double amount, Connection co_a) {
		try {
			String query;
			if (XConomy.config.getBoolean("Settings.mysql")) {
				query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?) "
						+ "ON DUPLICATE KEY UPDATE UID = ?";
			} else {
				query = "INSERT INTO " + tableName + "(UID,player,balance,hidden) values(?,?,?,?) ";
			}

			PreparedStatement statement = co_a.prepareStatement(query);
			statement.setString(1, UID);
			statement.setString(2, user);
			statement.setDouble(3, amount);
			statement.setInt(4, 0);

			if (XConomy.config.getBoolean("Settings.mysql")) {
				statement.setString(5, UID);
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

	public static void save(UUID u, BigDecimal newbalance, BigDecimal balance, BigDecimal amount, Boolean isAdd, RecordData x) {
		Connection connection = database.getConnectionAndCheck();
		try {
			String query;
				query = " set balance = " + newbalance.doubleValue() + " where UID = ?";
			Boolean requirefresh = false;
			if (XConomy.config.getBoolean("Settings.cache-correction")&&isAdd!=null){
				requirefresh = true;
				query = query + "AND balance = " + balance.toString();
			}
			PreparedStatement statement1 = connection.prepareStatement("update " + tableName + query);
			statement1.setString(1, u.toString());
			Integer rs = statement1.executeUpdate();
			statement1.close();
			if (requirefresh && rs == 0){
				Cache.refreshFromCache(u);
				Cache.cachecorrection(u,amount,isAdd);
				x.addcachecorrection();
				if (isAdd) {
				query = " set balance = balance + " + amount.doubleValue() + " where UID = ?";
				} else {
				query = " set balance = balance - " + amount.doubleValue() + " where UID = ?";
				}
				PreparedStatement statement2 = connection.prepareStatement("update " + tableName + query);
				statement2.setString(1, u.toString());
				statement2.executeUpdate();
				statement2.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		record(x,connection);
		database.closeHikariConnection(connection);
	}

	public static void saveall(String targettype, List<UUID> players, Double amount, Boolean isAdd,  RecordData x) {
		Connection connection = database.getConnectionAndCheck();
		try {
			if (targettype.equalsIgnoreCase("all")) {
				String query;
				if (isAdd) {
					query = " set balance = balance + " + amount;
				} else {
					query = " set balance = balance - " + amount;
				}
				PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
				statement.executeUpdate();
				statement.close();
			} else if (targettype.equalsIgnoreCase("online")) {
				String query;
				if (isAdd) {
					query = " set balance = balance + " + amount + " where";
				} else {
					query = " set balance = balance - " + amount + " where";
				}
				int jsm = players.size();
				int js = 1;

				for (UUID u : players) {
					if (js == jsm) {
						query = query + " UID = '"+u.toString()+"'";
					}else {
						query = query + " UID = '"+u.toString()+"' OR";
						js = js + 1;
					}
				}
				PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
				statement.executeUpdate();
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (x!=null) {
			record(x, connection);
		}
		database.closeHikariConnection(connection);
	}

	public static void saveNonPlayer(String account, Double newbalance, RecordData x) {
		Connection connection = database.getConnectionAndCheck();
		try {
			String query;
				query = " set balance = " + newbalance + " where account = ?";
			PreparedStatement statement = connection.prepareStatement("update " + tableNonPlayerName + query);
			statement.setString(1, account);
			statement.executeUpdate();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		record(x,connection);
		database.closeHikariConnection(connection);
	}

	public static void select(UUID uuid) {
		try {
			Connection connection = database.getConnectionAndCheck();
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
			Connection connection = database.getConnectionAndCheck();
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
			Connection connection = database.getConnectionAndCheck();
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
			Connection connection = database.getConnectionAndCheck();
			PreparedStatement statement = connection.prepareStatement(
					"select * from " + tableName + " where hidden != '1' order by balance desc limit 10");

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
			Connection connection = database.getConnectionAndCheck();
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

	public static void hidetop(UUID u, Integer type) {
		Connection connection = database.getConnectionAndCheck();
		try {
			String query = " set hidden = ? where UID = ?";
			PreparedStatement statement = connection.prepareStatement("update " + tableName + query);
			statement.setInt(1, type);
			statement.setString(2, u.toString());
			statement.executeUpdate();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		database.closeHikariConnection(connection);
	}

	public static void record(RecordData x,Connection co) {
		if (XConomy.config.getBoolean("Settings.mysql") && XConomy.config.getBoolean("Settings.transaction-record")) {
			try {
				String query;
				query = "INSERT INTO " + tableRecordName + "(type,uid,player,balance,amount,operation,date,command) values(?,?,?,?,?,?,?,?)";
				PreparedStatement statement = co.prepareStatement(query);
				statement.setString(1, x.gettype());
				statement.setString(2, x.getuid());
				statement.setString(3, x.getplayer());
				statement.setDouble(4, x.getbalance());
				statement.setDouble(5, x.getamount());
				statement.setString(6, x.getoperation());
				statement.setString(7, x.getdate());
				statement.setString(8, x.getcommand());
				statement.executeUpdate();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static void convertData(String UID, String name, Double amount) {
		Connection co = database.getConnectionAndCheck();
		createAccount(UID, name, amount, co);
	}

	public static void convertNonPlayerData(String acc, Double amount) {
		Connection co = database.getConnectionAndCheck();
		createNonPlayerAccount(acc, amount, co);
	}
}
