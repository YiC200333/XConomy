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

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

import java.util.List;

public class VPlayerEvent{

    @SuppressWarnings(value = {"unused"})
    @Subscribe
    public void quit(DisconnectEvent event) {
        String playername = event.getPlayer().getUsername();
        for (String serversign : Vsync.allservername.keySet()) {
            List<String> allname = Vsync.allservername.get(serversign);
            if (allname.contains(playername)) {
                allname.remove(playername);
                Vsync.allservername.put(serversign, allname);
            }
        }
    }
}
