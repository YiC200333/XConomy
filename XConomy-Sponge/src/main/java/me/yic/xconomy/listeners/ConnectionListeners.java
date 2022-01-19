/*
 *  This file (ConnectionListeners.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.info.ServerINFO;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.Updater;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

public class ConnectionListeners {

    @SuppressWarnings("unused")
    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        if (Sponge.getServer().getOnlinePlayers().size() == 1) {
            Cache.clearCache();
        }
    }

    @SuppressWarnings("unused")
    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player a = event.getTargetEntity();

        if (DataBaseINFO.getStorageType() == 0 || DataBaseINFO.getStorageType() == 1) {
            DataLink.newPlayer(a);
        } else {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> DataLink.newPlayer(a));
        }

        if (a.hasPermission("xconomy.admin.op")) {
            notifyUpdate(a);
        }

    }


    private void notifyUpdate(Player player) {
        if (!(XConomy.checkup() & Updater.old)) {
            return;
        }
        player.sendMessage(Text.of("§f[XConomy]§b" + MessagesManager.systemMessage("发现新版本 ") + Updater.newVersion));
        player.sendMessage(Text.of("§f[XConomy]§ahttps://ore.spongepowered.org/YiC/XConomy"));

        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            player.sendMessage(Text.of("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html"));
        }

    }
}
