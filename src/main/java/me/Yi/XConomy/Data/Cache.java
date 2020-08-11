package me.Yi.XConomy.Data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Task.Save;
import me.Yi.XConomy.Task.SendMessTaskS;

public class Cache {
	public static Map<UUID, BigDecimal> bal = new ConcurrentHashMap<UUID, BigDecimal>();
	public static Map<String, Double> baltop = new HashMap<String, Double>();
	public static List<String> baltop_papi = new ArrayList<String>();
	public static Map<String, UUID> uid = new ConcurrentHashMap<String, UUID>();
	public static BigDecimal sumbalance = BigDecimal.ZERO;

	public static void addbal(final UUID u, BigDecimal v) {
		if (v != null && !v.equals(null)) {
			if (Bukkit.getOnlinePlayers().size() > 0) {
				bal.put(u, v);
			}
		}
	}

	public static void adduid(final String u, UUID v) {
		uid.put(u, v);
	}

	public static void cclean() {
		bal.clear();
		uid.clear();
	}

	public static BigDecimal getbal(UUID u) {
		if (bal.containsKey(u)) {
			return bal.get(u);
		} else {
			DataCon.getbal(u);
			if (bal.containsKey(u)) {
				return bal.get(u);
			} else {
				return BigDecimal.ZERO;
			}
		}

	}

	public static void change(UUID u, BigDecimal amount, Integer type) {
		BigDecimal ls = BigDecimal.ZERO;
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
			sendmess(u, ls);
		}
	}

	public static void baltop() {
		baltop.clear();
		baltop_papi.clear();
		sumbal();
		DataCon.gettopbal();
	}

	public static void sumbal() {
		sumbalance = DataFormat.formatd(DataCon.getsumbal());
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

	public static void sendmess(UUID u, BigDecimal bb) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try {
			output.writeUTF("balance");
			output.writeUTF(XConomy.getsign());
			output.writeUTF(u.toString());
			output.writeUTF(bb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new SendMessTaskS(stream).runTaskAsynchronously(XConomy.getInstance());
	}

}
