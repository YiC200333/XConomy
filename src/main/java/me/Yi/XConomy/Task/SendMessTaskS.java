package me.Yi.XConomy.Task;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.DataCon;

public class SendMessTaskS extends BukkitRunnable {
	private final ByteArrayOutputStream stream;
	private final UUID u;
	private final BigDecimal amount;

	public SendMessTaskS(ByteArrayOutputStream stream,UUID u,BigDecimal amount) {
		this.stream = stream;
		this.u = u;
		this.amount = amount;
	}

	@Override
	public void run() {
        ((Player)Bukkit.getOnlinePlayers().iterator().next()).sendPluginMessage(XConomy.getInstance(), "xconomy:acb", this.stream.toByteArray());
		if (u!=null) {
		DataCon.save(u, amount);
		}
	}
}