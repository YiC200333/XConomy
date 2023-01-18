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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.info.SyncInfo;
import me.yic.xconomy.info.SyncType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.UUID;

public abstract class SyncData implements Serializable {

    final String server_key;
    final String sign;
    final SyncType st;
    final UUID uuid;

    protected SyncData(SyncType st, UUID uuid) {
        this.sign = XConomyLoad.Config.SYNCDATA_SIGN;
        this.server_key = SyncInfo.server_key;
        this.st = st;
        this.uuid = uuid;
    }

    public boolean isSammServer(String key) {
        return this.server_key.equals(key);
    }

    public String getSign() {
        return sign;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public SyncType getSyncType() {
        return st;
    }

    @SuppressWarnings("unused")
    public ByteArrayOutputStream toByteArray(String syncversion) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            oos.writeObject(this);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public abstract void SyncStart();
}
