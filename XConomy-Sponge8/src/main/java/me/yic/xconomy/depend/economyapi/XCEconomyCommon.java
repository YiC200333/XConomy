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

import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.CacheNonPlayer;

public class XCEconomyCommon {

    public static boolean SimpleCheckNonPlayerAccount(String name) {
        if (!XConomyLoad.Config.NON_PLAYER_ACCOUNT) {
            return false;
        }
        if (XConomyLoad.Config.NON_PLAYER_ACCOUNT_SUBSTRING != null) {
            return DataCon.containinfieldslist(name);
        }
        return false;
    }

    public static boolean CheckNonPlayerAccountEnable() {
        return XConomyLoad.Config.NON_PLAYER_ACCOUNT;
    }

    public static boolean isNonPlayerAccount(String name) {
        if (!XConomyLoad.Config.NON_PLAYER_ACCOUNT) {
            return false;
        }

        if (name.length() >= 17) {
            return true;
        }

        if (XConomyLoad.Config.NON_PLAYER_ACCOUNT_SUBSTRING == null) {
            if (CacheNonPlayer.bal.containsKey(name)) {
                return true;
            }

            return DataCon.getPlayerData(name) == null;
        }else{
            return DataCon.containinfieldslist(name);
        }
    }
}
