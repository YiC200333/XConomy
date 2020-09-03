package me.YiC.XConomy.task;

import me.YiC.XConomy.data.caches.Cache;
import me.YiC.XConomy.data.SQL;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Baltop extends BukkitRunnable {

	@Override
	public void run() {
		Cache.baltop_papi.clear();
		Cache.baltop.clear();
		SQL.getBaltop();
		Cache.sumbal();
		if (Bukkit.getOnlinePlayers().size() == 0) {
			Cache.clearCache();
		}
	}
}
