package me.yic.xconomy.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class RecordData {
	private final String type;
	private final String uid;
	private final String player;
	private final Double balance;
	private final Double amount;
	private final String operation;
	private final String date;
	private final String command;

	public RecordData(String type, UUID uid, String player, BigDecimal balance, BigDecimal amount, Boolean isAdd, String command) {
		this.type = type;
		if (uid == null){
		    this.uid = "N/A";
		}else {
			this.uid = uid.toString();
		}
		this.player = player;
		this.balance = balance.doubleValue();
		this.amount = amount.doubleValue();
		String operation = "SET";
		if (isAdd) {
			operation = "DEPOSIT";
		} else {
			operation = "WITHDRAW";
		}
		this.operation = operation;
		this.date = (new Date()).toString();
		this.command = command;
	}

	public String gettype() {
		return type;
	}

	public String getuid() {
		return uid;
	}

	public String getplayer() {
		return player;
	}

	public Double getbalance() {
		return balance;
	}

	public Double getamount() {
		return amount;
	}

	public String getoperation() {
		return operation;
	}

	public String getdate() {
		return date;
	}

	public String getcommand() {
		return command;
	}
}
