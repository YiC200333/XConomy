/*
 *  This file (Vsync.java) is a part of project XConomy
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
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.yic.xconomy.XConomyVelocity;

import java.util.Optional;
import java.util.UUID;

public class Vsync {

    @SuppressWarnings(value = {"UnstableApiUsage", "unused"})
    @Subscribe
    public void on(PluginMessageEvent event) {
        if (event.getSource() instanceof Player) {
            return;
        }

        if (!event.getIdentifier().getId().equals(XConomyVelocity.acb.getId())) {
            return;
        }


        ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        ServerConnection senderServer = (ServerConnection) event.getSource();

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(input.readUTF());

        String type = input.readUTF();
        if (type.equalsIgnoreCase("updateplayer")) {
            output.writeUTF("updateplayer");
            output.writeUTF(input.readUTF());
        } else if (type.equalsIgnoreCase("message")) {
            output.writeUTF("message");
            String uid = input.readUTF();
            Optional<Player> p = XConomyVelocity.getInstance().server.getPlayer(UUID.fromString(uid));
            if (p.isPresent()) {
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
        } else if (type.equalsIgnoreCase("broadcast")) {
            output.writeUTF("broadcast");
            output.writeUTF(input.readUTF());
        }

        for (RegisteredServer s : XConomyVelocity.getInstance().server.getAllServers()) {
            if (!s.equals(senderServer.getServer()) && s.getPlayersConnected().size() > 0) {
                XConomyVelocity.getInstance().server.getScheduler().buildTask(XConomyVelocity.getInstance(), () -> SendMessTaskB(s, output)).schedule();
            }
        }

    }

    public static void SendMessTaskB(RegisteredServer s, ByteArrayDataOutput stream) {
        s.sendPluginMessage(XConomyVelocity.aca, stream.toByteArray());
    }
}
