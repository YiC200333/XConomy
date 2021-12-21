/*
 *  This file (XCEconomyCommon.java) is a part of project XConomy
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
package me.yic.xconomy.depend.economyapi;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.CacheNonPlayer;

public class XCEconomyCommon {

    public static boolean isNonPlayerAccount(String name) {
        if (!XConomy.config.getNode("Settings","non-player-account").getBoolean()) {
            return false;
        }

        if (name.length() >= 17) {
            return true;
        }

        if (CacheNonPlayer.bal.containsKey(name)) {
            return true;
        }

        return DataCon.getPlayerData(name).getUniqueId() == null;
    }
}
