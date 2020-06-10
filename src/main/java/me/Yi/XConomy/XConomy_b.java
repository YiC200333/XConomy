package me.Yi.XConomy;

import me.Yi.XConomy.Event.BCsync;
import net.md_5.bungee.api.plugin.Plugin;

public class XConomy_b extends Plugin {

	@Override
	public void onEnable() {

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

}
