package me.Yi.XConomy.Data;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import me.Yi.XConomy.Task.Save_Non;
import me.Yi.XConomy.XConomy;

public class Cache_NonPlayer {
    public static ConcurrentHashMap<String, BigDecimal> bal = new ConcurrentHashMap<String, BigDecimal>();

    public static void addbal(final String playerName, BigDecimal v) {
        if (v != null && !v.equals(null)) {
            bal.put(playerName, v);
        }
    }

    /**
     * If balance in cache, get from cache. Else fetch from db
     *
     * @param u
     * @return
     */
    public static BigDecimal getBalanceFromCacheOrDatabase(String u) {
        if (bal.containsKey(u)) {
            return bal.get(u);
        } else {
            DataCon.getbalnon(u);
            return bal.get(u);
        }

    }

    public static void change(String u, BigDecimal amount, Integer type) {
        BigDecimal newValue = BigDecimal.ZERO;
        if (type == 1) {
            newValue = getBalanceFromCacheOrDatabase(u).add(amount);
        } else if (type == 2) {
            newValue = getBalanceFromCacheOrDatabase(u).subtract(amount);
        } else if (type == 3) {
            newValue = amount;
        }
        addbal(u, newValue);
        new Save_Non(u, amount, type).runTaskAsynchronously(XConomy.getInstance());
    }

}
