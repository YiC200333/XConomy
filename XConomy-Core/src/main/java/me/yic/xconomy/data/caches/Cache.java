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

import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.GetUUID;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    public static final ConcurrentHashMap<UUID, PlayerData> pds = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, Integer> phids = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, UUID> uuids = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<UUID, UUID> m_uuids = new ConcurrentHashMap<>();

    public static LinkedHashMap<String, BigDecimal> baltop = new LinkedHashMap<>();
    public static List<String> baltop_papi = new ArrayList<>();
    public static BigDecimal sumbalance = BigDecimal.ZERO;

    public static void insertIntoCache(final UUID uuid, final PlayerData pd) {
        if (pd != null) {
            if (pd.getName() != null && pd.getBalance() != null) {
                pds.put(uuid, pd);
                if (XConomyLoad.Config.USERNAME_IGNORE_CASE) {
                    uuids.put(pd.getName().toLowerCase(), uuid);
                } else {
                    uuids.put(pd.getName(), uuid);
                }
            }
        }
    }

    public static void insertIntoMultiUUIDCache(final UUID uuid, final UUID luuid) {
        if (uuid != null && luuid != null && !uuid.toString().equals(luuid.toString())) {
            m_uuids.put(luuid, uuid);
        }
    }

    public static void insertIntoHiddenMap(final UUID uuid, final int hidden) {
        if (uuid != null) {
            phids.put(uuid, hidden);
        }
    }

    public static <T> boolean CacheContainsKey(final T key) {
        if (key instanceof UUID) {
            if (pds.containsKey((UUID) key)) {
                return true;
            }
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                UUID luuid = getMultiUUIDCache((UUID) key);
                if (luuid == null) {
                    return false;
                } else {
                    return pds.containsKey(luuid);
                }
            }
            return false;
        }
        if (XConomyLoad.Config.USERNAME_IGNORE_CASE) {
            return uuids.containsKey(((String) key).toLowerCase());
        }
        return uuids.containsKey((String) key);
    }


    public static <T> PlayerData getDataFromCache(final T key) {
        UUID u;
        if (key instanceof UUID) {
            u = (UUID) key;
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                if (!pds.containsKey((UUID) key) && getMultiUUIDCache((UUID) key) != null) {
                    u = getMultiUUIDCache((UUID) key);
                }
            }
        } else {
            if (XConomyLoad.Config.USERNAME_IGNORE_CASE) {
                u = uuids.get(((String) key).toLowerCase());
            } else {
                u = uuids.get(((String) key));
            }
        }
        return pds.get(u);
    }

    public static void deleteDataFromCache(final UUID key) {
        if (CacheContainsKey(key)) {
            pds.remove(key);
        }
    }

    public static UUID getMultiUUIDCache(final UUID luuid) {
        if (m_uuids.containsKey(luuid)) {
            return m_uuids.get(luuid);
        }
        return null;
    }

    public static void updateIntoCache(final UUID uuid, final PlayerData pd, final BigDecimal newbalance, final BigDecimal vbalance) {
        pd.setBalance(newbalance);
        pd.setVerifyBalance(vbalance);
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
        m_uuids.clear();
    }


}
