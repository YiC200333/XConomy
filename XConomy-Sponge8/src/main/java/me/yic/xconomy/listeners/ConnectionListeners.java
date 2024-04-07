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

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.syncdata.tab.SyncTabQuit;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.Updater;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class ConnectionListeners {

    @SuppressWarnings("unused")
    @Listener
    public void onQuit(ServerSideConnectionEvent.Disconnect event) {
        if (Sponge.server().onlinePlayers().size() == 1) {
            Cache.clearCache();
        }

        if (event.player().profile().name().isPresent()) {
            if (XConomyLoad.getSyncData_Enable() && XConomyLoad.Config.SYNCDATA_TYPE == SyncChannalType.REDIS) {
                DataCon.SendMessTask(new SyncTabQuit(event.player().profile().name().get()));
            }
            AdapterManager.remove_Tab_PlayerList(event.player().profile().name().get());

        }

        if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.PAY_TIPS) {
            DataLink.updatelogininfo(event.player().profile().uuid());
        }
        DataCon.removePlayerHiddenState(event.player().profile().uuid());
    }

    @SuppressWarnings("unused")
    @Listener
    public void onJoin(ServerSideConnectionEvent.Join event) {
        CPlayer a = new CPlayer(event.player());

        PlayerConnection.onJoin(a);

        if (a.hasPermission("xconomy.admin.op")) {
            notifyUpdate(a);
        }

    }


    private void notifyUpdate(CPlayer player) {
        if (!(XConomyLoad.Config.CHECK_UPDATE & Updater.old)) {
            return;
        }
        player.sendMessage("§f[XConomy]§b" + MessagesManager.systemMessage("发现新版本 ") + Updater.newVersion);
        player.sendMessage("§f[XConomy]§ahttps://ore.spongepowered.org/YiC/XConomy");

        if (XConomyLoad.Config.LANGUAGE.equalsIgnoreCase("Chinese")
                | XConomyLoad.Config.LANGUAGE.equalsIgnoreCase("ChineseTW")) {
            player.sendMessage("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html");
        }

    }
}
