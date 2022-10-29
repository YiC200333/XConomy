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

public class UUIDs {
    private static final ConcurrentHashMap<String, UUID> uuids = new ConcurrentHashMap<>();

    public static final String prefix = "XConomy-UUIDs-";

    public static void put(String name, UUID uuid) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.setex((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + name), RedisConnection.duration, uuids.toString());
            }
        } else {
            uuids.put(name, uuid);
        }
    }

    public static boolean containsKey(String name) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.exists(prefix + XConomy.Config.BUNGEECORD_SIGN + " " + name);
            }
        } else {
            return uuids.containsKey(name);
        }
        return false;
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

    public static void clear() {
        uuids.clear();
    }
}
