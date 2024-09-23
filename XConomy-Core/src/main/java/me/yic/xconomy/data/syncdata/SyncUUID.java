/*
 *  This file (SyncUUID.java) is a part of project XConomy
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

import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.SyncType;

import java.util.UUID;

public class SyncUUID extends SyncData {

    private final String newname;
    private final String oldname;

    public SyncUUID(UUID newUUID, String newname, String oldname) {
        super(SyncType.SYNCONLINEUUID, newUUID);
        this.newname = newname;
        this.oldname = oldname;
    }

    public String getNewname() {
        return newname;
    }

    public String getOldname() {
        return oldname;
    }

    @Override
    public void SyncStart() {
        Cache.syncOnlineUUIDCache(getOldname(), getNewname(), getUniqueId());
    }
}
