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
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.network.RawDataListener;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;

import java.util.UUID;

public class SPsync implements RawDataListener {

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "NullableProblems"})
    @Override
    public void handlePayload(ChannelBuf data, RemoteConnection connection, Platform.Type side) {
        String type = data.readUTF();
        String sign = data.readUTF();
        if (!sign.equals(XConomy.getSign())) {
            return;
        }

        if (type.equalsIgnoreCase("balance")) {
            UUID u = UUID.fromString(data.readUTF());
            String bal = data.readUTF();
            Cache.bal.put(u, DataFormat.formatString(bal));
        } else if (type.equalsIgnoreCase("message")) {
            User p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(
                    provide -> provide.get(UUID.fromString(data.readUTF()))).get();

            String mess = data.readUTF();
            if (p.isOnline()) {
                p.getPlayer().get().sendMessage(Text.of(mess));
            }
        } else if (type.equalsIgnoreCase("balanceall")) {
            String targettype = data.readUTF();
            String amount = data.readUTF();
            String isadds = data.readUTF();
            if (targettype.equalsIgnoreCase("all")) {
                Cache.bal.clear();
            } else if (targettype.equalsIgnoreCase("online")) {
                Cache.bal.clear();
                Boolean isadd = null;
                if (isadds.equalsIgnoreCase("add")) {
                    isadd = true;
                } else if (isadds.equalsIgnoreCase("subtract")) {
                    isadd = false;
                }
                DataCon.saveall("online", null, DataFormat.formatString(amount), isadd, null);
            }
        } else if (type.equalsIgnoreCase("broadcast")) {
            String mess = data.readUTF();
            Sponge.getServer().getBroadcastChannel().send(Text.of(mess));
        } else if (type.equalsIgnoreCase("updateplayer")) {
            String u = data.readUTF();
            if (!Cache.uid.containsKey(u)) {
                return;
            }
            Cache.uid.remove(u);
        }
    }
}
