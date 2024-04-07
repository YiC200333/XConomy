/*
 *  This file (PlayerConnection.java) is a part of project XConomy
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

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.syncdata.tab.SyncTabJoin;
import me.yic.xconomy.info.HiddenINFO;

public class PlayerConnection{

    public static void onJoin(CPlayer player) {

        if (XConomyLoad.DConfig.canasync) {
            AdapterManager.runTaskAsynchronously(() -> DataLink.newPlayer(player));
        } else {
            DataLink.newPlayer(player);
        }

        if (player.hasPermission("xconomy.admin.hidden")){
            AdapterManager.remove_Tab_PlayerList(player.getName());
            HiddenINFO.addHidden(player.getName());
        }else {
            if (XConomyLoad.getSyncData_Enable()) {
                DataCon.SendMessTask(new SyncTabJoin(player.getName()));
            }

            if (!AdapterManager.get_Tab_PlayerList().contains(player.getName())) {
                AdapterManager.add_Tab_PlayerList(player.getName());
            }
        }

        if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.PAY_TIPS) {
            DataLink.selectlogininfo(player);
        }

    }


}
