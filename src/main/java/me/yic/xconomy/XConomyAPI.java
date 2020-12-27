package me.yic.xconomy;

import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import org.bukkit.Bukkit;

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
		BigDecimal bal = getbalance(Cache.translateUUID(playername));
		if (isadd){
			if (ismaxnumber(bal.add(amount))){
				return 3;
			}
		}else {
			if (bal.compareTo(amount) < 0) {
				return 2;
			}
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
