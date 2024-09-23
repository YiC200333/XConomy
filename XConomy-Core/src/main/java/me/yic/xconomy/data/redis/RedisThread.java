/*
 *  This file (RedisThread.java) is a part of project XConomy
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
package me.yic.xconomy.data.redis;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.RedisConnection;
import redis.clients.jedis.Jedis;

public class RedisThread extends Thread {

    public RedisThread() {
        super("XConomyRedisSub");
    }

    @Override
    public void run() {
        XConomy.getInstance().logger("Redis监听线程创建中", 0, null);
        Jedis jedis = null;
        try {
            jedis = RedisConnection.getResource();
            jedis.subscribe(RedisConnection.subscriber, RedisConnection.channelname);
        } catch (Exception e) {
            XConomy.getInstance().logger(null, 1, "Error during creation");
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                RedisConnection.returnResource(jedis);
            }
        }
    }
}
