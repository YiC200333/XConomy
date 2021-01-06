package me.yic.xconomy;

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.SQL;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.depend.Placeholder;
import me.yic.xconomy.depend.Vault;
import me.yic.xconomy.listeners.ConnectionListeners;
import me.yic.xconomy.listeners.SPsync;
import me.yic.xconomy.message.Messages;
import me.yic.xconomy.message.MessagesManager;
import me.yic.xconomy.task.Baltop;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.utils.EconomyCommand;
import me.yic.xconomy.utils.UpdateConfig;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;

public class XConomy extends JavaPlugin {

	private static XConomy instance;
	public static FileConfiguration config;
	private MessagesManager messageManager;
	private static boolean foundvaultpe = false;
	public Economy econ = null;
	private BukkitTask refresherTask = null;
	Metrics metrics = null;
	private Placeholder papiExpansion = null;
	public static Boolean ddrivers = false;

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
				logger("不推荐开启 Vault 中 baltop 变量的功能");
				logger("请在 PlaceholderAPI 的 config.yml 中设置 expansions.vault.baltop.enabled 为 false");
				logger("======================================================================");
				foundvaultpe = true;
			}
			setupPlaceHolderAPI();
		}

		if (Bukkit.getPluginManager().getPlugin("DatabaseDrivers") != null) {
			logger("发现 DatabaseDrivers");
			ddrivers = true;
		}

		getServer().getServicesManager().register(Economy.class, econ, this, ServicePriority.Normal);
		getServer().getPluginManager().registerEvents(new ConnectionListeners(), this);

		if (config.getBoolean("Settings.disable-essentials")) {
			Collection<RegisteredServiceProvider<Economy>> econs = Bukkit.getPluginManager().getPlugin("Vault").getServer().getServicesManager().getRegistrations(Economy.class);
			for (RegisteredServiceProvider<Economy> econ : econs) {
				if (econ.getProvider().getName().equalsIgnoreCase("Essentials Economy")) {
					getServer().getServicesManager().unregister(econ.getProvider());
				}
			}
		}

		metrics = new Metrics(this, 6588);

		Bukkit.getPluginCommand("money").setExecutor(new Commands());
		Bukkit.getPluginCommand("balance").setExecutor(new Commands());
		Bukkit.getPluginCommand("balancetop").setExecutor(new Commands());
		Bukkit.getPluginCommand("pay").setExecutor(new Commands());
		Bukkit.getPluginCommand("xconomy").setExecutor(new Commands());

		if (config.getBoolean("Settings.eco-command")) {
			try {
				final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
				bukkitCommandMap.setAccessible(true);
				CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
				coveress(commandMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!DataCon.create()) {
			onDisable();
			return;
		}

		Cache.baltop();

		if (config.getBoolean("BungeeCord.enable")) {
			if (isBungeecord()) {
				getServer().getMessenger().registerIncomingPluginChannel(this, "xconomy:aca", new SPsync());
				getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:acb");
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
		}

		refresherTask.cancel();
		CacheSemiOnline.save();
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
		if (foundvaultpe) {
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
		papiExpansion = new Placeholder(this);
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


	public static boolean issemionlinemode() {
		return XConomy.config.getBoolean("Settings.semi-online-mode");
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
		if (!peFolder.exists()) {
			return false;
		}
		FileConfiguration peConfig = YamlConfiguration.loadConfiguration(peFolder);
		if (peConfig.contains("expansions.vault.baltop.enabled")) {
			return peConfig.getBoolean("expansions.vault.baltop.enabled");
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


	private void coveress(CommandMap commandMap) {
		Command commanda  = new EconomyCommand("economy");
		commandMap.register("economy", commanda);
		Command commandb  = new EconomyCommand("eco");
		commandMap.register("eco", commandb);
		Command commandc  = new EconomyCommand("ebalancetop");
		commandMap.register("ebalancetop", commandc);
		Command commandd  = new EconomyCommand("ebaltop");
		commandMap.register("ebaltop", commandd);
		Command commande  = new EconomyCommand("eeconomy");
		commandMap.register("eeconomy", commande);
	}
}
