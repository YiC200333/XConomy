package me.yi.xconomy;

import me.yi.xconomy.listeners.BCsync;
import net.md_5.bungee.api.plugin.Plugin;

public class XConomyBungee extends Plugin {
	private static XConomyBungee instance;

	@Override
	public void onEnable() {

		instance = this;

		getProxy().registerChannel("xconomy:aca");
		getProxy().registerChannel("xconomy:acb");
		getProxy().getPluginManager().registerListener(this, new BCsync());

		getLogger().info("XConomy successfully enabled!");
		getLogger().info("===== YiC =====");

	}

	@Override
	public void onDisable() {
		getLogger().info("XConomy successfully disabled!");
	}

	public static XConomyBungee getInstance() {
		return instance;
	}
}
