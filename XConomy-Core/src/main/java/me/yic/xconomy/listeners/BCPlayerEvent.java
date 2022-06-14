/*
 *  This file (BCPlayerEvent.java) is a part of project XConomy
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
package me.yic.xconomy.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.XConomyBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BCPlayerEvent implements Listener {

    @SuppressWarnings(value = {"UnstableApiUsage", "unused"})
    @EventHandler
    public void join(ServerConnectedEvent event) {
        String playername = event.getPlayer().getName();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();

            output.writeUTF("Join");
            output.writeUTF(playername);

        for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
            if (s.getPlayers().size() > 0) {
                ProxyServer.getInstance().getScheduler().runAsync(XConomyBungee.getInstance(), () -> SendMessTaskB(s, output));
            }
        }

    }

    @SuppressWarnings(value = {"UnstableApiUsage", "unused"})
    @EventHandler
    public void quit(PlayerDisconnectEvent event) {
        String playername = event.getPlayer().getName();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Quit");
        output.writeUTF(playername);

        for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
            if (s.getPlayers().size() > 0) {
                ProxyServer.getInstance().getScheduler().runAsync(XConomyBungee.getInstance(), () -> SendMessTaskB(s, output));
            }
        }

    }

    public static void SendMessTaskB(ServerInfo s, ByteArrayDataOutput stream) {
        s.sendData("xconomy:global", stream.toByteArray());
    }
}
