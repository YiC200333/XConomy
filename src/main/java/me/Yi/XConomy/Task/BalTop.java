package me.Yi.XConomy.Task;

import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.YML;

public class BalTop extends BukkitRunnable {
	@Override
	public void run() {
		Cache.baltop_papi.clear();
		if (XConomy.config.getBoolean("Settings.mysql")) {
			Cache.baltop.clear();
			XConomy.mysqldb.top();
		} else {
			YML.savetop();
		}

		Cache.cclean();
	}
}