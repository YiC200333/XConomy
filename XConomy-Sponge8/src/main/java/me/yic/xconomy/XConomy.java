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
import me.yic.xconomy.adapter.comp.CConfig;
import me.yic.xconomy.command.*;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.depend.economyapi.XCService;
import me.yic.xconomy.depend.economyapi.XCurrency;
import me.yic.xconomy.info.DataBaseConfig;
import me.yic.xconomy.info.DefaultConfig;
import me.yic.xconomy.info.SyncInfo;
import me.yic.xconomy.info.UpdateConfig;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.listeners.ConnectionListeners;
import me.yic.xconomy.task.Baltop;
import me.yic.xconomy.task.Updater;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ProvideServiceEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.scheduler.TaskExecutorService;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Plugin("xconomy")
public class XConomy {
    public final static String version = "Sponge8";

    private static XConomy instance;
    public PluginContainer plugincontainer;

    @SuppressWarnings("unused")
    public static String PVersion;

    public PermissionService permissionService;
    public PermissionDescription.Builder permissionDescriptionBuilder;

    public static String syncversion = SyncInfo.syncversion;

    private TaskExecutorService refresherTask;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Metrics metrics;
    public static final Currency xc = new XCurrency();

    @Inject
    private final Logger inforation;

    @Inject
    @ConfigDir(sharedRoot = false)
    public Path configDir;

    @Inject
    public XConomy(final PluginContainer container, final Logger inforation, final Metrics.Factory metricsFactory) {
        this.inforation = inforation;
        instance = this;
        this.plugincontainer = container;
        String v = plugincontainer.metadata().version().toString();
        PVersion = v.substring(0, v.length() - 8);
        metrics = metricsFactory.make(10142);
    }


    @SuppressWarnings(value = {"unused"})
    @Listener
    public void onEnable(final StartedEngineEvent<Server> event) {

        //if (Sponge.getPluginManager().getPlugin("DatabaseDrivers").isPresent()) {
        //logger("发现 DatabaseDrivers", null);
        //}
        if (XConomyLoad.Config.ISOLDCONFIG){
            logger(null, 1, "==================================================");
            logger(null, 1, "Please regenerate all configuration files");
            logger(null, 1, "==================================================");
            logger("XConomy已成功卸载", 0, null);
            return;
        }

        if (!DataLink.create()) {
            logger("XConomy已成功卸载", 0, null);
            return;
        }

        if (XConomyLoad.Config.CHECK_UPDATE) {
            Sponge.asyncScheduler().executor(plugincontainer).execute(new Updater());
        }
        // 检查更新

        Sponge.eventManager().registerListeners(plugincontainer, new ConnectionListeners());


        this.permissionService = Sponge.server().serviceProvider().permissionService();

        if (permissionService != null) {
            this.permissionDescriptionBuilder = this.permissionService.newDescriptionBuilder(plugincontainer);

            this.permissionDescriptionBuilder
                    .id("xconomy.user.balance")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.user.balance.other")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.user.pay")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.user.pay.receive")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.user.balancetop")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.user.paytoggle")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.admin.give")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.admin.take")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.admin.set")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.admin.permission")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.admin.balancetop")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permissionDescriptionBuilder
                    .id("xconomy.admin.paytoggle")
                    .description(Component.text("xconomy command"))
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
        }



        XConomyLoad.Initial();

        int time = XConomyLoad.Config.REFRESH_TIME;

        refresherTask = Sponge.asyncScheduler().executor(plugincontainer);
        refresherTask.scheduleAtFixedRate(new Baltop(), time, time, TimeUnit.SECONDS);
        //Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(Baltop::new);

        logger(null, 0, "===== YiC =====");

    }

    @SuppressWarnings("unused")
    @Listener
    public void onDisable(StoppingEngineEvent<Server> event) {

        refresherTask.shutdown();

        XConomyLoad.Unload();
        logger("XConomy已成功卸载", 0, null);
    }


    @SuppressWarnings("unused")
    @Listener
    public void onRegisterService(final ProvideServiceEvent<EconomyService> event){
        event.suggest(XCService::new);
    }


    @SuppressWarnings("unused")
    @Listener
    public void onRegisterCommand(final RegisterCommandEvent<Command.Parameterized> event){
        loadconfig();
        XConomyLoad.LoadConfig();

        final Command.Parameterized balcmd = Command.builder()
                .executor(new CommandBalance())
                .addParameters(Parameter.seq(CommandBalance.arg1, CommandBalance.arg2, CommandBalance.arg3,
                        CommandBalance.arg4, CommandBalance.arg5)).build();
        final Command.Parameterized paycmd = Command.builder()
                .executor(new CommandPay())
                .addParameters(Parameter.seq(CommandPay.arg1, CommandPay.arg2)).build();
        final Command.Parameterized baltopcmd = Command.builder()
                .executor(new CommandBaltop())
                .addParameters(Parameter.seq(CommandBaltop.arg1, CommandBaltop.arg2)).build();
        final Command.Parameterized xccmd = Command.builder()
                .executor(new CommandSystem())
                .addParameters(Parameter.seq(CommandSystem.arg1, CommandSystem.arg2)).build();
        final Command.Parameterized paypr = Command.builder()
                .executor(new CommandPermission())
                .addParameters(Parameter.seq(CommandPermission.arg1, CommandPermission.arg2, CommandPermission.arg3)).build();
        final Command.Parameterized paytog = Command.builder()
                .executor(new CommandPaytoggle())
                .addParameters(Parameter.seq(CommandPaytoggle.arg1, CommandPaytoggle.arg2)).build();

        if (XConomyLoad.Config.ECO_COMMAND) {
            event.register(plugincontainer, balcmd,
                    "balance", "bal", "money", "economy", "eeconomy", "eco");
            event.register(plugincontainer, baltopcmd,
                    "balancetop", "baltop", "ebalancetop", "ebaltop");
        } else {
            event.register(plugincontainer, balcmd, "balance", "bal", "money");
            event.register(plugincontainer, baltopcmd, "balancetop", "baltop");
        }
        event.register(plugincontainer, paycmd, "pay");
        event.register(plugincontainer, xccmd, "xconomy", "xc");
        event.register(plugincontainer, paypr, "paypermission", "payperm");
        event.register(plugincontainer, paytog, "paytoggle");
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

        DataBaseload();

    }

    @SuppressWarnings("unused")
    public File getDataFolder() {
        return configDir.toFile();
    }

    public File getPDataFolder() {
        return new File(configDir.toFile(), "playerdata");
    }

    @SuppressWarnings("ConstantConditions")
    private void configload() {
        File configpath = new File(configDir.toFile(), "config.yml");

        if (!configpath.exists()) {
            try {
                Files.copy(this.getClass().getClassLoader().getResourceAsStream("config.yml"), Paths.get(configpath.toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        DefaultConfig.config = new CConfig(configpath);
        UpdateConfig.update(DefaultConfig.config);
    }

    @SuppressWarnings("ConstantConditions")
    private void DataBaseload() {
        Path configpath = Paths.get(XConomy.getInstance().configDir + System.getProperty("file.separator") + "database.yml");
        if (!Files.exists(configpath)) {
            try {
                Files.copy(this.getClass().getClassLoader().getResourceAsStream("database.yml"), configpath);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(configpath).build();
        try {
            DataBaseConfig.config = new CConfig(loader.load());
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
