package me.yic.xconomy.listeners;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.task.Updater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ConnectionListeners implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (Bukkit.getOnlinePlayers().size() == 1) {
			Cache.clearCache();
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player a = event.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				DataCon.newPlayer(a);
			}
		}.runTaskAsynchronously(XConomy.getInstance());

		if (!XConomy.config.getBoolean("Settings.semi-online-mode")) {
			Cache.translateUUID(a.getName(), a);
		}

		if (a.isOp()) {
			notifyUpdate(a);
		}
	}


	private void notifyUpdate(Player player) {
		if (!(XConomy.checkup() & Updater.old)) {
			return;
		}

		if (XConomy.getInstance().lang().equalsIgnoreCase("Chinese")
				| XConomy.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
			player.sendMessage("§f[XConomy]§b发现新版本 " + Updater.newVersion);
			player.sendMessage("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html");
		} else {
			player.sendMessage("§f[XConomy]§bDiscover the new version " + Updater.newVersion);
			player.sendMessage("§f[XConomy]§ahttps://www.spigotmc.org/resources/xconomy.75669/");
		}

	}

}
