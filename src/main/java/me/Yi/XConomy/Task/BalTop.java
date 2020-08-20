package me.Yi.XConomy.Task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.SQL;

public class BalTop extends BukkitRunnable {
	@Override
	public void run() {
		Cache.baltop_papi.clear();
		Cache.baltop.clear();
		SQL.top();
        Cache.sumbal();
		if (Bukkit.getOnlinePlayers().size() == 0) {
		Cache.clearCache();
		}
	}
}
