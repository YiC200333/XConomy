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
                jr.setex((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes() , RedisConnection.duration, pd.toByteArray(XConomy.syncversion).toByteArray());
            }
        } else {
            pds.put(uuid, pd);
        }
    }

    public static boolean containsKey(UUID uuid) {
        if (XConomy.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            try (Jedis jr = RedisConnection.getResource()) {
                jr.exists((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes());
            }
        } else {
            return pds.containsKey(uuid);
        }
        return false;
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
                byte[] ba =  jr.get((prefix + XConomy.Config.BUNGEECORD_SIGN + " " + uuid.toString()).getBytes());
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

    public static void clear() {
        pds.clear();
    }
}
