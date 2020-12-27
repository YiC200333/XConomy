package me.yic.xconomy.task;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SendMessTaskS extends BukkitRunnable {
	private final ByteArrayOutputStream stream;
	private final UUID u;
	private final Boolean isAdd;
	private final PlayerData pd;

	public SendMessTaskS(ByteArrayOutputStream stream, UUID u, Boolean isAdd, PlayerData pd) {
		this.stream = stream;
		this.u = u;
		this.isAdd = isAdd;
		this.pd = pd;
	}

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), "xconomy:acb", this.stream.toByteArray());
		if (u != null) {
			DataCon.save(u, isAdd, pd);
		}
	}
}