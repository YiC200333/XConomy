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

import me.yic.xconomy.info.ServerINFO;
import me.yic.xconomy.utils.PlayerData;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private static final Map<UUID, PlayerData> pds = new ConcurrentHashMap<>();
    private static final Map<String, UUID> uuids = new ConcurrentHashMap<>();
    public static Map<String, BigDecimal> baltop = new HashMap<>();
    public static List<String> baltop_papi = new ArrayList<>();
    public static BigDecimal sumbalance = BigDecimal.ZERO;

    public static void insertIntoCache(final UUID uuid, PlayerData pd) {
        if (pd != null) {
            if (pd.getName() != null && pd.getbalance() != null) {
                pds.put(uuid, pd);
                if (ServerINFO.IgnoreCase) {
                    uuids.put(pd.getName().toLowerCase(), uuid);
                } else {
                    uuids.put(pd.getName(), uuid);
                }
            }
        }
    }


    public static <T> boolean CacheContainsKey(final T key) {
        if (key instanceof UUID) {
            return pds.containsKey((UUID) key);
        }
        if (ServerINFO.IgnoreCase) {
            return uuids.containsKey(((String) key).toLowerCase());
        }
        return uuids.containsKey((String) key);
    }


    public static <T> PlayerData getDataFromCache(final T key) {
        UUID u;
        if (key instanceof UUID) {
            u = (UUID) key;
        } else {
            if (ServerINFO.IgnoreCase) {
                u = uuids.get(((String) key).toLowerCase());
            } else {
                u = uuids.get(((String) key));
            }
        }
        return pds.get(u);
    }

    public static void updateIntoCache(final UUID uuid, PlayerData pd, BigDecimal newbalance) {
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


    public static void clearCache() {
        pds.clear();
        uuids.clear();
    }


}
