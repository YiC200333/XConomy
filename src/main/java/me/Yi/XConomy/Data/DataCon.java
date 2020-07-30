package me.Yi.XConomy.Data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Message.Messages;

public class DataCon {

	public static File userdata;
	public static File uiddata;
	public static File topdata;
	public static File nonpdata;

	public static boolean create(FileConfiguration config) {
		if (!config.getBoolean("Settings.mysql")) {
			logger("数据保存方式 - YML");
			File dataFolder = new File(XConomy.getInstance().getDataFolder(), "playerdata");
			dataFolder.mkdirs();
			userdata = new File(dataFolder, "data.yml");
			uiddata = new File(dataFolder, "uid.yml");
			topdata = new File(dataFolder, "baltop.yml");
			nonpdata = new File(dataFolder, "datanon.yml");
			// cheak_oldpath();
			YML.pd = YamlConfiguration.loadConfiguration(userdata);
			YML.pdu = YamlConfiguration.loadConfiguration(uiddata);
			YML.pdnon = YamlConfiguration.loadConfiguration(nonpdata);
			YML.top = YamlConfiguration.loadConfiguration(topdata);
			if (!userdata.exists() | !uiddata.exists()) {
				try {
					YML.pd.save(userdata);
					YML.pdu.save(uiddata);
					if (config.getBoolean("Settings.non-player-account")) {
						YML.pdnon.save(nonpdata);
					}
					logger("数据文件创建完成");
				} catch (IOException e) {
					e.printStackTrace();
					logger("数据文件创建异常");
					return false;
				}
			} else {
				logger("找到数据文件");
			}
		} else {
			logger("数据保存方式 - MySQL");
			XConomy.mysqldb = new MySQL();
			mysql_table(config);

			if (XConomy.mysqldb.con()) {
				XConomy.mysqldb.createt();
				logger("MySQL连接正常");
				logger("XConomy加载成功");
			} else {
				logger("MySQL连接异常");
				return false;
			}
		}
		return true;
	}

	public static void newplayer(Player a, Double ii) {
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			YML.create(a, ii);
			YML.createu(a);
		} else {
			XConomy.mysqldb.newplayer(a.getUniqueId().toString(), a.getName(), ii);
		}
	}

	public static void getbal(UUID u) {
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			YML.getbal(u);
		} else {
			XConomy.mysqldb.select(u);
		}
	}

	public static void getuid(String name) {
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			YML.getuid(name);
		} else {
			XConomy.mysqldb.select_UID(name);
		}
	}

	public static void getbalnon(String u) {
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			YML.getbal_nonp(u);
		} else {
			XConomy.mysqldb.select_non(u);
		}
	}

	public static void save(UUID UID, BigDecimal amount, Integer type) {
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			Double am = YML.pd.getDouble(UID.toString() + ".balance");
			BigDecimal ls = DataFormat.formatd(am);
			if (type == 1) {
				YML.pd.set(UID + ".balance", (ls.add(amount)).doubleValue());
				YML.baltopls.put(YML.pd.getString(UID.toString() + ".username"),ls.add(amount).doubleValue());
			} else if (type == 2) {
				YML.pd.set(UID + ".balance", (ls.subtract(amount)).doubleValue());
				YML.baltopls.put(YML.pd.getString(UID.toString() + ".username"),ls.subtract(amount).doubleValue());
			} else if (type == 3) {
				YML.pd.set(UID + ".balance", amount.doubleValue());
				YML.baltopls.put(YML.pd.getString(UID.toString() + ".username"),amount.doubleValue());
			}
			YML.save(YML.pd, userdata);
		} else {
			XConomy.mysqldb.save(UID.toString(), amount.doubleValue(), type);
		}
	}

	public static void save_non(String account, BigDecimal amount, Integer type) {
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			Double am = YML.pd.getDouble(account + ".balance");
			BigDecimal ls = DataFormat.formatd(am);
			if (type == 1) {
				YML.pd.set(account + ".balance", (ls.add(amount)).doubleValue());
			} else if (type == 2) {
				YML.pd.set(account + ".balance", (ls.subtract(amount)).doubleValue());
			} else if (type == 3) {
				YML.pd.set(account + ".balance", amount.doubleValue());
			}
			YML.save(YML.pdnon, nonpdata);
		} else {
			XConomy.mysqldb.save_non(account, amount.doubleValue(), type);
		}
	}

	// ===============================================================================================
	// private static void cheak_oldpath() {
	// File userdatao = new File(XConomy.getInstance().getDataFolder(), "data.yml");
	// File uiddatao = new File(XConomy.getInstance().getDataFolder(), "uid.yml");
	// if (userdatao.exists() & !userdata.exists()) {
	// userdatao.renameTo(userdata);
	// }
	// if (uiddatao.exists() & !uiddata.exists()) {
	// uiddatao.renameTo(uiddata);
	// }
	// }

	private static void logger(String mess) {
		XConomy.getInstance().getLogger().info(Messages.sysmess(mess));
	}

	private static void mysql_table(FileConfiguration config) {
		if (config.getString("MySQL.table_suffix") != null & !config.getString("MySQL.table_suffix").equals("")) {
			MySQL.datana = "xconomy_" + config.getString("MySQL.table_suffix").replace("%sign%", XConomy.getsign());
			MySQL.datananon = "xconomynon_"
					+ config.getString("MySQL.table_suffix").replace("%sign%", XConomy.getsign());
		}
	}
}
