/*
 *  This file (SyncTabJoin.java) is a part of project XConomy
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

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.data.syncdata.SyncData;
import me.yic.xconomy.info.SyncType;

import java.util.List;

public class SyncTabJoin extends SyncData {

    private final String name;
    List<String> allname;

    public SyncTabJoin(String name){
        super(SyncType.TAB_JOIN, null);
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setallPlayers(List<String> allname){
        this.allname = allname;
    }

    @Override
    public void SyncStart() {
        if (allname == null) {
            AdapterManager.add_Tab_PlayerList(name);
        }else{
            for (String pn : allname){
                AdapterManager.add_Tab_PlayerList(pn);
            }
        }
    }
}
