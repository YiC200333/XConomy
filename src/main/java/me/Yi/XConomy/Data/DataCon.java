package me.Yi.XConomy.Data;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
			cheak_oldpath();
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

	public static Integer save(Connection co, List<UUID> ls, List<UUID> ol) {
		int x = 0;
		Iterator<UUID> aa = ls.listIterator();
		Map<String, Double> ymltop = new HashMap<String, Double>();
		while (aa.hasNext()) {
			UUID uid = aa.next();
			BigDecimal bd1 = Cache.bal.get(uid);
			if (bd1 != null) {
				Double bal = bd1.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				if (!XConomy.config.getBoolean("Settings.mysql")) {
					ymltop.put(YML.pd.getString(uid.toString() + ".username"), bal);
					YML.pd.set(uid.toString() + ".balance", bal);
				} else {
					XConomy.mysqldb.save(co, uid.toString(), bal);
				}
				x = x + 1;
			}
			if (!ol.contains(uid)) {
				Cache.bal.remove(uid);
			}
		}
		if (!XConomy.config.getBoolean("Settings.mysql")) {
			YML.savetop(ymltop);
		}
		return x;
	}

	public static Integer save_non(Connection co, List<String> ls) {
		int x = 0;
		Iterator<String> aa = ls.listIterator();
		while (aa.hasNext()) {
			String account = aa.next();
			BigDecimal bd2 = Cache_NonPlayer.bal.get(account);
			if (bd2 != null) {
				Double bal = bd2.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
				if (!XConomy.config.getBoolean("Settings.mysql")) {
					YML.pdnon.set(account + ".balance", bal);
				} else {
					XConomy.mysqldb.save_non(co, account, bal);
				}
				x = x + 1;
			}
		}
		return x;
	}

	// ===============================================================================================
	private static void cheak_oldpath() {
		File userdatao = new File(XConomy.getInstance().getDataFolder(), "data.yml");
		File uiddatao = new File(XConomy.getInstance().getDataFolder(), "uid.yml");
		if (userdatao.exists() & !userdata.exists()) {
			userdatao.renameTo(userdata);
		}
		if (uiddatao.exists() & !uiddata.exists()) {
			uiddatao.renameTo(uiddata);
		}
	}

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
