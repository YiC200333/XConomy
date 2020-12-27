package me.yic.xconomy.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class PlayerData {
	private final String type;
	private final String uid;
	private final String player;
	private final BigDecimal balance;
	private final BigDecimal amount;
	private final BigDecimal newbalance;
	private final String operation;
	private final String date;
	private String command;

	public PlayerData(String type, UUID uid, String player, BigDecimal balance, BigDecimal amount, BigDecimal newbalance, Boolean isAdd, String command) {
		this.type = type;
		if (uid == null){
		    this.uid = "N/A";
		}else {
			this.uid = uid.toString();
		}
		if (player == null){
			this.player = "N/A";
		}else {
			this.player = player;
		}
		this.balance = balance;
		this.amount = amount;
		this.newbalance = newbalance;
		String operation = "SET";
		if (isAdd != null) {
			if (isAdd) {
				operation = "DEPOSIT";
			} else {
				operation = "WITHDRAW";
			}
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

	public BigDecimal getbalance() {
		return balance;
	}

	public BigDecimal getamount() {
		return amount;
	}

	public BigDecimal getnewbalance() {
		return newbalance;
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

	public void addcachecorrection() {
		command = command+"   (Cache Correction)";
	}
}
