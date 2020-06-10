package me.Yi.XConomy.Data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class YML {
	public static FileConfiguration pd;
	public static FileConfiguration pdnon;
	public static FileConfiguration pdu;
	public static FileConfiguration top;

	public static void create(Player a, Double ii) {
		if (pd.contains(a.getUniqueId().toString())) {
			pd.set(a.getUniqueId().toString() + ".username", a.getName());
			save(pd, DataCon.userdata);
		} else {
			pd.createSection(a.getUniqueId().toString() + ".username");
			pd.set(a.getUniqueId().toString() + ".username", a.getName());
			pd.createSection(a.getUniqueId().toString() + ".balance");
			pd.set(a.getUniqueId().toString() + ".balance", ii);
			save(pd, DataCon.userdata);
		}
	}

	public static void createu(Player a) {
		pdu.createSection(a.getName() + ".UUID");
		pdu.set(a.getName() + ".UUID", a.getUniqueId().toString());
		save(pdu, DataCon.uiddata);
	}

	public static void getbal(UUID u) {
		Double am = pd.getDouble(u.toString() + ".balance");
		Cache.addbal(u, DataFormat.formatd(am));
	}

	public static void getbal_nonp(String u) {
		if (pdnon.contains(u)) {
			Double am = pdnon.getDouble(u + ".balance");
			Cache_NonPlayer.addbal(u, DataFormat.formatd(am));
		} else {
			Cache_NonPlayer.addbal(u, DataFormat.formatd(0.0));
			pdnon.createSection(u + ".balance");
			pdnon.set(u + ".balance", 0.0);
			save(pdnon, DataCon.nonpdata);
		}
	}

	public static void check(Player a) {
		if (!pd.getString(a.getUniqueId().toString() + ".username").equals(a.getName())) {

		}

	}

	public static void getuid(String name) {
		if (pdu.contains(name)) {
			Cache.adduid(name, UUID.fromString(pdu.getString(name + ".UUID")));
		}

	}

	public static void gettop() {
		if (top.contains("topname") & top.contains("topbal")) {
			List<String> name = Arrays.asList(top.getString("topname").split("###"));
			List<String> baltop = Arrays.asList(top.getString("topbal").split("###"));
			Integer i = 0;
			for (String xxx : name) {
				Cache.baltop_papi.add(xxx);
				Cache.baltop.put(xxx, Double.valueOf(baltop.get(i)));
				i = i + 1;
			}
		} else {
			top.createSection("topname");
			top.createSection("topbal");
		}
	}

	public static void savetop(Map<String, Double> bal) {
		Map<String, Double> ls = Cache.baltop;
		ls.putAll(bal);
		List<Map.Entry<String, Double>> list = new ArrayList<>(ls.entrySet());
		Collections.sort(list,
				(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) -> o2.getValue().compareTo(o1.getValue()));
		List<String> name = new ArrayList<String>();
		List<String> baltop = new ArrayList<String>();
		int x = 1;
		for (Map.Entry<String, Double> e : list) {
			if (x > 10) {
				break;
			}
			if (e.getKey().length() < 20) {
				name.add(e.getKey());
				baltop.add(Double.toString(e.getValue()));
				x = x + 1;
			}
		}
		top.set("topname", String.join("###", name));
		top.set("topbal", String.join("###", baltop));
		save(top, DataCon.topdata);
	}

	public static void save(FileConfiguration fc, File x) {
		try {
			fc.save(x);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
