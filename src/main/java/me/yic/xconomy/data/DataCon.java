package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import org.bukkit.entity.Player;

import java.io.File;
import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataCon extends XConomy {

	public static boolean create() {
		if (config.getBoolean("Settings.mysql")) {
			getInstance().logger("数据保存方式 - MySQL");
			setupMySqlTable();

			if (SQL.con()) {
				SQL.createTable();
				getInstance().logger("MySQL连接正常");
			} else {
				getInstance().logger("MySQL连接异常");
				return false;
			}

		} else {
			getInstance().logger("数据保存方式 - SQLite");
			setupSqLiteAddress();

			File dataFolder = new File(getInstance().getDataFolder(), "playerdata");
			dataFolder.mkdirs();
			if (SQL.con()) {
				SQL.createTable();
				getInstance().logger("SQLite连接正常");
			} else {
				getInstance().logger("SQLite连接异常");
				return false;
			}

			Convert.convert(dataFolder);
		}
		getInstance().logger("XConomy加载成功");
		return true;
	}

	public static void newPlayer(Player a) {
		SQL.newPlayer(a);
	}

	public static void getBal(UUID u) {
		SQL.select(u);
	}

	public static void getUid(String name) {
		SQL.selectUID(name);
	}

	public static void getBalNonPlayer(String u) {
		SQL.selectNonPlayer(u);
	}

	public static void getTopBal() {
		SQL.getBaltop();
	}

	public static String getBalSum() {
		if (SQL.sumBal() == null) {
			return "0.0";
		}
		return SQL.sumBal();
	}

	public static void save(UUID UID, BigDecimal amount, Boolean isAdd) {
		SQL.save(UID.toString(), amount.doubleValue(), isAdd);
	}

	public static void saveNonPlayer(String account, BigDecimal amount, Boolean isAdd) {
		SQL.saveNonPlayer(account, amount.doubleValue(), isAdd);
	}

	private static void setupMySqlTable() {
		if (config.getString("MySQL.table_suffix") != null & !config.getString("MySQL.table_suffix").equals("")) {
			SQL.tableName = "xconomy_" + config.getString("MySQL.table_suffix").replace("%sign%", getSign());
			SQL.tableNonPlayerName = "xconomynon_" + config.getString("MySQL.table_suffix").replace("%sign%", getSign());
		}
	}

	private static void setupSqLiteAddress() {
		if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
			return;
		}

		File folder = new File(config.getString("SQLite.path"));
		if (folder.exists()) {
			DatabaseConnection.userdata = new File(folder, "data.db");
		} else {
			getInstance().logger("自定义文件夹路径不存在");
		}

	}
}
