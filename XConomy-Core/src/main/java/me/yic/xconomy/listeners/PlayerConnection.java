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
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.syncdata.tab.SyncTab;
import me.yic.xconomy.info.HiddenINFO;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.utils.RedisConnection;
import me.yic.xconomy.utils.TabListCon;

public class PlayerConnection{

    public static void onJoin(CPlayer player) {

        if (XConomyLoad.DConfig.canasync) {
            AdapterManager.runTaskAsynchronously(() -> DataLink.newPlayer(player));
        } else {
            DataLink.newPlayer(player);
        }

        if (player.hasPermission("xconomy.admin.hidden")){
            TabListCon.remove_Tab_PlayerList(player.getName());
            HiddenINFO.addHidden(player.getName());
        }else {
            TabListCon.add_Tab_PlayerList(player.getName());

            if (XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)){
                RedisConnection.insertdata("Tab_List", TabListCon.get_Tab_PlayerList(), 1000);
            }

            if (XConomyLoad.getSyncData_Enable()) {
                DataCon.SendMessTask(new SyncTab(player.getName(), true));
            }
        }

        if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.PAY_TIPS) {
            DataLink.selectlogininfo(player);
        }

    }
    public static void onQuit(CPlayer player) {

        if (AdapterManager.PLUGIN.getOnlinePlayerSize() == 1) {
            Cache.clearCache();
        }

        TabListCon.remove_Tab_PlayerList(player.getName());

        if (XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)){
            RedisConnection.insertdata("Tab_List", TabListCon.get_Tab_PlayerList(), 1000);
        }
        if (XConomyLoad.getSyncData_Enable()) {
            DataCon.SendMessTask(new SyncTab(player.getName(), false));
        }

        if (XConomyLoad.DConfig.isMySQL() && XConomyLoad.Config.PAY_TIPS) {
            DataLink.updatelogininfo(player.getUniqueId());
        }
        DataCon.removePlayerHiddenState(player.getUniqueId());

    }

}
