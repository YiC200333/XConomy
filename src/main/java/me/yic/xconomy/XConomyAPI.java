package me.yic.xconomy;

import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.utils.EconomyCommand;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class XConomyAPI {

	public String getversion() {
		return XConomy.getInstance().getDescription().getVersion();
	}

	public Boolean isbungeecordmode() {
		return XConomy.isBungeecord();
	}

	public UUID translateUUID(String playername) {
		return Cache.translateUUID(playername);
	}

	public BigDecimal formatdouble(String amount) {
		return DataFormat.formatString(amount);
	}

	public String getdisplay(BigDecimal balance) {
		return DataFormat.shown(balance);
	}

	public BigDecimal getbalance(UUID uid) {
		return Cache.getBalanceFromCacheOrDB(uid);
	}

	public Boolean ismaxnumber(BigDecimal amount) {
		return DataFormat.isMAX(amount);
	}

	public Integer changebalance(UUID u, String playername, BigDecimal amount, Boolean isadd) {
		if (XConomy.isBungeecord() & Bukkit.getOnlinePlayers().isEmpty()) {
			return 1;
		}
		Cache.change(u ,amount ,isadd ,"PLUGIN_API" , playername, "N/A");
		return 0;
	}

	public List<String> getbalancetop() {
		return Cache.baltop_papi;
	}

	public BigDecimal getsumbalance() {
		return Cache.sumbalance;
	}
}
