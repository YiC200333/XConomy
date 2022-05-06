/*
 *  This file (SPsync.java) is a part of project XConomy
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
import me.yic.xconomy.data.caches.CacheSemiOnline;
import me.yic.xconomy.data.syncdata.*;
import me.yic.xconomy.info.SyncType;
import me.yic.xconomy.utils.UUIDMode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

public class SPsync implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player arg1, byte[] message) {
        if (!channel.equals("xconomy:aca")) {
            return;
        }

        ByteArrayInputStream input = new ByteArrayInputStream(message);
        try {
            ObjectInputStream ios = new ObjectInputStream(input);

            String sv = ios.readUTF();
            if (!sv.equals(XConomy.syncversion)) {
                XConomy.getInstance().logger("收到不同版本插件的数据，无法同步，当前插件版本 ", 1, XConomy.syncversion);
                return;
            }

            SyncData ob = (SyncData) ios.readObject();
            if (!ob.getSign().equals(XConomy.Config.BUNGEECORD_SIGN)) {
                return;
            }

            if (ob.getSyncType().equals(SyncType.UPDATEPLAYER)) {
                SyncUpdatePlayer sd = (SyncUpdatePlayer) ob;
                UUID u = sd.getUUID();
                Cache.removefromCache(u);
            } else if (ob.getSyncType().equals(SyncType.MESSAGE) || ob.getSyncType().equals(SyncType.MESSAGE_SEMI) ) {
                SyncMessage sd = (SyncMessage) ob;
                UUID muid = sd.getUUID();
                Player p = Bukkit.getPlayer(sd.getUUID());
                if (p != null) {
                    p.sendMessage(sd.getMessage());
                } else if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    UUID suid = CacheSemiOnline.CacheSubUUID_getsubuuid(muid.toString());
                    if (suid != null) {
                        Player sp = Bukkit.getPlayer(suid);
                        if (sp != null) {
                            sp.sendMessage(sd.getMessage());
                        }
                    }
                }
            } else if (ob.getSyncType().equals(SyncType.BALANCEALL)) {
                SyncBalanceAll sd = (SyncBalanceAll) ob;
                Cache.clearCache();
                if (sd.getisOnline()) {
                    DataLink.saveall("online", null, sd.getAmount(), sd.getC(), null);
                }
            } else if (ob.getSyncType().equals(SyncType.BROADCAST)) {
                SyncMessage sd = (SyncMessage) ob;
                Bukkit.broadcastMessage(sd.getMessage());
            } else if (ob.getSyncType().equals(SyncType.SYNCONLINEUUID)) {
                SyncUUID sd = (SyncUUID) ob;
                Cache.syncOnlineUUIDCache(sd.getOldname(), sd.getNewname(), sd.getUUID());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
