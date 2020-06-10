package me.Yi.XConomy;

import java.io.File;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataCon;
import me.Yi.XConomy.Data.DataFormat;
import me.Yi.XConomy.Data.MySQL;
import me.Yi.XConomy.Data.Save;
import me.Yi.XConomy.Event.Join;
import me.Yi.XConomy.Event.Quit;
import me.Yi.XConomy.Event.SPsync;
import me.Yi.XConomy.Message.MessManage;
import me.Yi.XConomy.Message.Messages;
import net.milkbowl.vault.economy.Economy;

public class XConomy extends JavaPlugin {

	private static XConomy instance;
	public static FileConfiguration config;
	private MessManage messm;
	public Economy econ = null;
	public static MySQL mysqldb = null;

	public void onEnable() {

		instance = this;
		load();
		if (checkup()) {
			new Updater().runTaskAsynchronously(this);
		}
		// 检查更新
		messm = new MessManage(this);
		messm.load();
		econ = new Vault();
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			logger("发现 PlaceholderAPI");
			PE.registerExpansion();
		}
		getServer().getServicesManager().register(Economy.class, econ, this, ServicePriority.Normal);
		getServer().getPluginManager().registerEvents(new Join(), this);
		new Metrics(this, 6588);
		Bukkit.getPluginCommand("money").setExecutor(new cmd());
		Bukkit.getPluginCommand("balance").setExecutor(new cmd());
		Bukkit.getPluginCommand("balancetop").setExecutor(new cmd());
		Bukkit.getPluginCommand("pay").setExecutor(new cmd());
		Bukkit.getPluginCommand("xconomy").setExecutor(new cmd());
		if (!DataCon.create(config)) {
			onDisable();
		}
		Cache.baltop();
		if (config.getBoolean("BungeeCord.enable")) {
			if (config.getBoolean("Settings.mysql")) {
				getServer().getPluginManager().registerEvents(new Quit(), this);
				getServer().getMessenger().registerIncomingPluginChannel(this, "xconomy:aca", new SPsync());
				getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:acb");
				getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:messb");
				logger("已开启BC同步");
			} else {
				logger("BC同步只支持MySQL");
				logger("BC同步未开启");
			}
		}
		DataFormat.load();
		Integer time = config.getInt("Settings.autosave-time");
		new Save().runTaskTimerAsynchronously(this, time * 20, time * 20);
		logger("===== YiC =====");

	}

	public void onDisable() {
		getServer().getServicesManager().unregister(econ);
		new Save().run();
		if (config.getBoolean("Settings.mysql")) {
			mysqldb.close();
		}
		logger("XConomy已成功卸载");
	}

	public static XConomy getInstance() {
		return instance;
	}

	public void reloadmess() {
		messm.load();
	}

	public Economy getEconomy() {
		return econ;
	}

	public static String getsign() {
		return config.getString("BungeeCord.sign");
	}

	public String lang() {
		if (config.getString("Settings.language").equalsIgnoreCase("zh_CN")) {
			return "Chinese";
		} else if (config.getString("Settings.language").equalsIgnoreCase("en_US")) {
			return "English";
		} else if (config.getString("Settings.language").equalsIgnoreCase("fr_FR")) {
			return "French";
		} else if (config.getString("Settings.language").equalsIgnoreCase("es_ES")) {
			return "Spanish";
		}
		return config.getString("Settings.language");
	}

	public void logger(String mess) {
		getLogger().info(Messages.sysmess(mess));
	}

	public static boolean isbc() {
		if (config.getBoolean("BungeeCord.enable") & config.getBoolean("Settings.mysql")) {
			return true;
		}
		return false;
	}

	public static boolean checkup() {
		if (config.getBoolean("Settings.check-update")) {
			return true;
		}
		return false;
	}

	public void load() {
		saveDefaultConfig();
		update_config();
		reloadConfig();
		config = getConfig();
	}

	private void update_config() {
		boolean x = false;
		File cc = new File(this.getDataFolder(), "config.yml");
		FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
		if (!ck.contains("Settings.check-update")) {
			getConfig().createSection("Settings.check-update");
			getConfig().set("Settings.check-update", true);
			x = true;
		}
		if (!ck.contains("MySQL.table_suffix")) {
			getConfig().createSection("MySQL.table_suffix");
			getConfig().set("MySQL.table_suffix", "");
			x = true;
		}
		if (!ck.contains("Settings.non-player-account")) {
			getConfig().createSection("Settings.non-player-account");
			getConfig().set("Settings.non-player-account", false);
			x = true;
		}
		if (!ck.contains("MySQL.usepool")) {
			getConfig().createSection("MySQL.usepool");
			getConfig().set("MySQL.usepool", true);
			x = true;
		}
		if (!ck.contains("Currency")) {
			getConfig().createSection("Currency.singular-name");
			getConfig().set("Currency.singular-name", "dollar");
			getConfig().createSection("Currency.plural-name");
			getConfig().set("Currency.plural-name", "dollars");
			getConfig().createSection("Currency.integer-bal");
			if (ck.contains("Settings.integer")) {
				getConfig().set("Currency.integer-bal", getConfig().getBoolean("Settings.integer"));
			} else {
				getConfig().set("Currency.integer-bal", false);
			}
			getConfig().createSection("Currency.display-format");
			getConfig().set("Currency.display-format", "%balance% %currencyname%");
			x = true;
		}
		if (!ck.contains("Currency.thousands-separator")) {
			getConfig().createSection("Currency.thousands-separator");
			getConfig().set("Currency.thousands-separator", ",");
			x = true;
		}
		if (!ck.contains("Pool-Settings.maximum-lifetime")) {
			getConfig().createSection("Pool-Settings.maximum-pool-size");
			getConfig().set("Pool-Settings.maximum-pool-size", 10);
			getConfig().createSection("Pool-Settings.minimum-idle");
			getConfig().set("Pool-Settings.minimum-idle", 10);
			getConfig().createSection("Pool-Settings.maximum-lifetime");
			getConfig().set("Pool-Settings.maximum-lifetime", 180000);
			x = true;
		}
		if (!ck.contains("Pool-Settings.idle-timeout")) {
			getConfig().createSection("Pool-Settings.idle-timeout");
			getConfig().set("Pool-Settings.idle-timeout", 60000);
			x = true;
		}
		if (x) {
			saveConfig();
		}
	}

}
