package me.yi.xconomy;

import me.yi.xconomy.data.caches.Cache;
import me.yi.xconomy.data.DataFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

public class PE extends PlaceholderExpansion {

	private final XConomy plugin;

	public PE(XConomy plugin) {
		this.plugin = plugin;
	}

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
		if (identifier.equals("balance")) {
			if (player == null) {
				return "0.0";
			}
			BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId());
			return DataFormat.shown(a);
		} else if (identifier.equals("balance_value")) {
			if (player == null) {
				return "0.0";
			}
			BigDecimal bal = Cache.getBalanceFromCacheOrDB(player.getUniqueId());
			return bal.toString();
		} else if (identifier.contains("top_player_")) {
			String nm = identifier.substring(identifier.indexOf("top_player_") + 11);
			if (isNumber(nm)) {
				int ii = Integer.parseInt(nm) - 1;
				return Cache.baltop_papi.get(ii);
			} else {
				return "[XConomy]Invalid index";
			}
		} else if (identifier.contains("top_balance_")) {
			String placement = identifier.substring(identifier.indexOf("top_balance_") + 12);
			if (identifier.contains("top_balance_value")) {
				placement = identifier.substring(identifier.indexOf("top_balance_value_") + 18);
			}
			if (isNumber(placement)) {
				int placementInt = Integer.parseInt(placement) - 1;
				String name = Cache.baltop_papi.get(placementInt);
				BigDecimal bal = Cache.baltop.get(name);
				if (identifier.contains("top_balance_value")) {
					return bal.toString();
				}
				return DataFormat.shown(bal);
			} else {
				return "[XConomy]Invalid index";
			}
		} else if (identifier.contains("sum_balance")) {
			if (Cache.sumbalance == null) {
				return "0.0";
			}
			BigDecimal bal = Cache.sumbalance;
			if (identifier.contains("sum_balance_value")) {
				return bal.toString();
			}
			return DataFormat.shown(bal);
		}
		return null;
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	public boolean isNumber(String str) {
		if (str.equals("10")) {
			return true;
		}
		if (str.matches("[0-9]")) {
			return Integer.parseInt(str) <= Cache.baltop_papi.size();
		}
		return false;
	}
}
