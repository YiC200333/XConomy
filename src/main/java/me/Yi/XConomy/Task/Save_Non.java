package me.Yi.XConomy.Task;

import java.math.BigDecimal;
import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.Data.DataCon;

public class Save_Non extends BukkitRunnable {

	private final String account;
	private final BigDecimal amount;
	private final int type;
	
	public Save_Non(String account,BigDecimal amount,Integer type) {
		this.account = account;
		this.amount = amount;
		this.type = type;
	}
	
	@Override
	public void run() {
		DataCon.save_non(account, amount, type);
	}
}