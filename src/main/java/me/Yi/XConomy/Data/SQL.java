package me.Yi.XConomy.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import java.math.BigDecimal;
import java.sql.Connection;
import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Message.Messages;

public class SQL {

	public static String datana = "xconomy";
	public static String datananon = "xconomynon";
	private final static DataBaseCon mcon = new DataBaseCon();
	private static Double iam = XConomy.config.getDouble("Settings.initial-bal");

	public static boolean con() {
		return mcon.con();
	}

	public static void close() {
		mcon.close();
	}

	public static void createt() {
		try {
			Connection co = mcon.getcon();
			Statement pst = co.createStatement();
			if (pst != null) {
				String sql1 = "SQL";
				String sql2 = "SQL";
				if (XConomy.config.getBoolean("Settings.mysql")) {
					sql1 = "CREATE TABLE IF NOT EXISTS " + datana
							+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, "
							+ "primary key (UID)) DEFAULT CHARSET = utf8;";
					sql2 = "CREATE TABLE IF NOT EXISTS " + datananon
							+ "(account varchar(50) not null, balance double(20,2) not null, "
							+ "primary key (account)) DEFAULT CHARSET = utf8;";
				} else {
					sql1 = "CREATE TABLE IF NOT EXISTS " + datana
							+ "(UID varchar(50) not null, player varchar(50) not null, balance double(20,2) not null, "
							+ "primary key (UID));";
					sql2 = "CREATE TABLE IF NOT EXISTS " + datananon
							+ "(account varchar(50) not null, balance double(20,2) not null, "
							+ "primary key (account));";
				}
				pst.executeUpdate(sql1);
				if (XConomy.config.getBoolean("Settings.non-player-account")) {
					pst.executeUpdate(sql2);
				}
				pst.close();
				mcon.closep(co);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void newplayer(String UID, String user) {
		Connection co = mcon.getcon();
		select_user(UID, user, co);
		mcon.closep(co);
	}

	private static void cr_a(String UID, String user, Double amount, Connection co_a) {
		try {
			String sql = "SQL";
			if (XConomy.config.getBoolean("Settings.mysql")) {
				sql = "INSERT INTO " + datana + "(UID,player,balance) values(?,?,?) "
						+ "ON DUPLICATE KEY UPDATE UID = ?";
			} else {
				sql = "INSERT INTO " + datana + "(UID,player,balance) values(?,?,?) ";
			}
			PreparedStatement inserplayer = co_a.prepareStatement(sql);
			inserplayer.setString(1, UID);
			inserplayer.setString(2, user);
			inserplayer.setDouble(3, amount);
			if (XConomy.config.getBoolean("Settings.mysql")) {
				inserplayer.setString(4, UID);
			}
			inserplayer.executeUpdate();
			inserplayer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void cr_non(String account, Double bal, Connection co) {
		try {
			String sql = "SQL";
			if (XConomy.config.getBoolean("Settings.mysql")) {
				sql = "INSERT INTO " + datananon + "(account,balance) values(?,?) "
						+ "ON DUPLICATE KEY UPDATE account = ?";
			} else {
				sql = "INSERT INTO " + datananon + "(account,balance) values(?,?)";
			}
			PreparedStatement inserplayernon = co.prepareStatement(sql);
			inserplayernon.setString(1, account);
			inserplayernon.setDouble(2, bal);
			if (XConomy.config.getBoolean("Settings.mysql")) {
				inserplayernon.setString(3, account);
			}
			inserplayernon.executeUpdate();
			inserplayernon.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void upuser(String UID, String user, Connection co_a) {
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

	public static void save(String UID, Double amount, Integer type) {
		try {
			Connection co = mcon.getcon();
			String sqla = "";
			if (type == 1) {
				sqla = " set balance = balance + " + amount.toString() + " where UID = ?";
			} else if (type == 2) {
				sqla = " set balance = balance - " + amount.toString() + " where UID = ?";
			} else if (type == 3) {
				sqla = " set balance = " + amount.toString() + " where UID = ?";
			}
			PreparedStatement saveda = co.prepareStatement("update " + datana + sqla);
			saveda.setString(1, UID);
			saveda.executeUpdate();
			saveda.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void save_non(String account, Double amount, Integer type) {
		try {
			Connection co = mcon.getcon();
			String sqla = "";
			if (type == 1) {
				sqla = " set balance = balance + " + amount + " where account = ?";
			} else if (type == 2) {
				sqla = " set balance = balance - " + amount + " where account = ?";
			} else if (type == 3) {
				sqla = " set balance = " + amount + " where account = ?";
			}
			PreparedStatement saveda = co.prepareStatement("update " + datananon + sqla);
			saveda.setString(1, account);
			saveda.executeUpdate();
			saveda.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void select(UUID u) {
		try {
			Connection co = mcon.getcon();
			PreparedStatement selectplayer = co.prepareStatement("select * from " + datana + " where UID = ?");
			selectplayer.setString(1, u.toString());
			ResultSet rs = selectplayer.executeQuery();
			if (rs.next()) {
				BigDecimal ls = DataFormat.formatsb(rs.getString(3));
				Cache.addbal(u, ls);
			}
			rs.close();
			selectplayer.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void select_non(String u) {
		try {
			Connection co = mcon.getcon();
			String sql = "SQL";
			if (XConomy.config.getBoolean("Settings.mysql")) {
				sql = "select * from " + datananon + " where binary account = ?";
			} else {
				sql = "select * from " + datananon + " where cast(account as varbinary)=cast(? as varbinary)";
			}
			PreparedStatement selectplayernon = co.prepareStatement(sql);
			selectplayernon.setString(1, u);
			ResultSet rs = selectplayernon.executeQuery();
			if (rs.next()) {
				Cache_NonPlayer.addbal(u, DataFormat.formatsb(rs.getString(2)));
			} else {
				cr_non(u, 0.0, co);
				Cache_NonPlayer.addbal(u, DataFormat.formatsb("0.0"));
			}
			rs.close();
			selectplayernon.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void select_user(String UID, String name, Connection co_a) {
		String user = "";
		try {
			PreparedStatement selectuser = co_a.prepareStatement("select * from " + datana + " where UID = ?");
			selectuser.setString(1, UID.toString());
			ResultSet rs = selectuser.executeQuery();
			if (rs.next()) {
				user = rs.getString(2);
			} else {
				user = name;
				cr_a(UID, user, iam, co_a);
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

	public static void select_UID(String name) {
		try {
			Connection co = mcon.getcon();
			String sql = "SQL";
			if (XConomy.config.getBoolean("Settings.mysql")) {
				sql = "select * from " + datana + " where binary player = ?";
			} else {
				sql = "select * from " + datana + " where cast(player as varbinary)=cast(? as varbinary)";
			}
			PreparedStatement selectuid = co.prepareStatement(sql);
			selectuid.setString(1, name);
			ResultSet rs = selectuid.executeQuery();
			if (rs.next()) {
				UUID id = UUID.fromString(rs.getString(1));
				Cache.adduid(name, id);
				Cache.addbal(id, DataFormat.formatsb(rs.getString(3)));
			}
			rs.close();
			selectuid.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void top() {
		try {
			Connection co = mcon.getcon();
			PreparedStatement savedatop = co.prepareStatement(
					"select * from " + datana + " where length(player) < 20" + " order by balance desc limit 10");
			ResultSet rs = savedatop.executeQuery();
			while (rs.next()) {
				Cache.baltop.put(rs.getString(2), DataFormat.formatsb(rs.getString(3)));
				Cache.baltop_papi.add(rs.getString(2));
			}
			rs.close();
			savedatop.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String sumbal() {
		String a = "0.0";
		try {
			Connection co = mcon.getcon();
			PreparedStatement ssumbal = co.prepareStatement("select SUM(balance) from " + datana);
			ResultSet rs = ssumbal.executeQuery();
			if (rs.next()) {
				a = rs.getString(1);
			}
			rs.close();
			ssumbal.close();
			mcon.closep(co);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}

	public static void converty(String UID, String name, Double amount) {
		Connection co = mcon.getcon();
		cr_a(UID, name, amount, co);
	}
	

	public static void convertnon(String acc, Double amount) {
		Connection co = mcon.getcon();
		cr_non(acc, amount, co);
	}
}
