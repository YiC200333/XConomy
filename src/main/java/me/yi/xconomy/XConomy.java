package me.yi.xconomy;

import me.yi.xconomy.data.caches.Cache;
import me.yi.xconomy.data.DataCon;
import me.yi.xconomy.data.DataFormat;
import me.yi.xconomy.data.SQL;
import me.yi.xconomy.listeners.ConnectionListeners;
import me.yi.xconomy.listeners.SPsync;
import me.yi.xconomy.message.MessagesManager;
import me.yi.xconomy.message.Messages;
import me.yi.xconomy.task.Baltop;
import me.yi.xconomy.task.Updater;
import me.yi.xconomy.utils.UpdateConfig;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

public class XConomy extends JavaPlugin {

	private static XConomy instance;
	public static FileConfiguration config;
	private MessagesManager messageManager;
	private static boolean hikariConnectionPooling = false;
	public Economy econ = null;
	private BukkitTask refresherTask = null;
	Metrics metrics = null;
	private PE papiExpansion = null;

	public void onEnable() {
		instance = this;
		load();
		if (checkup()) {
			new Updater().runTaskAsynchronously(this);
		}
		// 检查更新
		messageManager = new MessagesManager(this);
		messageManager.load();
		econ = new Vault();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			logger("发现 PlaceholderAPI");
			if (checkVaultPE()) {
				logger("======================================================================");
				logger("XConomy 的连接池 不支持 Vault 变量的 baltop 功能");
				logger("请在 PlaceholderAPI 的 config.yml 中设置 expansions.vault.baltop.enabled 为 false");
				logger("或者在 XConomy 的 config.yml 中设置 Pool-Settings.usepool 为 false");
				logger("======================================================================");
				hikariConnectionPooling = true;
			}
			setupPlaceHolderAPI();
		}

		getServer().getServicesManager().register(Economy.class, econ, this, ServicePriority.Normal);
		getServer().getPluginManager().registerEvents(new ConnectionListeners(), this);

		metrics = new Metrics(this, 6588);

		Bukkit.getPluginCommand("money").setExecutor(new Commands());
		Bukkit.getPluginCommand("balance").setExecutor(new Commands());
		Bukkit.getPluginCommand("balancetop").setExecutor(new Commands());
		Bukkit.getPluginCommand("pay").setExecutor(new Commands());
		Bukkit.getPluginCommand("xconomy").setExecutor(new Commands());

		if (!DataCon.create()) {
			onDisable();
			return;
		}

		Cache.baltop();

		if (config.getBoolean("BungeeCord.enable")) {
			if (isBungeecord()) {
				getServer().getMessenger().registerIncomingPluginChannel(this, "xconomy:aca", new SPsync());
				getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:acb");
				getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:messb");
				logger("已开启BC同步");
			} else if (!config.getBoolean("Settings.mysql")) {
				if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
					logger("SQLite文件路径设置错误");
					logger("BC同步未开启");
				}
			}
		}

		DataFormat.load();

		int time = config.getInt("Settings.refresh-time");
		if (time < 30) {
			time = 30;
		}

		refresherTask = new Baltop().runTaskTimerAsynchronously(this, time * 20, time * 20);
		logger("===== YiC =====");

	}

	public void onDisable() {
		getServer().getServicesManager().unregister(econ);

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			try {
				papiExpansion.unregister();
			} catch (NoSuchMethodError ignored) {
			}
		}

		if (isBungeecord()) {
			getServer().getMessenger().unregisterIncomingPluginChannel(this, "xconomy:aca", new SPsync());
			getServer().getMessenger().unregisterOutgoingPluginChannel(this, "xconomy:acb");
			getServer().getMessenger().unregisterOutgoingPluginChannel(this, "xconomy:messb");
		}

		refresherTask.cancel();
		SQL.close();
		logger("XConomy已成功卸载");
	}

	public static XConomy getInstance() {
		return instance;
	}

	public void reloadMessages() {
		messageManager.load();
	}

	public Economy getEconomy() {
		return econ;
	}

	public static boolean allowHikariConnectionPooling() {
		if (hikariConnectionPooling) {
			return false;
		}

		if (!config.getBoolean("Settings.mysql")) {
			return false;
		}

		return XConomy.config.getBoolean("Pool-Settings.usepool");
	}

	public static String getSign() {
		return config.getString("BungeeCord.sign");
	}

	private void setupPlaceHolderAPI() {
		papiExpansion = new PE(this);

		if (papiExpansion.register()) {
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
		getLogger().info(Messages.systemMessage(mess));
	}

	public static boolean isBungeecord() {
		if (!config.getBoolean("BungeeCord.enable")) {
			return false;
		}

		if (config.getBoolean("Settings.mysql")) {
			return true;
		}

		return !config.getBoolean("Settings.mysql") & !config.getString("SQLite.path").equalsIgnoreCase("Default");

	}

	public static boolean checkup() {
		return config.getBoolean("Settings.check-update");
	}

	private void load() {
		saveDefaultConfig();
		update_config();
		reloadConfig();
		config = getConfig();
	}

	private static boolean checkVaultPE() {
		File peFolder = new File(Bukkit.getPluginManager().getPlugin("PlaceholderAPI").getDataFolder(), "config.yml");

		boolean cvap = false;

		if (!peFolder.exists()) {
			return false;
		}

		FileConfiguration peConfig = YamlConfiguration.loadConfiguration(peFolder);
		if (peConfig.contains("expansions.vault.baltop.enabled")) {
			cvap = peConfig.getBoolean("expansions.vault.baltop.enabled");
		}

		if (config.getBoolean("Settings.mysql") && config.getBoolean("Pool-Settings.usepool")) {
			return cvap;
		}

		return false;
	}

	private void update_config() {
		File config = new File(this.getDataFolder(), "config.yml");
		boolean update = UpdateConfig.update(getConfig(), config);
		if (update) {
			saveConfig();
		}
	}

}
