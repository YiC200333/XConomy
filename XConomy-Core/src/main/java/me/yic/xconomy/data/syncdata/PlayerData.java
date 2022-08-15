/*
 *  This file (PlayerData.java) is a part of project XConomy
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
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.SyncType;
import me.yic.xconomy.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.UUID;

public class PlayerData extends SyncData {
    private final String name;
    private BigDecimal balance;

    public PlayerData(String sign, UUID uuid, String name, BigDecimal balance) {
        super(sign, SyncType.UPDATEPLAYER, uuid);
        this.name = name;
        this.balance = balance;
    }

    protected PlayerData(String sign, SyncType sycn, UUID uuid, String name, BigDecimal balance) {
        super(sign, sycn, uuid);
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void SyncStart() {
        Cache.insertIntoCache(getUniqueId(), this);
        if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
            if (Cache.getMultiUUIDCache(getUniqueId()) != null) {
                for (UUID u : Cache.getMultiUUIDCache(getUniqueId())) {
                    Cache.insertIntoCache(u, this);
                }
            }
        }
    }
}