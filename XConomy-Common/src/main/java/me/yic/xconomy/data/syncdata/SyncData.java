/*
 *  This file (SyncData.java) is a part of project XConomy
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
package me.yic.xconomy.data.syncdata;

import me.yic.xconomy.info.SyncType;

import java.io.Serializable;
import java.util.UUID;

public class SyncData implements Serializable {

    final String sign;
    final SyncType st;
    final UUID uuid;

    protected SyncData(String sign, SyncType st, UUID uuid){
        this.sign = sign;
        this.st = st;
        this.uuid = uuid;
    }


    public String getSign(){
        return sign;
    }

    public UUID getUniqueId(){
        return uuid;
    }

    public SyncType getSyncType(){
        return st;
    }
}
