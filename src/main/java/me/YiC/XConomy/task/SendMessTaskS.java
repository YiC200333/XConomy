package me.YiC.XConomy.task;

import me.YiC.XConomy.data.DataCon;
import me.YiC.XConomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.UUID;

public class SendMessTaskS extends BukkitRunnable {
	private final ByteArrayOutputStream stream;
	private final UUID u;
	private final BigDecimal amount;
	private final Boolean type;

	public SendMessTaskS(ByteArrayOutputStream stream, UUID u, BigDecimal amount, Boolean type) {
		this.stream = stream;
		this.u = u;
		this.amount = amount;
		this.type = type;
	}

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), "xconomy:acb", this.stream.toByteArray());
		if (u != null) {
			DataCon.save(u, amount, type);
		}
	}
}