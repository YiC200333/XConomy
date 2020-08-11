package me.Yi.XConomy.Task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.MySQL;
import me.Yi.XConomy.Data.YML;

public class BalTop extends BukkitRunnable {
	@Override
	public void run() {
		if (XConomy.config.getBoolean("Settings.mysql")) {
			Cache.baltop_papi.clear();
			Cache.baltop.clear();
			MySQL.top();
		} else {
			YML.savetop();
		}
        Cache.sumbal();
		if (Bukkit.getOnlinePlayers().size() == 0) {
		Cache.cclean();
		}
	}
}