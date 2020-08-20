package me.Yi.XConomy.Event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Task.Save;

public class Quit implements Listener {

	@EventHandler
	public void qu(PlayerQuitEvent event) {
		if (Bukkit.getOnlinePlayers().size() == 1) {
			if (!Cache.bal_change.isEmpty()) {
			new Save().runTaskAsynchronously(XConomy.getInstance());
			}
			Cache.clearCache();
		}
	}

}
