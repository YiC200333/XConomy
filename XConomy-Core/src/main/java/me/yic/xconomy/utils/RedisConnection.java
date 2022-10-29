/*
 *  This file (SQL.java) is a part of project XConomy
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
package me.yic.xconomy.utils;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.info.DataBaseConfig;
import me.yic.xconomy.lang.MessagesManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

public class RedisConnection {

    private static final String url = DataBaseConfig.config.getString("Redis.host");
    private static final int port = DataBaseConfig.config.getInt("Redis.port");
    private static final int dbindex = DataBaseConfig.config.getInt("Redis.db-index");
    private static final int duration = DataBaseConfig.config.getInt("Redis.duration");

    private static final String user = DataBaseConfig.config.getString("Redis.auth.user");
    private static final String password = DataBaseConfig.config.getString("Redis.auth.pass");
    private static final int maxtotal = DataBaseConfig.config.getInt("Redis.pool-settings.max-total");
    private static final int maxidle = DataBaseConfig.config.getInt("Redis.pool-settings.max-idle");
    private static final int minidle = DataBaseConfig.config.getInt("Redis.pool-settings.min-idle");

    public static JedisPool jedis;

    public static void close() {
        jedis.close();
    }

    public static boolean connectredis() {
        String cachetype = DataBaseConfig.config.getString("Settings.cache-type");
        if (cachetype != null && cachetype.equalsIgnoreCase("Redis")) {
            JedisPoolConfig jedisconfig = new JedisPoolConfig();
            jedisconfig.setMaxTotal(maxtotal);
            jedisconfig.setMaxIdle(maxidle);
            jedisconfig.setMinIdle(minidle);
            jedis = new JedisPool(jedisconfig, url, port);
            try {
                getResource();
                XConomy.getInstance().logger(null, 0,
                        MessagesManager.systemMessage("连接正常").replace("%type%", "Redis"));
            } catch (Exception e) {
                XConomy.getInstance().logger(null, 0,
                        MessagesManager.systemMessage("连接异常").replace("%type%", "Redis"));
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static Jedis getResource() {
        Jedis jr = jedis.getResource();
        if (!user.isEmpty() || !password.isEmpty()){
            if (!user.isEmpty()){
                jr.auth(user, password);
            }else{
                jr.auth(password);
            }
        }
        jr.select(dbindex);
        return jr;
    }

    public static void insertdata(UUID uid, PlayerData pd) {
        try (Jedis jr = getResource()) {
            jr.setex(uid.toString().getBytes(), 10, pd.toByteArray(XConomy.syncversion).toByteArray());
        }
    }


    public static boolean containsKey(UUID uid) {
        try (Jedis jr = getResource()) {
            return jr.exists(uid.toString().getBytes());
        }
    }

    public static PlayerData getPlayerData(UUID uid) {
        try (Jedis jr = getResource()) {
            ByteArrayInputStream input = new ByteArrayInputStream(jr.get(uid.toString().getBytes()));
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
    }
}