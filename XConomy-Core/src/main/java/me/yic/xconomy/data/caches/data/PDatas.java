/*
 *  This file (PDatas.java) is a part of project XConomy
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
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.utils.RedisConnection;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PDatas {

    public static final ConcurrentHashMap<UUID, PlayerData> pds = new ConcurrentHashMap<>();
    public static final String prefix = "XConomy-PlayerDatas-";

    public static void put(UUID uuid, PlayerData pd) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.setex((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes(), RedisConnection.duration, pd.toByteArray(XConomy.syncversion).toByteArray());
            }
        } else {
            pds.put(uuid, pd);
        }
    }

    public static boolean containsKey(UUID uuid) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                return jr.exists((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes());
            }
        } else {
            return pds.containsKey(uuid);
        }
    }

    public static void remove(UUID uuid) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.del((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes());
            }
        } else {
            pds.remove(uuid);
        }
    }

    public static PlayerData get(UUID uuid) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                byte[] ba = jr.get((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes());
                ByteArrayInputStream input = new ByteArrayInputStream(ba);
                ObjectInputStream ios = new ObjectInputStream(input);

                String sv = ios.readUTF();
                if (!sv.equals(XConomy.syncversion)) {
                    XConomy.getInstance().logger("收到不同版本插件的数据，无法同步，当前插件版本 ", 1, XConomy.syncversion);
                    return null;
                }

                return (PlayerData) ios.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return pds.get(uuid);
        }
    }

    public static void clear(boolean redis) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis") && redis) {
            try (Jedis jr = RedisConnection.getResource()) {
                for (String key : jr.keys(prefix + XConomy.Config.BUNGEECORD_SIGN + "*")) {
                    jr.del(key);
                }
            }
        } else {
            pds.clear();
        }
    }
}
