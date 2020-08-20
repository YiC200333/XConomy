package me.Yi.XConomy.Data;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import me.Yi.XConomy.XConomy;

public class DataCon extends XConomy {

	public static File userdata;
	public static File uiddata;
	public static File topdata;
	public static File nonpdata;

	public static boolean create() {
		if (config.getBoolean("Settings.mysql")) {
			getInstance().logger("数据保存方式 - MySQL");
			mysql_table();
			if (SQL.con()) {
				SQL.createt();
				getInstance().logger("MySQL连接正常");
			} else {
				getInstance().logger("MySQL连接异常");
				return false;
			}
		} else {
			getInstance().logger("数据保存方式 - SQLite");
			File dataFolder = new File(getInstance().getDataFolder(), "playerdata");
			dataFolder.mkdirs();
			if (SQL.con()) {
				SQL.createt();
				getInstance().logger("SQLite连接正常");
			} else {
				getInstance().logger("SQLite连接异常");
				return false;
			}
			Convert.conv(dataFolder);
		}
		getInstance().logger("XConomy加载成功");
		return true;
	}

	public static void newplayer(Player a) {
		SQL.newplayer(a.getUniqueId().toString(), a.getName());
	}

	public static void getbal(UUID u) {
		SQL.select(u);
	}

	public static void getuid(String name) {
		SQL.select_UID(name);
	}

	public static void getbalnon(String u) {
		SQL.select_non(u);
	}

	public static void gettopbal() {
		SQL.top();
	}

	public static String getsumbal() {
		if (SQL.sumbal() == null) {
			return "0.0";
		}
		return SQL.sumbal();
	}

	public static Integer save(Connection co, List<UUID> ls) {
		int x = 0;
		Iterator<UUID> aa = ls.listIterator();
		while (aa.hasNext()) {
			UUID uid = aa.next();
			BigDecimal bd1 = Cache.bal_change.get(uid);
			if (bd1 != null && !bd1.equals(null)) {
				Double bal = bd1.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				SQL.save(co, uid.toString(), bal);
				x = x + 1;
			}
		}
		return x;
	}

	public static Integer save_non(Connection co, List<String> ls) {
		int x = 0;
		Iterator<String> aa = ls.listIterator();
		while (aa.hasNext()) {
			String account = aa.next();
			BigDecimal bd2 = Cache_NonPlayer.bal.get(account);
			if (bd2 != null && !bd2.equals(null)) {
				Double bal = bd2.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				SQL.save_non(co, account, bal);
				x = x + 1;
			}
		}
		return x;
	}

	private static void mysql_table() {
		if (config.getString("MySQL.table_suffix") != null & !config.getString("MySQL.table_suffix").equals("")) {
			SQL.datana = "xconomy_" + config.getString("MySQL.table_suffix").replace("%sign%", getsign());
			SQL.datananon = "xconomynon_" + config.getString("MySQL.table_suffix").replace("%sign%", getsign());
		}
	}
}
