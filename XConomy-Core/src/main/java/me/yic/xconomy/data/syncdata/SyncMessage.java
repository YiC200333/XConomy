/*
 *  This file (SyncMessage.java) is a part of project XConomy
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

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.info.SyncType;

import java.util.UUID;

public class SyncMessage extends SyncData {

    private final String name;
    private UUID ruuid;
    private final String message;

    public SyncMessage(SyncType st, UUID uuid, String message) {
        super(st, uuid);
        this.message = message;
        this.name = null;
        this.ruuid = null;
    }

    public SyncMessage(SyncType st, String name, String message) {
        super(st, null);
        this.message = message;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getRUniqueId() {
        return ruuid;
    }

    public void setRUniqueId(UUID ruuid) {
        this.ruuid = ruuid;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void SyncStart() {
        AdapterManager.PLUGIN.broadcastMessage(getMessage());
    }
}
