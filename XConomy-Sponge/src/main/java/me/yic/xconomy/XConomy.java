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

import com.google.inject.Inject;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.SQL;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.economyapi.XCAccount;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.listeners.ConnectionListeners;
import me.yic.xconomy.task.Baltop;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.utils.ServerINFO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bstats.sponge.Metrics2;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Plugin(id = "xconomy", name = "XConomy")

public class XConomy {

    private static XConomy instance;

    private MessagesManager messageManager;
    public static String version;
    public EconomyService econ = null;

    private SpongeExecutorService refresherTask;
    private Metrics2 metrics;

    @Inject
    private Logger inforation;


    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    public XConomy(Metrics2.Factory metricsFactory) {
        instance = this;
        metrics = metricsFactory.make(6588);
    }

    public static String jarPath;
    public static ConfigurationNode config;


    @SuppressWarnings("ConstantConditions")
    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        loadconfig();
        readserverinfo();
        if (checkup()) {
            Sponge.getScheduler().createAsyncExecutor(this).execute(new Updater());
        }
        // 检查更新
        messageManager = new MessagesManager(this);
        messageManager.load();

        Sponge.getServiceManager().setProvider(this, EconomyService.class, new XCAccount());
        //Sponge.getRegistry().registerModule(Currency.class, new XConomyRegistry());

        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        econ = serviceOpt.get();

        if (Sponge.getPluginManager().getPlugin("DatabaseDrivers").isPresent()) {
            logger("发现 DatabaseDrivers", null);
            ServerINFO.DDrivers = true;
        }

        Sponge.getEventManager().registerListeners(this, new ConnectionListeners());

        //if (config.getBoolean("Settings.disable-essentials")) {
        //    Collection<RegisteredServiceProvider<Economy>> econs = Bukkit.getPluginManager().getPlugin("Vault").getServer().getServicesManager().getRegistrations(Economy.class);
        //    for (RegisteredServiceProvider<Economy> econ : econs) {
        //        if (econ.getProvider().getName().equalsIgnoreCase("Essentials Economy")) {
        //            getServer().getServicesManager().unregister(econ.getProvider());
        //        }
        //    }
        //}
        //metricsFactory.make(6588);

        //Bukkit.getPluginCommand("money").setExecutor(new Commands());
        CommandSpec balcmd = CommandSpec.builder()
                .permission("xconomy.user.balance")
                .executor(new Command())
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("Player"))))
                .build();

        Sponge.getCommandManager().register(this, balcmd, "balance", "bal");
        //Bukkit.getPluginCommand("balancetop").setExecutor(new Commands());
        //Bukkit.getPluginCommand("pay").setExecutor(new Commands());
        //Bukkit.getPluginCommand("xconomy").setExecutor(new Commands());

        allowHikariConnectionPooling();
        if (!DataCon.create()) {
          // onDisable();
            return;
        }

        //Cache.baltop();

        if (config.getNode("BungeeCord","enable").getBoolean()) {
            if (isBungeecord()) {
         //       Sponge.getChannelRegistrar().registerChannel.registerIncomingPluginChannel(this, "xconomy:aca", new SPsync());
         //       getServer().getMessenger().registerOutgoingPluginChannel(this, "xconomy:acb");
         //       logger("已开启BungeeCord同步", null);
         //   } else if (!config.getBoolean("Settings.mysql")) {
         //       if (config.getString("SQLite.path").equalsIgnoreCase("Default")) {
        //            logger("SQLite文件路径设置错误", null);
        //            logger("BungeeCord同步未开启", null);
                }
        //    }
        }

        DataFormat.load();

        int time = config.getNode("Settings","refresh-time").getInt();
        if (time < 30) {
            time = 30;
        }

        refresherTask = Sponge.getScheduler().createAsyncExecutor(this);
        refresherTask.schedule(() -> {
            Task.builder().execute(new Baltop() {
        }); }, time, TimeUnit.SECONDS);

        logger(null, "===== YiC =====");

    }

    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        //getServer().getServicesManager().unregister(econ);


       // if (isBungeecord()) {
      //      getServer().getMessenger().unregisterIncomingPluginChannel(this, "xconomy:aca", new SPsync());
      //      getServer().getMessenger().unregisterOutgoingPluginChannel(this, "xconomy:acb");
       // }

        refresherTask.shutdown();
        //CacheSemiOnline.save();
        SQL.close();
        logger("XConomy已成功卸载", null);
    }

    public static XConomy getInstance() {
        return instance;
    }

    //public void reloadMessages() {
    //    messageManager.load();
    //}

    public EconomyService getEconomy() {
        return econ;
    }

    private void loadconfig() {
        if(!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        Path configpath =  Paths.get(configDir + "/config.yml");

        jarPath = "jar:" + this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        int i = jarPath.indexOf(".jar!/");
        jarPath = jarPath.substring(0,i+4);
        if(!Files.exists(configpath)){
            try{
                URL configurl = new URL(jarPath + "!/config.yml");
                Files.copy(configurl.openStream(), configpath);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        URL vurl = Sponge.getAssetManager().getAsset(this,"version.json").get().getUrl();
        //try {
        //    vurl = new URL(jarPath.substring(0,i+4) + "!/mcmod.info");
       // } catch (MalformedURLException e) {
        //    e.printStackTrace();
        //}
        HoconConfigurationLoader vloader = HoconConfigurationLoader.builder().setURL(vurl).build();
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configpath).build();
            try {
                version = vloader.load().getNode("version").getString();
                config = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public void readserverinfo() {
        ServerINFO.Lang = config.getNode("Settings","language").getString();
        ServerINFO.IsBungeeCordMode = isBungeecord();
        ServerINFO.IsSemiOnlineMode = config.getNode("Settings","semi-online-mode").getBoolean();
        ServerINFO.Sign = config.getNode("BungeeCord","sign").getString();
        ServerINFO.InitialAmount = config.getNode("Settings.initial-bal").getDouble();

        ServerINFO.RequireAsyncRun = config.getNode("Settings","mysql").getBoolean();
    }

    public static void allowHikariConnectionPooling() {
        if (!config.getNode("Settings","mysql").getBoolean()) {
            return;
        }
        ServerINFO.EnableConnectionPool = config.getNode("Pool-Settings","usepool").getBoolean();
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isBungeecord() {
        if (!config.getNode("BungeeCord","enable").getBoolean()) {
            return false;
        }

        if (config.getNode("Settings","mysql").getBoolean()) {
            return true;
        }

        return !config.getNode("Settings","mysql").getBoolean() & !config.getNode("SQLite","path").getString().equalsIgnoreCase("Default");

    }

    public void logger(String tag, String message) {
        if (tag == null) {
            inforation.info(message);
        } else {
            if (message == null) {
                inforation.info(MessagesManager.systemMessage(tag));
            } else {
                if (message.startsWith("<#>")) {
                    inforation.info(message.substring(3) + MessagesManager.systemMessage(tag));
                } else {
                    inforation.info(MessagesManager.systemMessage(tag) + message);
                }
            }
        }
    }

    public static String getSign() {
        return config.getNode("BungeeCord.sign").getString();
    }

    public static boolean checkup() {
        return config.getNode("Settings", "check-update").getBoolean();
    }

}
