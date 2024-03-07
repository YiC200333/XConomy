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

import me.yic.xconomy.adapter.comp.CSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HiddenINFO {
    private static final List<UUID> hidstatus = new ArrayList<>();

    public static boolean switichHidden(CSender cp) {
        UUID targetUUID = cp.toPlayer().getUniqueId();
        if (targetUUID != null){
            if (hidstatus.contains(targetUUID)){
                hidstatus.remove(targetUUID);
            }else{
                hidstatus.add(targetUUID);
                return true;
            }
        }
        return false;
    }
    public static boolean getHidden(UUID targetUUID) {
        if (targetUUID != null){
            return hidstatus.contains(targetUUID);
        }
        return false;
    }
}