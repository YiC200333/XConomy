/*
 *  This file (SPPsync.java) is a part of project XConomy
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
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class SPPsync implements PluginMessageListener {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player arg1, byte[] message) {
        if (!channel.equals("xconomy:global")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String type = input.readUTF();
        String name = input.readUTF();

        if (type.equalsIgnoreCase("Join")) {
            if (!TabList.PlayerList.contains(name)) {
                TabList.PlayerList.add(name);
            }
        }
        if (type.equalsIgnoreCase("Quit")) {
            TabList.PlayerList.remove(name);
        }
    }

}
