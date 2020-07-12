package me.Yi.XConomy.Task;

import java.io.ByteArrayOutputStream;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;

public class SendMessTask extends BukkitRunnable {
	private final ByteArrayOutputStream stream;

	public SendMessTask(ByteArrayOutputStream stream) {
		this.stream = stream;
	}

	@Override
	public void run() {
        ((Player)Bukkit.getOnlinePlayers().iterator().next()).sendPluginMessage(XConomy.getInstance(), "xconomy:acb", this.stream.toByteArray());
	}
}