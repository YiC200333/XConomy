package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.DatabaseConnection;
import me.yic.xconomy.utils.RecordData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataCon extends XConomy {

	public static boolean create() {
		if (config.getBoolean("Settings.mysql")) {
			getInstance().logger("数据保存方式 - MySQL");
			setupMySqlTable();

			if (SQL.con()) {
				SQL.getwaittimeout();
				SQL.createTable();
				SQL.updataTable();
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
				SQL.updataTable();
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

	public static void setTopBalHide(UUID u,Integer type) {
		SQL.hidetop(u,type);
	}

	public static String getBalSum() {
		if (SQL.sumBal() == null) {
			return "0.0";
		}
		return SQL.sumBal();
	}

	public static void save(UUID UID, BigDecimal newbalance, BigDecimal balance, BigDecimal amount, Boolean isAdd, RecordData x) {
		SQL.save(UID, newbalance, balance, amount, isAdd, x);
	}

	public static void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordData x) {
		new BukkitRunnable() {
			@Override
			public void run() {
				if (targettype.equalsIgnoreCase("all")) {
					SQL.saveall(targettype, null, amount.doubleValue(), isAdd, x);
				} else if (targettype.equalsIgnoreCase("online")) {
					List<UUID> ol = new ArrayList<UUID>();
					for (Player pp : Bukkit.getOnlinePlayers()) {
						ol.add(pp.getUniqueId());
					}
					SQL.saveall(targettype, ol, amount.doubleValue(), isAdd, x);
				}
			}
		}.runTaskAsynchronously(XConomy.getInstance());
	}

	public static void saveNonPlayer(String account, BigDecimal newbalance, RecordData x) {
		SQL.saveNonPlayer(account, newbalance.doubleValue(), x);
	}

	private static void setupMySqlTable() {
		if (config.getString("MySQL.table-suffix") != null & !config.getString("MySQL.table-suffix").equals("")) {
			SQL.tableName = "xconomy_" + config.getString("MySQL.table-suffix").replace("%sign%", getSign());
			SQL.tableNonPlayerName = "xconomynon_" + config.getString("MySQL.table-suffix").replace("%sign%", getSign());
			SQL.tableRecordName = "xconomyrecord_" + config.getString("MySQL.table-suffix").replace("%sign%", getSign());
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
