package me.Yi.XConomy.Task;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.Cache_NonPlayer;
import me.Yi.XConomy.Data.DataCon;
import me.Yi.XConomy.Data.YML;
import me.Yi.XConomy.Message.Messages;

public class Save extends BukkitRunnable {
	@Override
	public void run() {
		if (!Cache.bal.isEmpty()) {
			List<UUID> ol = new ArrayList<UUID>();
			for (Player pp : Bukkit.getOnlinePlayers()) {
				ol.add(pp.getUniqueId());
			}

			int x = 0;

			// =================================================================================
			Connection co = null;
			if (XConomy.config.getBoolean("Settings.mysql")) {
				co = XConomy.mysqldb.getcon();
			}
			List<UUID> uids = Arrays.asList(Cache.bal.keySet().toArray(new UUID[Cache.bal.keySet().size()]));
			x = DataCon.save(co, uids, ol);

			if (XConomy.config.getBoolean("Settings.non-player-account")) {
				List<String> accounts = Arrays
						.asList(Cache_NonPlayer.bal.keySet().toArray(new String[Cache_NonPlayer.bal.keySet().size()]));
				x = x + DataCon.save_non(co, accounts);
			}

			if (XConomy.config.getBoolean("Settings.mysql")) {
				XConomy.mysqldb.closep(co);
			}

			// =================================================================================
			if (ol.isEmpty()) {
				Cache.bal.clear();
			}

			if (XConomy.config.getBoolean("Settings.mysql")) {
				Cache.baltop();
			} else {
				YML.save(YML.pd, DataCon.userdata);
				if (XConomy.config.getBoolean("Settings.non-player-account")) {
					YML.save(YML.pdnon, DataCon.nonpdata);
				}
				Cache.baltop();
			}
			if (XConomy.config.getBoolean("Settings.autosave-message")) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&7[XConomy] " + Messages.sysmess("&a&l数据缓存已保存 &7>>> ") + x + Messages.sysmess("&a&l 条")));
			}
			return;
		}

	}
}