package me.yic.xconomy.listeners;/*
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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SPsync implements PluginMessageListener {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player arg1, byte[] message) {
        if (!channel.equals("xconomy:aca")) {
            return;
        }

        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String type = input.readUTF();
        String sign = input.readUTF();
        if (!sign.equals(XConomy.getSign())) {
            return;
        }

        if (type.equalsIgnoreCase("balance")) {
            UUID u = UUID.fromString(input.readUTF());
            String bal = input.readUTF();
            Cache.bal.put(u, DataFormat.formatString(bal));
        } else if (type.equalsIgnoreCase("message")) {
            Player p = Bukkit.getPlayer(UUID.fromString(input.readUTF()));
            String mess = input.readUTF();
            if (p != null) {
                p.sendMessage(mess);
            }
        } else if (type.equalsIgnoreCase("balanceall")) {
            String targettype = input.readUTF();
            String amount = input.readUTF();
            String isadds = input.readUTF();
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
            String mess = input.readUTF();
            Bukkit.broadcastMessage(mess);
        }
    }

}
