package me.Yi.XConomy.Data;

import java.io.File;
import java.math.BigDecimal;
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

	public static void save(UUID UID, BigDecimal amount, Boolean isAdd) {
		SQL.save(UID.toString(), amount.doubleValue(), isAdd);
	}

	public static void save_non(String account, BigDecimal amount, Integer type) {
		SQL.save_non(account, amount.doubleValue(), type);
	}

	private static void mysql_table() {
		if (config.getString("MySQL.table_suffix") != null & !config.getString("MySQL.table_suffix").equals("")) {
			SQL.datana = "xconomy_" + config.getString("MySQL.table_suffix").replace("%sign%", getsign());
			SQL.datananon = "xconomynon_" + config.getString("MySQL.table_suffix").replace("%sign%", getsign());
		}
	}
}
