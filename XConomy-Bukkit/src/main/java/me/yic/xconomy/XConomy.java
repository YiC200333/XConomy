/*
 *  This file (XConomy.java) is a part of project XConomy
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.Baltop;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.utils.EconomyCommand;
import me.yic.xconomy.utils.ServerINFO;
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
import java.util.Collections;

public class XConomy extends JavaPlugin {

    private static XConomy instance;
    public static FileConfiguration config;
    private MessagesManager messageManager;
    private static boolean foundvaultpe = false;
    public Economy econ = null;
    private BukkitTask refresherTask = null;
    Metrics metrics = null;
    private Placeholder papiExpansion = null;

    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        instance = this;
        load();
        readserverinfo();
        if (checkup()) {
            new Updater().runTaskAsynchronously(this);
        }
        // 检查更新
        messageManager = new MessagesManager(this);
        messageManager.load();
        econ = new Vault();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            logger("发现 PlaceholderAPI", null);
            if (checkVaultPE()) {
                logger(null, String.join("", Collections.nCopies(70, "=")));
                logger("vault-baltop-tips-a", null);
                logger("vault-baltop-tips-b", null);
                logger(null, String.join("", Collections.nCopies(70, "=")));
                foundvaultpe = true;
            }
            setupPlaceHolderAPI();
        }

        if (Bukkit.getPluginManager().getPlugin("DatabaseDrivers") != null) {
            logger("发现 DatabaseDrivers", null);
            ServerINFO.DDrivers = true;
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

        allowHikariConnectionPooling();
        if (!DataCon.create()) {
            onDisable();
            return;
        }

        Cache.baltop();

        if (config.getBoolean("BungeeCord.enable")) {
            if (isBungeecord()) {
                getServer().getMessenger().registerIncomingPluginChannel(this, "xconomy:aca", new SPsync());
                getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:acb");
                logger("已开启BungeeCord同步", null);
            } else if (!config.getBoolean("Settings.mysql")) {
                if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
                    logger("SQLite文件路径设置错误", null);
                    logger("BungeeCord同步未开启", null);
                }
            }
        }

        DataFormat.load();

        int time = config.getInt("Settings.refresh-time");
        if (time < 30) {
            time = 30;
        }

        refresherTask = new Baltop().runTaskTimerAsynchronously(this, time * 20, time * 20);
        logger(null, "===== YiC =====");

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
        logger("XConomy已成功卸载", null);
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

    public void readserverinfo() {
        ServerINFO.Lang = config.getString("Settings.language");
        ServerINFO.IsBungeeCordMode = isBungeecord();
        ServerINFO.IsSemiOnlineMode = config.getBoolean("Settings.semi-online-mode");
        ServerINFO.Sign = config.getString("BungeeCord.sign");
        ServerINFO.InitialAmount = config.getDouble("Settings.initial-bal");
    }

    public static void allowHikariConnectionPooling() {
        if (foundvaultpe) {
            return;
        }
        if (!config.getBoolean("Settings.mysql")) {
            return;
        }
        ServerINFO.EnableConnectionPool = XConomy.config.getBoolean("Pool-Settings.usepool");
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

    public void logger(String tag, String message) {
        if (tag == null) {
            getLogger().info(message);
        } else {
            if (message == null) {
                getLogger().info(MessagesManager.systemMessage(tag));
            } else {
                if (message.startsWith("<#>")) {
                    getLogger().info(message.substring(2) + MessagesManager.systemMessage(tag));
                } else {
                    getLogger().info(MessagesManager.systemMessage(tag) + message);
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
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

    @SuppressWarnings("ConstantConditions")
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
        Command commanda = new EconomyCommand("economy");
        commandMap.register("economy", commanda);
        Command commandb = new EconomyCommand("eco");
        commandMap.register("eco", commandb);
        Command commandc = new EconomyCommand("ebalancetop");
        commandMap.register("ebalancetop", commandc);
        Command commandd = new EconomyCommand("ebaltop");
        commandMap.register("ebaltop", commandd);
        Command commande = new EconomyCommand("eeconomy");
        commandMap.register("eeconomy", commande);
    }
}
