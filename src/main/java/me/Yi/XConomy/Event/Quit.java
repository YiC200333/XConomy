package me.Yi.XConomy.Event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import me.Yi.XConomy.Data.Cache;

public class Quit implements Listener {

	@EventHandler
	public void qu(PlayerQuitEvent event) {
		if (Bukkit.getOnlinePlayers().size() == 1) {
			Cache.clearCache();;
		}
	}

}
