package me.Yi.XConomy.Data;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Task.Save_Non;

public class Cache_NonPlayer {
	public static ConcurrentHashMap<String, BigDecimal> bal = new ConcurrentHashMap<String, BigDecimal>();

	public static void addbal(final String u, BigDecimal v) {
		if (v!=null && !v.equals(null)) {
		bal.put(u, v);
		}
	}

	public static BigDecimal getbal(String u) {
		if (bal.containsKey(u)) {
			return bal.get(u);
		} else {
			DataCon.getbalnon(u);
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
		new Save_Non(u, amount, type).runTaskAsynchronously(XConomy.getInstance());
	}

}
