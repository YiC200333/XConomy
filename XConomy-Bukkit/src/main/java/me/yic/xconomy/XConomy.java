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

import me.yic.xconomy.adapter.comp.CConfig;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.ImportData;
import me.yic.xconomy.depend.LoadEconomy;
import me.yic.xconomy.depend.NonPlayerPlugin;
import me.yic.xconomy.depend.Placeholder;
import me.yic.xconomy.depend.economy.VaultHook;
import me.yic.xconomy.info.*;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.listeners.ConnectionListeners;
import me.yic.xconomy.listeners.TabList;
import me.yic.xconomy.task.RunBaltop;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.utils.EconomyCommand;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;

public class XConomy extends JavaPlugin {

    public final static String version = "Bukkit";
    public static String PVersion;
    private static XConomy instance;

    public static String syncversion = SyncInfo.syncversion;
    Metrics metrics = null;
    private Placeholder papiExpansion = null;

    private ImportData itd = null;

    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        instance = this;
        PVersion = getInstance().getDescription().getVersion();

        load();

        XConomyLoad.LoadConfig();

        if (XConomyLoad.Config.ISOLDCONFIG) {
            getLogger().warning("==================================================");
            getLogger().warning("Please regenerate all configuration files");
            getLogger().warning("==================================================");
            logger("XConomy已成功卸载", 0, null);
            return;
        }

        if (XConomyLoad.Config.IMPORTMODE) {
            itd = new ImportData(this);
            itd.onEnable();
            return;
        }


        MCVersion.MCVersion = Bukkit.getBukkitVersion().toLowerCase();
        MCVersion.chatcolorcheck();

        if (!LoadEconomy.load()) {
            getLogger().warning("No supported dependent plugins were found");
            getLogger().warning("[ Vault ][ Enterprise ]");
            logger("XConomy已成功卸载", 0, null);
            return;
        }

        AdapterManager.foundvaultOfflinePermManager = checkVaultOfflinePermManager();

        NonPlayerPlugin.load();

        if (!DataLink.create()) {
            logger("XConomy已成功卸载", 0, null);
            return;
        }

        if (XConomyLoad.Config.CHECK_UPDATE) {
            AdapterManager.runTaskAsynchronously(new Updater());
        }
        // 检查更新

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            logger("发现 PlaceholderAPI", 0, null);
            if (checkVaultPE()) {
                logger(null, 0, String.join("", Collections.nCopies(70, "=")));
                logger("vault-baltop-tips-a", 0, null);
                logger("vault-baltop-tips-b", 0, null);
                logger(null, 0, String.join("", Collections.nCopies(70, "=")));
                AdapterManager.foundvaultpe = true;
            }
            setupPlaceHolderAPI();
        }

        getServer().getPluginManager().registerEvents(new ConnectionListeners(), this);


        metrics = new Metrics(this, 6588);
        metrics.addCustomChart(new SimplePie("uuid-mode", () -> XConomyLoad.Config.UUIDMODE.toString().substring(11)));

        Bukkit.getPluginCommand("money").setExecutor(new Commands());
        Bukkit.getPluginCommand("balance").setExecutor(new Commands());
        Bukkit.getPluginCommand("balancetop").setExecutor(new Commands());
        Bukkit.getPluginCommand("pay").setExecutor(new Commands());
        Bukkit.getPluginCommand("xconomy").setExecutor(new Commands());
        Bukkit.getPluginCommand("paytoggle").setExecutor(new Commands());
        Bukkit.getPluginCommand("paypermission").setExecutor(new Commands());

        this.getCommand("money").setTabCompleter(new TabList());
        this.getCommand("balance").setTabCompleter(new TabList());
        this.getCommand("balancetop").setTabCompleter(new TabList());
        this.getCommand("pay").setTabCompleter(new TabList());
        this.getCommand("xconomy").setTabCompleter(new TabList());
        this.getCommand("paytoggle").setTabCompleter(new TabList());
        this.getCommand("paypermission").setTabCompleter(new TabList());
        this.getCommand("paypermission").setPermission("xconomy.admin.permission");

        if (XConomyLoad.Config.ECO_COMMAND) {
            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                coveress(commandMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        XConomyLoad.Initial();

        RunBaltop.runstart();
        logger(null, 0, "===== YiC =====");
    }

    public void onDisable() {

        if (XConomyLoad.Config.IMPORTMODE) {
            itd.onDisable();
            return;
        }

        LoadEconomy.unload();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && papiExpansion != null) {
            try {
                papiExpansion.unregister();
            } catch (NoSuchMethodError ignored) {
            }
        }

        RunBaltop.stop();
        XConomyLoad.Unload();
        logger("XConomy已成功卸载", 0, null);
    }

    public static XConomy getInstance() {
        return instance;
    }


    private void setupPlaceHolderAPI() {
        papiExpansion = new Placeholder(this);
        if (papiExpansion.register()) {
            getLogger().info("PlaceholderAPI successfully hooked");
        } else {
            getLogger().warning("PlaceholderAPI unsuccessfully hooked");
        }
    }

    public void logger(String tag, int type, String message) {
        String mess = message;
        if (tag != null) {
            if (message == null) {
                mess = MessagesManager.systemMessage(tag);
            } else {
                if (message.startsWith("<#>")) {
                    mess = message.substring(3) + MessagesManager.systemMessage(tag);
                } else {
                    mess = MessagesManager.systemMessage(tag) + message;
                }
            }
        }
        if (type == 1) {
            getLogger().warning(mess);
        } else {
            getLogger().info(mess);
        }
    }


    private void load() {
        saveDefaultConfig();
        update_config();
        reloadConfig();

        File config = new File(this.getDataFolder(), "config.yml");
        DefaultConfig.config = new CConfig(config);

        File file = new File(XConomy.getInstance().getDataFolder(), "database.yml");
        if (!file.exists()) {
            XConomy.getInstance().saveResource("database.yml", false);
        }
        DataBaseConfig.config = new CConfig(YamlConfiguration.loadConfiguration(file));
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
        UpdateConfig.update(new CConfig(config));
    }

    public File getPDataFolder() {
        return new File(XConomy.getInstance().getDataFolder(), "playerdata");
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

    @SuppressWarnings("all")
    private boolean checkVaultOfflinePermManager() {
        // Check if vault is linked to a permission system that supports offline player checks.
        if (LoadEconomy.vault && VaultHook.vaultPerm != null) {
            switch (VaultHook.vaultPerm.getName()) {
                // Add other plugins that also have an offline player permissions manager.
                case "LuckPerms":
                    return true;
            }
        }
        return false;
    }
}
