/*
 *  This file (HiddenINFO.java) is a part of project XConomy
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
package me.yic.xconomy.info;

import java.util.ArrayList;
import java.util.List;

public class HiddenINFO {
    private static final List<String> hidstatus = new ArrayList<>();

    public static void addHidden(String name) {
        if (!hidstatus.contains(name)){
            hidstatus.add(name);
        }
    }
    public static boolean getHidden(String name) {
        return hidstatus.contains(name);
    }
}