package me.Yi.XConomy.Task;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.Cache_NonPlayer;
import me.Yi.XConomy.Data.DataCon;
import me.Yi.XConomy.Data.SQL;
import me.Yi.XConomy.Message.Messages;

public class Save extends BukkitRunnable {
	@Override
	public void run() {
		if (!Cache.bal_change.isEmpty() | !Cache_NonPlayer.bal_change.isEmpty()) {

			int x = 0;

			// =================================================================================
			Connection co = SQL.mcon.getConnection();

			if (!Cache.bal_change.isEmpty()) {
				List<UUID> uids = Arrays
						.asList(Cache.bal_change.keySet().toArray(new UUID[Cache.bal_change.keySet().size()]));
				x = x + DataCon.save(co, uids);
				Cache.bal_change.clear();
			}

			if (XConomy.config.getBoolean("Settings.non-player-account") && !Cache_NonPlayer.bal_change.isEmpty()) {
				List<String> accounts = Arrays.asList(Cache_NonPlayer.bal_change.keySet()
						.toArray(new String[Cache_NonPlayer.bal_change.keySet().size()]));
				x = x + DataCon.save_non(co, accounts);
				Cache_NonPlayer.bal_change.clear();
			}
			
			SQL.mcon.closeHikariConnection(co);
			
			if (XConomy.config.getBoolean("Settings.autosave-message")) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&7[XConomy] " + Messages.sysmess("&a&l数据缓存已保存 &7>>> ") + x + Messages.sysmess("&a&l 条")));
			}

			// =================================================================================
			Cache.baltop_papi.clear();
			Cache.baltop.clear();
			SQL.top();
			Cache.sumbal();
			if (Bukkit.getOnlinePlayers().size() == 0) {
				Cache.clearCache();
			}
		}
	}
}
