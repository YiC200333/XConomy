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
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.Updater;
import me.yic.xconomy.info.DataBaseINFO;
import me.yic.xconomy.info.ServerINFO;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() == 1) {
            Cache.clearCache();
        }
        if (!ServerINFO.IsBungeeCordMode) {
            TabList.PlayerList.remove(event.getPlayer().getName());
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player a = event.getPlayer();
        Cache.removefromCache(a.getUniqueId());
        if (DataBaseINFO.getStorageType() == 0 || DataBaseINFO.getStorageType() == 1) {
            DataCon.newPlayer(a);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), () -> DataCon.newPlayer(a));
        }

        if (!XConomy.config.getBoolean("Settings.semi-online-mode")) {
            Cache.translateUUID(a.getName(), a);
        }

        if (!TabList.PlayerList.contains(a.getName())) {
            TabList.PlayerList.add(a.getName());
        }

        if (a.isOp()) {
            notifyUpdate(a);
        }
    }


    private void notifyUpdate(Player player) {
        if (!(XConomy.checkup() & Updater.old)) {
            return;
        }
        player.sendMessage("§f[XConomy]§b" + MessagesManager.systemMessage("发现新版本 ") + Updater.newVersion);
        player.sendMessage("§f[XConomy]§ahttps://www.spigotmc.org/resources/xconomy.75669/");

        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            player.sendMessage("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html");
        }

    }

}
