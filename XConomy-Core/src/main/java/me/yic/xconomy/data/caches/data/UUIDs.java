/*
 *  This file (UUIDs.java) is a part of project XConomy
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
package me.yic.xconomy.data.caches.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.RedisConnection;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UUIDs {
    private static final ConcurrentHashMap<String, UUID> uuids = new ConcurrentHashMap<>();

    public static final String prefix = "XConomy-UUIDs-";

    public static void put(String name, UUID uuid) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.setex((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + name), RedisConnection.duration, uuid.toString());
            }
        } else {
            uuids.put(name, uuid);
        }
    }

    public static boolean containsKey(String name) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                return jr.exists(prefix + XConomy.Config.BUNGEECORD_SIGN + " " + name);
            }
        } else {
            return uuids.containsKey(name);
        }
    }

    public static void remove(String name) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.del(prefix + XConomy.Config.BUNGEECORD_SIGN + " " + name);
            }
        } else {
            uuids.remove(name);
        }
    }

    public static UUID get(String name) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                String ba = jr.get(prefix + XConomy.Config.BUNGEECORD_SIGN + " " + name);
                return UUID.fromString(ba);
            }
        } else {
            return uuids.get(name);
        }
    }

    public static void clear(boolean redis) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis") && redis) {
            try (Jedis jr = RedisConnection.getResource()) {
                for (String key: jr.keys(prefix + XConomy.Config.BUNGEECORD_SIGN + "*")){
                    jr.del(key);
                }
            }
        }else {
            uuids.clear();
        }
    }
}
