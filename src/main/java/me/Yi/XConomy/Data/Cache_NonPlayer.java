package me.Yi.XConomy.Data;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

public class Cache_NonPlayer {
	public static ConcurrentHashMap<String, BigDecimal> bal = new ConcurrentHashMap<String, BigDecimal>();

	public static void addbal(final String u, BigDecimal v) {
		bal.put(u, v);
	}

	public static BigDecimal getbal(String u) {
		if (bal.containsKey(u)) {
			return bal.get(u);
		} else {
			DataCon.getbalnon(u);
			if (Bukkit.getOnlinePlayers().isEmpty()) {
				BigDecimal am = bal.get(u);
				bal.remove(u);
				return am;
			}
			return bal.get(u);
		}

	}

	public static void change(String u, BigDecimal amount, Integer type) {
		BigDecimal ls = new BigDecimal("0.0");
		if (type == 1) {
			ls = getbal(u).add(amount);
		} else if (type == 2) {
			ls = getbal(u).subtract(amount);
		} else if (type == 3) {
			ls = amount;
		}
		addbal(u, ls);
	}

}
