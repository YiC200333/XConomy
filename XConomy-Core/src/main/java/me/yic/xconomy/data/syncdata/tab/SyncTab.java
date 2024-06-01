/*
 *  This file (SyncTab.java) is a part of project XConomy
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
package me.yic.xconomy.data.syncdata.tab;

import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.syncdata.SyncData;
import me.yic.xconomy.info.HiddenINFO;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.info.SyncType;
import me.yic.xconomy.utils.TabListCon;

import java.util.List;

public class SyncTab extends SyncData {
    List<String> allname;
    List<String> hidename;
    private final String name;
    private final boolean isadd;

    public SyncTab(String name, boolean isadd){
        super(SyncType.TAB_JOIN, null);
        this.name = name;
        this.isadd = isadd;
        this.hidename = HiddenINFO.getHidList();
    }

    public void setallPlayers(List<String> allname){
        this.allname = allname;
    }
    public String getName(){
        return this.name;
    }

    public boolean getisAdd(){
        return this.isadd;
    }

    public boolean isinHidList(String name){
        return hidename.contains(name);
    }

    @Override
    public void SyncStart() {
        if (XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)){
            TabListCon.redis_sync_Tab_PlayerList();
            return;
        }
        for (String hn : hidename){
            HiddenINFO.addHidden(hn);
        }
        if (allname != null && allname.size() > 0) {
            TabListCon.renew_Tab_PlayerList(allname);
        }
        if (getisAdd() && !TabListCon.get_Tab_PlayerList().contains(name)){
            TabListCon.add_Tab_PlayerList(name);
        }
    }
}
