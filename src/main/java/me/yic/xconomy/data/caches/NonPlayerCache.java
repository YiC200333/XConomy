package me.yic.xconomy.data.caches;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.utils.RecordData;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NonPlayerCache {
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
		if (isAdd != null) {
			BigDecimal bal = getBalanceFromCacheOrDB(u);
			if (isAdd) {
				newvalue = bal.add(amount);
			} else {
				newvalue = bal.subtract(amount);
			}
		}
        insertIntoCache(u, newvalue);
        RecordData x = null;
        if (XConomy.config.getBoolean("Settings.mysql") && XConomy.config.getBoolean("Settings.transaction-record")) {
            x = new RecordData(type, null, u, newvalue, amount, isAdd, "N/A");
        }
        DataCon.saveNonPlayer(u, amount, isAdd, x);
    }

}
