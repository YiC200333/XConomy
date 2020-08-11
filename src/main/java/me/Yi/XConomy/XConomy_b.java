package me.Yi.XConomy;

import me.Yi.XConomy.Event.BCsync;
import net.md_5.bungee.api.plugin.Plugin;

public class XConomy_b extends Plugin {
	private static XConomy_b instance;

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

	public static XConomy_b getInstance() {
		return instance;
	}
}
