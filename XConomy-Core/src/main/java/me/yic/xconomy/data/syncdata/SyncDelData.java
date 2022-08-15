/*
 *  This file (SyncDelData.java) is a part of project XConomy
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

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.info.SyncType;

public class SyncDelData extends PlayerData {

    public SyncDelData(String sign, PlayerData pd){
        super(sign, SyncType.DELETEDATA, pd.getUniqueId(), pd.getName(), pd.getBalance());
    }

    @Override
    public void SyncStart() {
        DataCon.deletePlayerData(this);
    }
}
