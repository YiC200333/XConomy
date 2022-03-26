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
import me.yic.libs.bstats.sponge.Metrics2;
import me.yic.xconomy.command.CommandBalance;
import me.yic.xconomy.command.CommandBaltop;
import me.yic.xconomy.command.CommandPay;
import me.yic.xconomy.command.CommandSystem;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.depend.economyapi.XCService;
import me.yic.xconomy.depend.economyapi.XCurrency;
import me.yic.xconomy.info.DataBaseConfig;
import me.yic.xconomy.info.DefaultConfig;
import me.yic.xconomy.info.SyncInfo;
import me.yic.xconomy.info.UpdateConfig;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.listeners.ConnectionListeners;
import me.yic.xconomy.listeners.SPsync;
import me.yic.xconomy.task.Baltop;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.utils.PluginINFO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Plugin(id = "xconomy", name = "XConomy", version = PluginINFO.VERSION, authors = {"YiC"})

public class XConomy {

    private static XConomy instance;
    public static DataBaseConfig DConfig;
    public static DefaultConfig Config;


    public static String syncversion = SyncInfo.syncversion;

    private SpongeExecutorService refresherTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Metrics2 metrics;
    private ChannelBinding.RawDataChannel channel;
    private RawDataListener channellistener;

    public static final XCurrency xc = new XCurrency();

    @Inject
    private final Logger inforation;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    public XConomy(Metrics2.Factory metricsFactory, Logger inforation) {
        this.inforation = inforation;
        instance = this;
        metrics = metricsFactory.make(10142);
    }

    public static String jarPath;


    @SuppressWarnings(value = {"unused"})
    @Listener
    public void onEnable(GamePreInitializationEvent event) {
        loadconfig();

        MessagesManager.load();

        Sponge.getServiceManager().setProvider(this, EconomyService.class, new XCService());


        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            logger(null, 1, "EconomyService is null");
            logger("XConomy已成功卸载", 0, null);
            return;
        }
        serviceOpt.get().getCurrencies().add(xc);

        //if (Sponge.getPluginManager().getPlugin("DatabaseDrivers").isPresent()) {
        //logger("发现 DatabaseDrivers", null);
        //}

        if (!DataLink.create()) {
            logger("XConomy已成功卸载", 0, null);
            return;
        }

        DataCon.baltop();

        if (Config.CHECK_UPDATE) {
            Sponge.getScheduler().createAsyncExecutor(this).execute(new Updater());
        }
        // 检查更新


        Sponge.getEventManager().registerListeners(this, new ConnectionListeners());

        CommandSpec balcmd = CommandSpec.builder()
                .executor(new CommandBalance())
                .arguments(GenericArguments.seq(GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg1"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg2"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg3"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg4"))),
                        GenericArguments.optionalWeak(GenericArguments.remainingJoinedStrings(Text.of("arg5")))))
                .build();

        CommandSpec paycmd = CommandSpec.builder()
                .executor(new CommandPay())
                .arguments(GenericArguments.seq(GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg1"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg2")))))
                .build();
        CommandSpec baltopcmd = CommandSpec.builder()
                .executor(new CommandBaltop())
                .arguments(GenericArguments.seq(GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg1"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg2")))))
                .build();
        CommandSpec xccmd = CommandSpec.builder()
                .executor(new CommandSystem())
                .arguments(GenericArguments.seq(GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg1"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg2"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg3"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg4"))),
                        GenericArguments.optionalWeak(GenericArguments.string(Text.of("arg5")))))
                .build();

        if (Config.ECO_COMMAND) {
            Sponge.getCommandManager().register(this, balcmd,
                    "balance", "bal", "money", "economy", "eeconomy", "eco");
            Sponge.getCommandManager().register(this, baltopcmd,
                    "balancetop", "baltop", "ebalancetop", "ebaltop");
        } else {
            Sponge.getCommandManager().register(this, balcmd, "balance", "bal", "money");
            Sponge.getCommandManager().register(this, baltopcmd, "balancetop", "baltop");
        }
        Sponge.getCommandManager().register(this, paycmd, "pay");
        Sponge.getCommandManager().register(this, xccmd, "xconomy", "xc");


        if (Config.BUNGEECORD_ENABLE) {
            channel = Sponge.getChannelRegistrar().createRawChannel(this, "xconomy:aca");
            channellistener = new SPsync();
            channel.addListener(Platform.Type.SERVER, channellistener);
            Sponge.getChannelRegistrar().createRawChannel(this, "xconomy:acb");
            logger("已开启BungeeCord同步", 0, null);
        } else if (DConfig.getStorageType() == 0 || DConfig.getStorageType() == 1) {
            if (DConfig.gethost().equalsIgnoreCase("Default")) {
                logger("SQLite文件路径设置错误", 1, null);
                logger("BungeeCord同步未开启", 1, null);
            }
        }

        DataFormat.load();

        int time = Config.REFRESH_TIME;

        refresherTask = Sponge.getScheduler().createAsyncExecutor(this);
        refresherTask.schedule(() -> {
            Task.builder().execute(new Baltop() {
            });
        }, time, TimeUnit.SECONDS);

        logger(null, 0, "===== YiC =====");

    }

    @SuppressWarnings("unused")
    @Listener
    public void onDisable(GameStoppingServerEvent event) {
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        serviceOpt.ifPresent(economyService -> economyService.getCurrencies().remove(xc));

        if (Config.BUNGEECORD_ENABLE) {
            channel.removeListener(channellistener);
        }

        refresherTask.shutdown();
        CacheSemiOnline.save();
        SQL.close();
        logger("XConomy已成功卸载", 0, null);
    }

    public static XConomy getInstance() {
        return instance;
    }

    private void loadconfig() {
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }

        configload();
        Config = new DefaultConfig();

        DataBaseload();
        DConfig = new DataBaseConfig();

        Config.setBungeecord();


    }

    private void configload() {
        File configpath = new File(configDir.toFile(), "config.yml");

        jarPath = "jar:" + this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        int i = jarPath.indexOf(".jar!/");
        jarPath = jarPath.substring(0, i + 4);
        if (!configpath.exists()) {
            try {
                URL configurl = new URL(jarPath + "!/config.yml");
                Files.copy(configurl.openStream(), Paths.get(configpath.toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ConfigurationLoader<ConfigurationNode> loader =
                YAMLConfigurationLoader.builder().setFlowStyle(DumperOptions.FlowStyle.BLOCK).setIndent(2).setFile(configpath).build();
        try {
            DefaultConfig.config = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (UpdateConfig.update(DefaultConfig.config)) {
            try {
                loader.save(DefaultConfig.config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void DataBaseload() {
        Path configpath = Paths.get(XConomy.getInstance().configDir + System.getProperty("file.separator") + "database.yml");
        if (!Files.exists(configpath)) {
            try {
                URL configurl = new URL(XConomy.jarPath + "!/database.yml");
                Files.copy(configurl.openStream(), configpath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configpath).build();
        try {
            DataBaseConfig.config = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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
            inforation.warn(mess);
        } else {
            inforation.info(mess);
        }
    }

}
