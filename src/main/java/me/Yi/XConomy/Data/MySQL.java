package me.Yi.XConomy.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Message.Messages;

public class MySQL {
	private static String Driver = "com.mysql.jdbc.Driver";
	private static String Url = "jdbc:mysql://" + XConomy.config.getString("MySQL.host") + "/"
			+ XConomy.config.getString("MySQL.database") + "?characterEncoding=utf-8&useSSL=false";
	private static String User = XConomy.config.getString("MySQL.user");
	private static String Pass = XConomy.config.getString("MySQL.pass");
	public static String datana = "xconomy";
	public static String datananon = "xconomynon";

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
		hikari.setMaximumPoolSize(XConomy.config.getInt("Pool-Settings.maximum-pool-size"));
		hikari.setMinimumIdle(XConomy.config.getInt("Pool-Settings.minimum-idle"));
		hikari.setMaxLifetime(XConomy.config.getInt("Pool-Settings.maximum-lifetime"));
		if (hikari.getMinimumIdle() < hikari.getMaximumPoolSize()) {
			hikari.setIdleTimeout(XConomy.config.getLong("Pool-Settings.idle-timeout"));
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

	public void createt() {
		try {
			Connection co = getcon();
			Statement pst = co.createStatement();
			if (pst != null) {
				String sql1 = "CREATE TABLE IF NOT EXISTS " + datana
						+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (UID)) DEFAULT CHARSET = utf8;";
				String sql2 = "CREATE TABLE IF NOT EXISTS " + datananon
						+ "(account varchar(50) not null, balance double(20,2) not null, "
						+ "primary key (account)) DEFAULT CHARSET = utf8;";
				pst.executeUpdate(sql1);
				if (XConomy.config.getBoolean("Settings.non-player-account")) {
					pst.executeUpdate(sql2);
				}
				pst.close();
				closep(co);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void newplayer(String UID, String user, Double bal) {
		Connection co = getcon();
		cr_a(UID, user, bal, co);
		select_user(UID, user, co);
		closep(co);
	}

	private void cr_a(String UID, String user, Double bal, Connection co_a) {
		try {
			PreparedStatement inserplayer = co_a.prepareStatement("INSERT INTO " + datana
					+ "(UID,player,balance) values(?,?,?) " + "ON DUPLICATE KEY UPDATE UID = ?");
			inserplayer.setString(1, UID);
			inserplayer.setString(2, user);
			inserplayer.setDouble(3, bal);
			inserplayer.setString(4, UID);
			inserplayer.executeUpdate();
			inserplayer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void cr_non(String account, Double bal, Connection co) {
				try {
					PreparedStatement inserplayernon = co.prepareStatement("INSERT INTO " + datananon
							+ "(account,balance) values(?,?) " + "ON DUPLICATE KEY UPDATE account = ?");
					inserplayernon.setString(1, account);
					inserplayernon.setDouble(2, bal);
					inserplayernon.setString(3, account);
					inserplayernon.executeUpdate();
					inserplayernon.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	}

	private void upuser(String UID, String user, Connection co_a) {
		try {
			PreparedStatement changeuser = co_a.prepareStatement("update " + datana + " set player = ? where UID = ?");
			changeuser.setString(1, user);
			changeuser.setString(2, UID);
			changeuser.executeUpdate();
			changeuser.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(Connection co, String UID, Double amount) {
		try {
			PreparedStatement saveda = co.prepareStatement("update " + datana + " set balance = ? where UID = ?");
			saveda.setDouble(1, amount);
			saveda.setString(2, UID);
			saveda.executeUpdate();
			saveda.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save_non(Connection co, String account, Double amount) {
		try {
			PreparedStatement savedanon = co
					.prepareStatement("update " + datananon + " set balance = ? where account = ?");
			savedanon.setDouble(1, amount);
			savedanon.setString(2, account);
			savedanon.executeUpdate();
			savedanon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void select(UUID u) {
		try {
			Connection co = getcon();
			PreparedStatement selectplayer = co.prepareStatement("select * from " + datana + " where UID = ?");
			selectplayer.setString(1, u.toString());
			ResultSet rs = selectplayer.executeQuery();
			if (rs.next()) {
				Cache.addbal(u, DataFormat.formatd(rs.getDouble(3)));
			}
			rs.close();
			selectplayer.close();
			closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void select_non(String u) {
		try {
			Connection co = getcon();
			PreparedStatement selectplayernon = co
					.prepareStatement("select * from " + datananon + " where account = ?");
			selectplayernon.setString(1, u);
			ResultSet rs = selectplayernon.executeQuery();
			if (rs.next()) {
				Cache_NonPlayer.addbal(u, DataFormat.formatd(rs.getDouble(2)));
			} else {
				cr_non(u, 0.0, co);
				Cache_NonPlayer.addbal(u, DataFormat.formatd(0.0));
			}
			rs.close();
			selectplayernon.close();
			closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void select_user(String UID, String name, Connection co_a) {
		String user = "";
		try {
			PreparedStatement selectuser = co_a.prepareStatement("select * from " + datana + " where UID = ?");
			selectuser.setString(1, UID.toString());
			ResultSet rs = selectuser.executeQuery();
			if (rs.next()) {
				user = rs.getString(2);
			}
			rs.close();
			selectuser.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!user.equals(name)) {
			upuser(UID, name, co_a);
			XConomy.getInstance().logger(name + Messages.sysmess(" 名称已更改!"));
		}
	}

	public void select_UID(String name) {
		try {
			Connection co = getcon();
			PreparedStatement selectuid = co.prepareStatement("select * from " + datana + " where binary player = ?");
			selectuid.setString(1, name);
			ResultSet rs = selectuid.executeQuery();
			if (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				Cache.adduid(name, id);
				Cache.addbal(id, DataFormat.formatd(rs.getDouble(3)));
			}
			rs.close();
			selectuid.close();
			closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void top() {
		if (checkcon()) {
			try {
				Connection co = getcon();
				PreparedStatement savedatop = co.prepareStatement(
						"select * from " + datana + " where length(player) < 20" + " order by balance desc limit 10");
				ResultSet rs = savedatop.executeQuery();
				while (rs.next()) {
					Cache.baltop.put(rs.getString(2), rs.getDouble(3));
					Cache.baltop_papi.add(rs.getString(2));
				}
				rs.close();
				savedatop.close();
				closep(co);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
