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
import me.yic.xconomy.data.syncdata.SyncData;
import me.yic.xconomy.data.syncdata.SyncMessage;
import me.yic.xconomy.info.SyncType;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.network.ServerSideConnection;
import org.spongepowered.api.network.channel.ChannelBuf;
import org.spongepowered.api.network.channel.raw.play.RawPlayDataHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SPsync implements RawPlayDataHandler<ServerSideConnection> {

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    @Override
    public void handlePayload(ChannelBuf data, ServerSideConnection connection) {

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

            if (ob.getSyncType().equals(SyncType.MESSAGE) || ob.getSyncType().equals(SyncType.MESSAGE_SEMI)) {
                SyncMessage sd = (SyncMessage) ob;
                UUID muid = sd.getUniqueId();
                if (ob.getSyncType().equals(SyncType.MESSAGE_SEMI)) {
                    muid = sd.getRUniqueId();
                }
                UUID finalMuid = muid;
                User p = Sponge.server().userManager().load(finalMuid).get().get();

                if (p.isOnline()) {
                    p.player().get().sendMessage(Component.text(sd.getMessage()));
                }
            } else {
                ob.SyncStart();

            }
        } catch (IOException | ClassNotFoundException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
