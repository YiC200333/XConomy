package me.Yi.XConomy.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.DataCon;
import me.Yi.XConomy.Task.Updater;

public class Join implements Listener {

	@EventHandler
	public void joine(PlayerJoinEvent event) {
		Double ii = XConomy.config.getDouble("Settings.initial-bal");
		Player a = event.getPlayer();
		new BukkitRunnable() {
			@Override
			public void run() {
				DataCon.newplayer(a, ii);
			}
		}.runTaskAsynchronously(XConomy.getInstance());
		if (a.isOp()) {
			updaterm(a);
		}
	}

	private void updaterm(Player a) {
		if (XConomy.checkup() & Updater.isold) {
			if (XConomy.getInstance().lang().equalsIgnoreCase("Chinese")
					| XConomy.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
				a.sendMessage("§f[XConomy]§b发现新版本 " + Updater.vs);
				a.sendMessage("§f[XConomy]§ahttps://www.mcbbs.net/thread-962904-1-1.html");
			} else {
				a.sendMessage("§f[XConomy]§bDiscover the new version " + Updater.vs);
				a.sendMessage("§f[XConomy]§ahttps://www.spigotmc.org/resources/xconomy.75669/");
			}
		}
	}

}
