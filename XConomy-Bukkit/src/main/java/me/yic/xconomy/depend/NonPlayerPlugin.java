/*
 *  This file (NonPlayerPlugin.java) is a part of project XConomy
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

package me.yic.xconomy.depend;

import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.depend.nonplayer.Towny;
import org.bukkit.Bukkit;

public class NonPlayerPlugin {

    public static boolean towny = false;

    public static void load() {
        if (Bukkit.getPluginManager().getPlugin("Towny") != null) {
            DataLink.hasnonplayerplugin = true;
            towny = true;
        }
    }

    public static boolean containinfields(String accountname) {
        if (towny) {
            return accountname.startsWith(Towny.getNationAccountPrefix()) || accountname.startsWith(Towny.getTownAccountPrefix());
        }
        return false;
    }

    public static boolean SimpleCheckNonPlayerAccount(String name) {
        if (containinfields(name)) {
            return true;
        }
        if (!XConomyLoad.Config.NON_PLAYER_ACCOUNT) {
            return false;
        }
        if (XConomyLoad.Config.NON_PLAYER_ACCOUNT_SUBSTRING != null) {
            return DataCon.containinfieldslist(name);
        }
        return false;
    }
}
