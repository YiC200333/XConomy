package me.Yi.XConomy;

import java.math.BigDecimal;
import org.bukkit.OfflinePlayer;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PE extends PlaceholderExpansion {

	private XConomy plugin;

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
			if (identifier.contains("balance_value")) {
			return a.toString();
			}
			return DataFormat.shown(a);
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
			if (identifier.contains("top_balance_value")) {
				nm = identifier.substring(identifier.indexOf("top_balance_value_") + 18);
			}
			if (isnum(nm)) {
				Integer ii = Integer.valueOf(nm) - 1;
				String name = Cache.baltop_papi.get(ii);
				BigDecimal a = Cache.baltop.get(name);
				if (identifier.contains("top_balance_value")) {
					return a.toString();
				}
					return DataFormat.shown(a);
			} else {
				return "[XConomy]Invalid index";
			}
		} else if (identifier.contains("sum_balance")) {
			if (Cache.sumbalance == null) {
				return "0.0";
			}
			BigDecimal s = Cache.sumbalance;
			if (identifier.contains("sum_balance_value")) {
				return s.toString();
			}
				return DataFormat.shown(s);
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
}
