/*
 *  This file (BCPlayerEvent.java) is a part of project XConomy
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

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class BCPlayerEvent implements Listener {

    @SuppressWarnings(value = {"unused"})
    @EventHandler
    public void quit(PlayerDisconnectEvent event) {
        String playername = event.getPlayer().getName();
        for (String serversign : BCsync.allservername.keySet()) {
            List<String> allname = BCsync.allservername.get(serversign);
            if (allname.contains(playername)) {
                allname.remove(playername);
                BCsync.allservername.put(serversign, allname);
            }
        }
    }

}
