/*
 *  This file (Cache.java) is a part of project XConomy
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
package me.yic.xconomy.data.caches;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.GetUUID;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private static final Map<UUID, PlayerData> pds = new ConcurrentHashMap<>();
    private static final Map<String, UUID> uuids = new ConcurrentHashMap<>();
    private static final Map<UUID, UUID> sub_uuids = new ConcurrentHashMap<>();
    public static Map<String, BigDecimal> baltop = new HashMap<>();
    public static List<String> baltop_papi = new ArrayList<>();
    public static BigDecimal sumbalance = BigDecimal.ZERO;

    public static void insertIntoCache(final UUID uuid, final PlayerData pd) {
        if (pd != null) {
            if (pd.getName() != null && pd.getbalance() != null) {
                pds.put(uuid, pd);
                if (XConomy.Config.USERNAME_IGNORE_CASE) {
                    uuids.put(pd.getName().toLowerCase(), uuid);
                } else {
                    uuids.put(pd.getName(), uuid);
                }
            }
        }
    }

    public static void insertIntoSUUIDCache(final UUID uuid, final UUID suuid) {
        sub_uuids.put(suuid, uuid);
    }

    public static UUID getSubUUID(final UUID uuid) {
        if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
            if (sub_uuids.containsKey(uuid)) {
                return sub_uuids.get(uuid);
            }
        }
        return uuid;
    }

    public static <T> boolean CacheContainsKey(final T key) {
        if (key instanceof UUID) {
            return pds.containsKey((UUID) key);
        }
        if (XConomy.Config.USERNAME_IGNORE_CASE) {
            return uuids.containsKey(((String) key).toLowerCase());
        }
        return uuids.containsKey((String) key);
    }


    public static <T> PlayerData getDataFromCache(final T key) {
        UUID u;
        if (key instanceof UUID) {
            u = (UUID) key;
        } else {
            if (XConomy.Config.USERNAME_IGNORE_CASE) {
                u = uuids.get(((String) key).toLowerCase());
            } else {
                u = uuids.get(((String) key));
            }
        }
        return pds.get(u);
    }

    public static void updateIntoCache(final UUID uuid, final PlayerData pd, final BigDecimal newbalance) {
        pd.setbalance(newbalance);
        pds.put(uuid, pd);
    }

    @SuppressWarnings("all")
    public static void removefromCache(final UUID uuid) {
        if (pds.containsKey(uuid)) {
            String name = pds.get(uuid).getName();
            pds.remove(uuid);
            uuids.remove(name);
        }
    }


    @SuppressWarnings("all")
    public static void syncOnlineUUIDCache(final String oldname, final String newname, final UUID uuid) {
        if (uuids.containsKey(newname)) {
            UUID u = uuids.get(newname);
            pds.remove(u);
            uuids.remove(newname);
        }
        GetUUID.removeUUIDFromCache(oldname);
        GetUUID.removeUUIDFromCache(newname);
        removefromCache(uuid);
    }


    public static void clearCache() {
        pds.clear();
        uuids.clear();
    }


}
