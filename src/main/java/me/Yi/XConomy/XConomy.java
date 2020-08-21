package me.Yi.XConomy;

import java.io.File;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataCon;
import me.Yi.XConomy.Data.DataFormat;
import me.Yi.XConomy.Data.SQL;
import me.Yi.XConomy.Event.Join;
import me.Yi.XConomy.Event.Quit;
import me.Yi.XConomy.Event.SPsync;
import me.Yi.XConomy.Message.MessManage;
import me.Yi.XConomy.Message.Messages;
import me.Yi.XConomy.Task.Baltop;
import me.Yi.XConomy.Task.Updater;
import me.Yi.XConomy.Utils.UpdateConfig;
import net.milkbowl.vault.economy.Economy;

public class XConomy extends JavaPlugin {

	private static XConomy instance;
	public static FileConfiguration config;
	private MessManage messm;
	private static boolean cpe = false;
	public Economy econ = null;
	private BukkitTask refreshtask = null;

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
			if (cvaultpe()) {
				logger("======================================================================");
				logger("XConomy 的连接池 不支持 Vault 变量的 baltop 功能");
				logger("请在 PlaceholderAPI 的 config.yml 中设置 expansions.vault.baltop.enabled 为 false");
				logger("或者在 XConomy 的 config.yml 中设置 Pool-Settings.usepool 为 false");
				logger("======================================================================");
				cpe = true;
			}
			setupPlaceHolderAPI();
		}
		getServer().getServicesManager().register(Economy.class, econ, this, ServicePriority.Normal);
		getServer().getPluginManager().registerEvents(new Join(), this);
		getServer().getPluginManager().registerEvents(new Quit(), this);
		new Metrics(this, 6588);
		Bukkit.getPluginCommand("money").setExecutor(new cmd());
		Bukkit.getPluginCommand("balance").setExecutor(new cmd());
		Bukkit.getPluginCommand("balancetop").setExecutor(new cmd());
		Bukkit.getPluginCommand("pay").setExecutor(new cmd());
		Bukkit.getPluginCommand("xconomy").setExecutor(new cmd());
		if (!DataCon.create()) {
			onDisable();
			return;
		}
		Cache.baltop();
		if (config.getBoolean("BungeeCord.enable")) {
			if (config.getBoolean("Settings.mysql")) {
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
		Integer time = config.getInt("Settings.refresh-time");
		if (time < 30) {
			time = 30;
		}
		refreshtask = new Baltop().runTaskTimerAsynchronously(this, time * 20, time * 20);
		logger("===== YiC =====");

	}

	public void onDisable() {
		getServer().getServicesManager().unregister(econ);
		(new PE(this)).unregister();
		refreshtask.cancel();
		SQL.close();
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

	public static boolean allowHikariConnectionPooling() {
		if (cpe) {
			return false;
		}
		if (!config.getBoolean("Settings.mysql")){
			return false;
		}
		return XConomy.config.getBoolean("Pool-Settings.usepool");
	}

	public static String getsign() {
		return config.getString("BungeeCord.sign");
	}

	@SuppressWarnings("deprecation")
	private void setupPlaceHolderAPI() {
		if ((new PE(this)).register()) {
			getLogger().info("PlaceholderAPI successfully hooked");
		} else {
			getLogger().info("PlaceholderAPI unsuccessfully hooked");
		}
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

	private static boolean cvaultpe() {
		File PEFolder = new File(Bukkit.getPluginManager().getPlugin("PlaceholderAPI").getDataFolder(), "config.yml");
		boolean cvap = false;
		if (PEFolder.exists()) {
			FileConfiguration PEck = YamlConfiguration.loadConfiguration(PEFolder);
			if (PEck.contains("expansions.vault.baltop.enabled")) {
				cvap = PEck.getBoolean("expansions.vault.baltop.enabled");
			}
		}
		if (config.getBoolean("Settings.mysql") && config.getBoolean("Pool-Settings.usepool")) {
			return cvap;
		}
		return false;
	}

	private void update_config() {
		File cc = new File(this.getDataFolder(), "config.yml");
		boolean x = UpdateConfig.update(getConfig(), cc);
		if (x) {
			saveConfig();
		}
	}

}
