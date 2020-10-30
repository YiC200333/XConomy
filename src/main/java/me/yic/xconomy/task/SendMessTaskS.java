package me.yic.xconomy.task;

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.RecordData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.UUID;

public class SendMessTaskS extends BukkitRunnable {
	private final ByteArrayOutputStream stream;
	private final UUID u;
	private final BigDecimal newbalance;
	private final BigDecimal balance;
	private final BigDecimal amount;
	private final Boolean type;
	private final RecordData x;

	public SendMessTaskS(ByteArrayOutputStream stream, UUID u, BigDecimal newbalance, BigDecimal balance, BigDecimal amount, Boolean type, RecordData x) {
		this.stream = stream;
		this.u = u;
		this.balance = balance;
		this.newbalance = newbalance;
		this.amount = amount;
		this.type = type;
		this.x = x;
	}

	@Override
	public void run() {
		Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(), "xconomy:acb", this.stream.toByteArray());
		if (u != null) {
			DataCon.save(u, newbalance, balance, amount, type, x);
		}
	}
}