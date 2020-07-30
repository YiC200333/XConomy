package me.Yi.XConomy.Task;

import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataCon;
import me.Yi.XConomy.Data.YML;

public class BalTop extends BukkitRunnable {
	@Override
	public void run() {

			if (XConomy.config.getBoolean("Settings.mysql")) {
				Cache.baltop();
			} else {
				YML.save(YML.pd, DataCon.userdata);
				if (XConomy.config.getBoolean("Settings.non-player-account")) {
					YML.save(YML.pdnon, DataCon.nonpdata);
				}
				Cache.baltop();
			}

	}
}