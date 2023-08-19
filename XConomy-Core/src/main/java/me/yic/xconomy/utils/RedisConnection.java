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
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.redis.RedisSubscriber;
import me.yic.xconomy.info.DataBaseConfig;
import me.yic.xconomy.info.SyncChannalType;
import me.yic.xconomy.lang.MessagesManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;

public class RedisConnection {

    public static final byte[] channelname = "XConomy_Sync".getBytes(StandardCharsets.UTF_8);
    public static final RedisSubscriber subscriber = new RedisSubscriber();

    private static final String url = DataBaseConfig.config.getString("Redis.host");
    private static final int port = DataBaseConfig.config.getInt("Redis.port");
    private static final int dbindex = DataBaseConfig.config.getInt("Redis.db-index");

    private static final String user = DataBaseConfig.config.getString("Redis.auth.user");
    private static final String password = DataBaseConfig.config.getString("Redis.auth.pass");
    private static final int maxtotal = DataBaseConfig.config.getInt("Redis.pool-settings.max-total");
    private static final int maxidle = DataBaseConfig.config.getInt("Redis.pool-settings.max-idle");
    private static final int minidle = DataBaseConfig.config.getInt("Redis.pool-settings.min-idle");

    public static JedisPool jedis;

    public static void close() {
        subscriber.close();
        if (jedis != null) {
            if (!jedis.isClosed()) {
                jedis.close();
            }
        }
    }

    public static boolean connectredis() {

        try {
            Class.forName("org.slf4j.Logger");
        } catch (ClassNotFoundException e) {
            XConomy.getInstance().logger("未找到 'org.slf4j.Logger'", 1, null);
            XConomy.getInstance().logger(null, 0,
                    MessagesManager.systemMessage("连接异常").replace("%type%", "Redis"));
            return false;
        }

        if (XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.REDIS)) {
            JedisPoolConfig jedisconfig = new JedisPoolConfig();
            jedisconfig.setMaxTotal(maxtotal);
            jedisconfig.setMaxIdle(maxidle);
            jedisconfig.setMinIdle(minidle);
            jedisconfig.setTestOnBorrow(true);
            jedisconfig.setTestOnReturn(true);
            if (!user.isEmpty() || !password.isEmpty()) {
                if (!user.isEmpty()) {
                    jedis = new JedisPool(jedisconfig, url, port, 0, user, password);
                }else{
                    jedis = new JedisPool(jedisconfig, url, port, 0, password);
                }
            }
            try {
                Jedis jr = getResource();
                jr.ping();
                returnResource(jr);
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

    public static void returnResource(final Jedis jedisc) {
        if (jedisc != null) {
            jedis.returnResource(jedisc);
        }
    }

    public static Jedis getResource() {
        Jedis jr = jedis.getResource();
        jr.select(dbindex);
        return jr;
    }
}