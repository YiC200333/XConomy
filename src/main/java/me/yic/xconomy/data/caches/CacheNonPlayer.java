package me.yic.xconomy.data.caches;

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.utils.PlayerData;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheNonPlayer {
    public static Map<String, BigDecimal> bal = new ConcurrentHashMap<>();

    public static void insertIntoCache(final String playerName, BigDecimal value) {
        if (value != null) {
            bal.put(playerName, value);
        }
    }

    public static BigDecimal getBalanceFromCacheOrDB(String u) {
        if (!bal.containsKey(u)) {
            DataCon.getBalNonPlayer(u);
        }
        return bal.get(u);

    }

    public static void change(String u, BigDecimal amount, Boolean isAdd, String type) {
        BigDecimal newvalue = amount;
        BigDecimal balance = getBalanceFromCacheOrDB(u);
        if (isAdd != null) {
            if (isAdd) {
                newvalue = balance.add(amount);
            } else {
                newvalue = balance.subtract(amount);
            }
        }
        insertIntoCache(u, newvalue);
        PlayerData pd = new PlayerData(type, null, u, balance, amount, newvalue, isAdd, "N/A");
        DataCon.saveNonPlayer(u, newvalue, pd);
    }

}
