/*
 *  This file (CacheNonPlayer.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataLink;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheNonPlayer {
    public static Map<String, BigDecimal> bal = new ConcurrentHashMap<>();

    public static void insertIntoCache(final String playerName, final BigDecimal value) {
        if (value != null) {
            bal.put(playerName, value);
        }
    }

    public static boolean CacheContainsKey(final String key) {
        return bal.containsKey(key);
    }

    public static BigDecimal getBalanceFromCacheOrDB(final String u) {
        if (!bal.containsKey(u)) {
            DataLink.getBalNonPlayer(u);
        }
        return bal.get(u);

    }

}
