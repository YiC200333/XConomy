package me.yic.xconomy.depend;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.DataFormat;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.math.BigDecimal;

public class Placeholder extends PlaceholderExpansion {

	private final XConomy plugin;

	public Placeholder(XConomy plugin) {
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
			String index = identifier.substring(identifier.indexOf("top_player_") + 11);
			if (isNumber(index)) {
				if (outindex(index)) {
					return "NO DATA";
				}
				int ii = Integer.parseInt(index) - 1;
				return Cache.baltop_papi.get(ii);
			} else {
				return "[XConomy]Invalid index";
			}
		} else if (identifier.contains("top_balance_")) {
			String index = identifier.substring(identifier.indexOf("top_balance_") + 12);
			if (identifier.contains("top_balance_value")) {
				index = identifier.substring(identifier.indexOf("top_balance_value_") + 18);
			}
			if (isNumber(index)) {
				if (outindex(index)) {
					return "NO DATA";
				}
				int indexInt = Integer.parseInt(index) - 1;
				String name = Cache.baltop_papi.get(indexInt);
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

	private boolean isNumber(String str) {
		if (str.equals("10")) {
			return true;
		}
		if (str.equals("0")) {
			return false;
		}
		return str.matches("[0-9]");
	}

	private boolean outindex(String str) {
		return Integer.parseInt(str) > Cache.baltop_papi.size();
	}
}
