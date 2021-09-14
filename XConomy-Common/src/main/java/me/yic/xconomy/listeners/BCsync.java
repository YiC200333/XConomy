/*
 *  This file (BCsync.java) is a part of project XConomy
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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.XConomyBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class BCsync implements Listener {

    @SuppressWarnings(value = {"UnstableApiUsage", "unused"})
    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!(event.getSender() instanceof Server)) {
            return;
        }

        if (!event.getTag().equalsIgnoreCase("xconomy:acb")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        Server senderServer = (Server) event.getSender();
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        String type = input.readUTF();
        if (type.equalsIgnoreCase("balance")) {
            output.writeUTF("balance");
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
        } else if (type.equalsIgnoreCase("message")) {
            output.writeUTF("message");
            output.writeUTF(input.readUTF());
            String uid = input.readUTF();
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(UUID.fromString(uid));
            if (p != null) {
                output.writeUTF(uid);
                output.writeUTF(input.readUTF());
            } else {
                return;
            }
        } else if (type.equalsIgnoreCase("balanceall")) {
            output.writeUTF("balanceall");
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
        } else if (type.equalsIgnoreCase("broadcast")) {
            output.writeUTF("broadcast");
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
        } else if (type.equalsIgnoreCase("updateplayer")) {
            output.writeUTF("updateplayer");
            output.writeUTF(input.readUTF());
            output.writeUTF(input.readUTF());
        }

        for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
            if (!s.getName().equals(senderServer.getInfo().getName()) && s.getPlayers().size() > 0) {
                ProxyServer.getInstance().getScheduler().runAsync(XConomyBungee.getInstance(), () -> SendMessTaskB(s, output));
            }
        }

    }

    public static void SendMessTaskB(ServerInfo s, ByteArrayDataOutput stream) {
        s.sendData("xconomy:aca", stream.toByteArray());
    }
}
