/*
 *  This file (XConomyVelocity.java) is a part of project XConomy
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
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import me.yic.xconomy.info.SyncInfo;
import me.yic.xconomy.listeners.VPlayerEvent;
import me.yic.xconomy.listeners.Vsync;
import org.slf4j.Logger;

@Plugin(id = "xconomy", name = "XConomy", version = "velocity", authors = {"YiC"})
public class XConomyVelocity{
    private static XConomyVelocity instance;
    public static String syncversion = SyncInfo.syncversion;
    public ProxyServer server;
    public Logger logger;

    public static final ChannelIdentifier aca = MinecraftChannelIdentifier.create("xconomy", "aca");
    public static final ChannelIdentifier acb = MinecraftChannelIdentifier.create("xconomy", "acb");
    public static final ChannelIdentifier global = MinecraftChannelIdentifier.create("xconomy", "global");

    @SuppressWarnings("unused")
    @Inject
    public void onEnable(ProxyServer server, Logger logger) {

        instance = this;
        this.server = server;
        this.logger = logger;


        server.getChannelRegistrar().register(aca);
        server.getChannelRegistrar().register(acb);
        server.getChannelRegistrar().register(global);

        logger.info("XConomy successfully enabled!");
        logger.info("===== YiC =====");

    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onInitialize(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new Vsync());
        server.getEventManager().register(this, new VPlayerEvent());
    }


    public static XConomyVelocity getInstance() {
        return instance;
    }
}
