package me.Yi.XConomy.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache_NonPlayer {
    public static Map<String, BigDecimal> bal = new ConcurrentHashMap<String, BigDecimal>();
    public static Map<String, BigDecimal> bal_change = new HashMap<String, BigDecimal>();

    public static void insertIntoCache(final String playerName, BigDecimal value) {
        if (value != null) {
            bal.put(playerName, value);
        }
    }
    
    public static void insertIntoCacheChange(final String playerName, BigDecimal value) {
        if (value != null) {
        	bal_change.put(playerName, value);
        }
    }
    
    public static BigDecimal getBalanceFromCacheOrDB(String u) {
        if (bal.containsKey(u)) {
            return bal.get(u);
        } else {
            DataCon.getbalnon(u);
            return bal.get(u);
        }

    }

    public static void change(String u, BigDecimal amount, Boolean isAdd) {
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
        insertIntoCacheChange(u, newvalue);        
    }

}
