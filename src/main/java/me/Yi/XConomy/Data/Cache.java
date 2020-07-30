package me.Yi.XConomy.Data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Task.Save;
import me.Yi.XConomy.Task.SendMessTask;

public class Cache {
	public static ConcurrentHashMap<UUID, BigDecimal> bal = new ConcurrentHashMap<UUID, BigDecimal>();
	public static HashMap<String, Double> baltop = new HashMap<String, Double>();
	public static List<String> baltop_papi = new ArrayList<String>();
	public static ConcurrentHashMap<String, UUID> uid = new ConcurrentHashMap<String, UUID>();

	public static void addbal(final UUID u, BigDecimal v) {
		if (v!=null && !v.equals(null)) {
		bal.put(u, v);
		}
	}

	public static void adduid(final String u, UUID v) {
		uid.put(u, v);
	}

	public static BigDecimal getbal(UUID u) {
		if (bal.containsKey(u)) {
			return bal.get(u);
		} else {
			DataCon.getbal(u);
			if (bal.containsKey(u)) {
				return bal.get(u);
			} else {
				BigDecimal ls = new BigDecimal("0.0");
				return ls;
			}
		}

	}

	public static void change(UUID u, BigDecimal amount, Integer type) {
		BigDecimal ls = new BigDecimal("0.0");
		if (type == 1) {
			ls = getbal(u).add(amount);
		} else if (type == 2) {
			ls = getbal(u).subtract(amount);
		} else if (type == 3) {
			ls = amount;
		}
		addbal(u, ls);
		new Save(u, amount, type).runTaskAsynchronously(XConomy.getInstance());
		if (XConomy.isbc()) {
			sendmess(u, ls.doubleValue());
		}
	}

	public static void baltop() {
		baltop.clear();
		baltop_papi.clear();
		if (XConomy.config.getBoolean("Settings.mysql")) {
			XConomy.mysqldb.top();
		} else {
			YML.gettop();
		}
	}

	public static UUID translateuid(String name) {
		if (uid.containsKey(name)) {
			return uid.get(name);
		} else {
			Player pp = Bukkit.getPlayer(name);
			if (pp != null) {
				uid.put(name, pp.getUniqueId());
				return uid.get(name);
			} else {
				DataCon.getuid(name);
				if (uid.containsKey(name)) {
					return uid.get(name);
				}
			}
		}
		return null;
	}

	public static void sendmess(UUID u, Double bb) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try {
			output.writeUTF("balance");
			output.writeUTF(XConomy.getsign());
			output.writeUTF(u.toString());
			output.writeUTF(Double.toString(bb));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new SendMessTask(stream).runTaskAsynchronously(XConomy.getInstance());
		// Bukkit.getServer().sendPluginMessage(XConomy.getInstance(), "xconomy:acb",
		// stream.toByteArray());
	}

}
