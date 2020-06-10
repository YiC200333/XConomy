package me.Yi.XConomy;

import org.bukkit.OfflinePlayer;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataFormat;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PE extends PlaceholderExpansion {

	@Override
	public String getAuthor() {
		return "YiC";
	}

	@Override
	public String getIdentifier() {
		return "xconomy";
	}

	@Override
	public String onRequest(OfflinePlayer player, String identifier) {
		if (player == null) {
			return "0.0";
		}
		if (identifier.equals("balance")) {
			Double a = XConomy.getInstance().getEconomy().getBalance(player);
			String str = DataFormat.shown((a));
			return str;
		} else if (identifier.contains("top_player_")) {
			String nm = identifier.substring(identifier.indexOf("top_player_") + 11);
			if (isnum(nm)) {
				Integer ii = Integer.valueOf(nm) - 1;
				String str = Cache.baltop_papi.get(ii);
				return str;
			} else {
				return "[XConomy]Invalid index";
			}
		} else if (identifier.contains("top_balance_")) {
			String nm = identifier.substring(identifier.indexOf("top_balance_") + 12);
			if (isnum(nm)) {
				Integer ii = Integer.valueOf(nm) - 1;
				String name = Cache.baltop_papi.get(ii);
				Double a = Cache.baltop.get(name);
				String str = DataFormat.shown((a));
				return str;
			} else {
				return "[XConomy]Invalid index";
			}
		}
		return null;
	}

	@Override
	public String getVersion() {
		return XConomy.getInstance().getDescription().getVersion();
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	public boolean isnum(String str) {
		if (str.equals("10")) {
			return true;
		}
		if (str.matches("[0-9]")) {
			if (Integer.valueOf(str) <= Cache.baltop_papi.size()) {
				return true;
			}
		}
		return false;
	}

	public static void registerExpansion() {
		PlaceholderAPI.registerExpansion(new PE());
	}
}
