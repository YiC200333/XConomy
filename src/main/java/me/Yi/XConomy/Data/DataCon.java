package me.Yi.XConomy.Data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.Yi.XConomy.XConomy;

public class DataCon extends XConomy {

	public static File userdata;
	public static File uiddata;
	public static File topdata;
	public static File nonpdata;

	public static boolean create() {
		if (!config.getBoolean("Settings.mysql")) {
			getInstance().logger("数据保存方式 - YML");
			File dataFolder = new File(getInstance().getDataFolder(), "playerdata");
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
					getInstance().logger("数据文件创建完成");
				} catch (IOException e) {
					e.printStackTrace();
					getInstance().logger("数据文件创建异常");
					return false;
				}
			} else {
				getInstance().logger("找到数据文件");
			}
		} else {
			getInstance().logger("数据保存方式 - MySQL");
			mysql_table();

			if (MySQL.con()) {
				MySQL.createt();
				getInstance().logger("MySQL连接正常");
				getInstance().logger("XConomy加载成功");
			} else {
				getInstance().logger("MySQL连接异常");
				return false;
			}
		}
		return true;
	}

	public static void newplayer(Player a, Double ii) {
		if (!config.getBoolean("Settings.mysql")) {
			YML.create(a, ii);
			YML.createu(a);
		} else {
			MySQL.newplayer(a.getUniqueId().toString(), a.getName(), ii);
		}
	}

	public static void getbal(UUID u) {
		if (!config.getBoolean("Settings.mysql")) {
			YML.getbal(u);
		} else {
			MySQL.select(u);
		}
	}

	public static void getuid(String name) {
		if (!config.getBoolean("Settings.mysql")) {
			YML.getuid(name);
		} else {
			MySQL.select_UID(name);
		}
	}

	public static void getbalnon(String u) {
		if (!config.getBoolean("Settings.mysql")) {
			YML.getbal_nonp(u);
		} else {
			MySQL.select_non(u);
		}
	}

	public static void gettopbal() {
		if (!config.getBoolean("Settings.mysql")) {
			YML.gettop();
		} else {
			MySQL.top();
		}
	}

	public static String getsumbal() {
		if (!config.getBoolean("Settings.mysql")) {
			return YML.getsumbal();
		} else {
			return MySQL.sumbal();
		}
	}

	public static void save(UUID UID, BigDecimal amount, Integer type) {
		if (!config.getBoolean("Settings.mysql")) {
			String am = YML.pd.getString(UID.toString() + ".balance");
			BigDecimal ls = DataFormat.formatsb(am);
			if (type == 1) {
				YML.pd.set(UID + ".balance", (ls.add(amount)).toString());
				YML.baltopls.put(YML.pd.getString(UID.toString() + ".username"), ls.add(amount));
			} else if (type == 2) {
				YML.pd.set(UID + ".balance", (ls.subtract(amount)).toString());
				YML.baltopls.put(YML.pd.getString(UID.toString() + ".username"), ls.subtract(amount));
			} else if (type == 3) {
				YML.pd.set(UID + ".balance", amount.toString());
				YML.baltopls.put(YML.pd.getString(UID.toString() + ".username"), amount);
			}
			YML.save(YML.pd, userdata);
		} else {
			MySQL.save(UID.toString(), amount.doubleValue(), type);
		}
	}

	public static void save_non(String account, BigDecimal amount, Integer type) {
		if (!config.getBoolean("Settings.mysql")) {
			String am = YML.pdnon.getString(account + ".balance");
			BigDecimal ls = DataFormat.formatsb(am);
			if (type == 1) {
				YML.pdnon.set(account + ".balance", (ls.add(amount)).toString());
			} else if (type == 2) {
				YML.pdnon.set(account + ".balance", (ls.subtract(amount)).toString());
			} else if (type == 3) {
				YML.pdnon.set(account + ".balance", amount.toString());
			}
			YML.save(YML.pdnon, nonpdata);
		} else {
			MySQL.save_non(account, amount.doubleValue(), type);
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

	private static void mysql_table() {
		if (config.getString("MySQL.table_suffix") != null & !config.getString("MySQL.table_suffix").equals("")) {
			MySQL.datana = "xconomy_" + config.getString("MySQL.table_suffix").replace("%sign%", getsign());
			MySQL.datananon = "xconomynon_" + config.getString("MySQL.table_suffix").replace("%sign%", getsign());
		}
	}
}
