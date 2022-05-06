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

import me.yic.xconomy.XConomyBungee;
import me.yic.xconomy.data.syncdata.SyncMessage;
import me.yic.xconomy.info.SyncType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;

public class BCsync implements Listener {

    @SuppressWarnings(value = {"unused"})
    @EventHandler
    public void on(PluginMessageEvent event) {
        if (!(event.getSender() instanceof Server)) {
            return;
        }

        if (!event.getTag().equalsIgnoreCase("xconomy:acb")) {
            return;
        }

        ByteArrayInputStream input = new ByteArrayInputStream(event.getData());
        try {
            ObjectInputStream ios = new ObjectInputStream(input);
            Server senderServer = (Server) event.getSender();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(output);

            String sv = ios.readUTF();
            oos.writeUTF(sv);
            String svv = sv;
            if (svv.contains(".")) {
                svv = "versions §f" + svv;
            } else {
                svv = "§fold versions";
            }
            if (!sv.equals(XConomyBungee.syncversion)) {
                XConomyBungee.getInstance().getLogger().warning("§cReceived data from " + svv + ", §cunable to synchronize, Current plugin version §f" + XConomyBungee.syncversion);
                return;
            }

            Object ob = ios.readObject();
            if (ob instanceof SyncMessage) {
                SyncMessage sd = (SyncMessage) ob;
                if (sd.getSyncType().equals(SyncType.MESSAGE)) {
                    ProxiedPlayer p = ProxyServer.getInstance().getPlayer(sd.getUUID());
                    if (p == null) {
                        return;
                    }
                }
            }
            oos.writeObject(ob);
            oos.flush();
            for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
                if (!s.getName().equals(senderServer.getInfo().getName()) && s.getPlayers().size() > 0) {
                    ProxyServer.getInstance().getScheduler().runAsync(XConomyBungee.getInstance(), () -> SendMessTaskB(s, output));
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void SendMessTaskB(ServerInfo s, ByteArrayOutputStream stream) {
        s.sendData("xconomy:aca", stream.toByteArray());
    }
}
