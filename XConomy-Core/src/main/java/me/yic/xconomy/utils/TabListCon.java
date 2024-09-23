/*
 *  This file (TabListCon.java) is a part of project XConomy
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
package me.yic.xconomy.utils;

import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.info.HiddenINFO;

import java.util.ArrayList;
import java.util.List;

public class TabListCon {

    private static List<String> Tab_PlayerList = new ArrayList<>();

    public static void add_Tab_PlayerList(String name) {
        if (!Tab_PlayerList.contains(name)) {
            if (name != null && !HiddenINFO.getHidden(name)) {
                if (XConomyLoad.getSyncData_Enable()) {
                    ArrayList<String> copiedList = new ArrayList<>(Tab_PlayerList);
                    copiedList.add(name);
                    renew_Tab_PlayerList(copiedList);
                } else {
                    Tab_PlayerList.add(name);
                }
            }
        }
    }

    public static void renew_Tab_PlayerList(List<String> rn) {
        if (rn != null) {
            Tab_PlayerList = rn;
        }
    }

    public static List<String> get_Tab_PlayerList() {
        return Tab_PlayerList;
    }

    public static void remove_Tab_PlayerList(String name) {
        if (XConomyLoad.getSyncData_Enable()) {
            ArrayList<String> copiedList = new ArrayList<>(Tab_PlayerList);
            copiedList.removeIf(ee -> ee == null || ee.equals(name));
            renew_Tab_PlayerList(copiedList);
        } else {
            Tab_PlayerList.removeIf(ee -> ee == null || ee.equals(name));
        }
    }

    @SuppressWarnings("unchecked")
    public static void redis_sync_Tab_PlayerList() {
        List<String> rsl = (List<String>) RedisConnection.getdata("Tab_List");
        renew_Tab_PlayerList(rsl);
    }
}
