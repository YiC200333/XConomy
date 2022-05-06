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
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.info.SyncType;
import me.yic.xconomy.utils.UUIDMode;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

public class SPsync implements RawDataListener {

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    @Override
    public void handlePayload(ChannelBuf data, @NotNull RemoteConnection connection, @NotNull Platform.Type side) {

        byte[] msgbytes = data.readBytes(data.available());
        ByteArrayInputStream input = new ByteArrayInputStream(msgbytes);
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
                User p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(
                        provide -> provide.get(muid)).get();

                if (p.isOnline()) {
                    p.getPlayer().get().sendMessage(Text.of(sd.getMessage()));
                } else if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    UUID suid = CacheSemiOnline.CacheSubUUID_getsubuuid(muid.toString());
                    if (suid != null) {
                        User sp = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(
                                provide -> provide.get(suid)).get();
                        if (sp.isOnline()) {
                            sp.getPlayer().get().sendMessage(Text.of(sd.getMessage()));
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
                Sponge.getServer().getBroadcastChannel().send(Text.of(sd.getMessage()));
            } else if (ob.getSyncType().equals(SyncType.SYNCONLINEUUID)) {
                SyncUUID sd = (SyncUUID) ob;
                Cache.syncOnlineUUIDCache(sd.getOldname(), sd.getNewname(), sd.getUUID());
            } else if (ob.getSyncType().equals(SyncType.PERMISSION)) {
                SyncPermission sd = (SyncPermission) ob;
                if (sd.getType() == 1){
                    if (sd.getUUID() == null){
                        PermissionINFO.globalpayment = sd.getValue();
                    }else{
                        PermissionINFO.setPaymentPermission(sd.getUUID(), sd.getValue());
                    }
                }else{
                    PermissionINFO.setRPaymentPermission(sd.getUUID(), sd.getValue());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
