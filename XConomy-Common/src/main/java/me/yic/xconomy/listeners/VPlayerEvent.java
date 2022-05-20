/*
 *  This file (VPlayerEvent.java) is a part of project XConomy
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
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.yic.xconomy.XConomyVelocity;

public class VPlayerEvent{

    @SuppressWarnings(value = {"UnstableApiUsage", "unused"})
    @Subscribe
    public void join(ServerPostConnectEvent event) {
        String playername = event.getPlayer().getUsername();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Join");
        output.writeUTF(playername);

        for (RegisteredServer s : XConomyVelocity.getInstance().server.getAllServers()) {
            if (s.getPlayersConnected().size() > 0) {
                XConomyVelocity.getInstance().server.getScheduler().buildTask(XConomyVelocity.getInstance(), () -> SendMessTaskB(s, output)).schedule();
            }
        }

    }

    @SuppressWarnings(value = {"UnstableApiUsage", "unused"})
    @Subscribe
    public void quit(DisconnectEvent event) {
        String playername = event.getPlayer().getUsername();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("Quit");
        output.writeUTF(playername);

        for (RegisteredServer s : XConomyVelocity.getInstance().server.getAllServers()) {
            if (s.getPlayersConnected().size() > 0) {
                XConomyVelocity.getInstance().server.getScheduler().buildTask(XConomyVelocity.getInstance(), () -> SendMessTaskB(s, output)).schedule();
            }
        }

    }

    public static void SendMessTaskB(RegisteredServer s, ByteArrayDataOutput stream) {
        try {
            s.sendPluginMessage(XConomyVelocity.global, stream.toByteArray());
        }catch (IllegalStateException ignored){
        }
    }
}
