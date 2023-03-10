/*
 *  This file (RedisSubscriber.java) is a part of project XConomy
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
import me.yic.xconomy.data.ProcessSyncData;
import redis.clients.jedis.BinaryJedisPubSub;

public class RedisSubscriber extends BinaryJedisPubSub {

    @Override
    public void onMessage(byte[] channel, byte[] message) {
        ProcessSyncData.process(message);
    }

    @Override
    public void onSubscribe(byte[] channel, int subscribedChannels) {
        XConomy.getInstance().logger("订阅Redis频道成功, channel ", 0,  new String(channel));
    }

    @Override
    public void onUnsubscribe(byte[] channel, int subscribedChannels) {
        XConomy.getInstance().logger("取消订阅Redis频道", 0, null);
    }

    public void close() {
        this.unsubscribe();
    }
}
