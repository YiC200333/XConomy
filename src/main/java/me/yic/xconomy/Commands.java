package me.yic.xconomy;

import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.message.Messages;
import me.yic.xconomy.message.MessagesManager;
import me.yic.xconomy.task.SendMessTaskS;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String commandName = cmd.getName().toLowerCase();
		switch (commandName) {
			case "xconomy": {
				if (sender.isOp()) {
					if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
						XConomy.getInstance().reloadMessages();
						sender.sendMessage(sendMessage("prefix") + Messages.systemMessage("§amessage.yml重载成功"));
						return true;
					}
				}
				if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
					sendHelpMessage(sender);
					return true;
				}
				showVersion(sender);
				break;
			}

			case "balancetop": {
				if (!(sender.isOp() || sender.hasPermission("xconomy.user.balancetop"))) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("no_permission"));
					return true;
				}

				if (Cache.baltop.isEmpty()) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("top_nodata"));
					return true;
				}

				sender.sendMessage(sendMessage("top_title"));
				sender.sendMessage(sendMessage("sum_text")
						.replace("%balance%", DataFormat.shown((Cache.sumbalance))));

				List<String> topNames = Cache.baltop_papi;
				int placement = 0;
				for (String topName : topNames) {
					placement++;
					sender.sendMessage(sendMessage("top_text")
							.replace("%index%", String.valueOf(placement))
							.replace("%player%", topName)
							.replace("%balance%", DataFormat.shown((Cache.baltop.get(topName)))));
				}

				if (checkMessage("top_subtitle"))
					sender.sendMessage(sendMessage("top_subtitle"));

				break;
			}

			case "pay": {
				if (!(sender instanceof Player)) {
					sender.sendMessage(sendMessage("prefix") + Messages.systemMessage("§6控制台无法使用该指令"));
					return true;
				}

				if (!(sender.isOp() || sender.hasPermission("xconomy.user.pay"))) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("no_permission"));
					return true;
				}

				if (args.length != 2) {
					sendHelpMessage(sender);
					return true;
				}

				if (sender.getName().equalsIgnoreCase(args[0])) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("pay_self"));
					return true;
				}

				if (!isDouble(args[1])) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
					return true;
				}

				BigDecimal amount = DataFormat.formatString(args[1]);
				String amountFormatted = DataFormat.shown(amount);
				BigDecimal bal_sender = Cache.getBalanceFromCacheOrDB(((Player) sender).getUniqueId());

				if (bal_sender.compareTo(amount) < 0) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("pay_fail")
							.replace("%amount%", amountFormatted));
					return true;
				}

				Player target = Bukkit.getPlayerExact(args[0]);
				UUID targetUUID = Cache.translateUUID(args[0]);
				if (targetUUID == null) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("noaccount"));
					return true;
				}

				BigDecimal bal_target = Cache.getBalanceFromCacheOrDB(targetUUID);
				if (DataFormat.isMAX(bal_target.add(amount))) {
					sender.sendMessage(sendMessage("prefix") + sendMessage("over_maxnumber"));
					return true;
				}

				String com = commandName + " " + args[0] + " " + args[1];
						Cache.change(((Player) sender).getUniqueId(), amount, false, "PLAYER_COMMAND", ((Player) sender).getName(), com);
				sender.sendMessage(sendMessage("prefix") + sendMessage("pay")
						.replace("%player%", args[0])
						.replace("%amount%", amountFormatted));

				Cache.change(targetUUID, amount, true, "PLAYER_COMMAND", args[0], com);
				String mess = sendMessage("prefix") + sendMessage("pay_receive")
						.replace("%player%", sender.getName())
						.replace("%amount%", amountFormatted);

				if (target == null) {
					broadcastSendMessage(args[0], mess);
					return true;
				}

				target.sendMessage(mess);
				break;
			}

			case "money":
			case "balance":
			case "economy": {

				switch (args.length) {
					case 0: {
						if (!(sender instanceof Player)) {
							sender.sendMessage(sendMessage("prefix") + Messages.systemMessage("§6控制台无法使用该指令"));
							return true;
						}

						if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance"))) {
							sender.sendMessage(sendMessage("prefix") + sendMessage("no_permission"));
							return true;
						}

						Player player = (Player) sender;
						BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId());
						sender.sendMessage(sendMessage("prefix") + sendMessage("balance")
								.replace("%balance%", DataFormat.shown((a))));

						break;
					}

					case 1: {
						if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance.other"))) {
							sender.sendMessage(sendMessage("prefix") + sendMessage("no_permission"));
							return true;
						}

						UUID targetUUID = Cache.translateUUID(args[0]);
						if (targetUUID == null) {
							sender.sendMessage(sendMessage("prefix") + sendMessage("noaccount"));
							return true;
						}

						BigDecimal targetBalance = Cache.getBalanceFromCacheOrDB(targetUUID);
						sender.sendMessage(sendMessage("prefix") + sendMessage("balance_other")
								.replace("%player%", args[0])
								.replace("%balance%", DataFormat.shown((targetBalance))));

						break;
					}

					case 3: {
						if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give")
								| sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set"))) {
							sendHelpMessage(sender);
							return true;
						}

						if (!check()) {
							sender.sendMessage(sendMessage("prefix") + Messages.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
							return true;
						}

						if (!isDouble(args[2])) {
							sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
							return true;
						}

						BigDecimal amount = DataFormat.formatString(args[2]);
						String amountFormatted = DataFormat.shown(amount);
						Player target = Bukkit.getPlayerExact(args[1]);
						UUID targetUUID = Cache.translateUUID(args[1]);

						if (targetUUID == null) {
							sender.sendMessage(sendMessage("prefix") + sendMessage("noaccount"));
							return true;
						}

						String com = commandName + " " + args[0] + " " + args[1] + " " + args[2];
						switch (args[0].toLowerCase()) {
							case "give": {
								if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give"))) {
									sendHelpMessage(sender);
									return true;
								}

								BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID);
								if (DataFormat.isMAX(bal.add(amount))) {
									sender.sendMessage(sendMessage("prefix") + sendMessage("over_maxnumber"));
									return true;
								}

								Cache.change(targetUUID, amount, true,"ADMIN_COMMAND", args[1], com);
								sender.sendMessage(sendMessage("prefix") + sendMessage("money_give")
										.replace("%player%", args[1])
										.replace("%amount%", amountFormatted));

								if (checkMessage("money_give_receive")) {
									String message = sendMessage("prefix") + sendMessage("money_give_receive")
											.replace("%player%", args[1])
											.replace("%amount%", amountFormatted);

									if (target == null) {
										broadcastSendMessage(args[1], message);
										return true;
									}

									target.sendMessage(message);

								}
								break;
							}

							case "take": {
								if (!(sender.isOp() | sender.hasPermission("xconomy.admin.take"))) {
									sendHelpMessage(sender);
									return true;
								}

								BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID);
								if (bal.compareTo(amount) < 0) {
									sender.sendMessage(sendMessage("prefix") + sendMessage("money_take_fail")
											.replace("%player%", args[1])
											.replace("%amount%", amountFormatted));

									return true;
								}

								Cache.change(targetUUID, amount, false,"ADMIN_COMMAND", args[1], com);
								sender.sendMessage(sendMessage("prefix") + sendMessage("money_take")
										.replace("%player%", args[1])
										.replace("%amount%", amountFormatted));

								if (checkMessage("money_take_receive")) {
									String mess = sendMessage("prefix") + sendMessage("money_take_receive")
											.replace("%player%", args[1]).replace("%amount%", amountFormatted);

									if (target == null) {
										broadcastSendMessage(args[1], mess);
										return true;
									}

									target.sendMessage(mess);

								}
								break;
							}

							case "set": {
								if (!(sender.isOp() | sender.hasPermission("xconomy.admin.set"))) {
									sendHelpMessage(sender);
									return true;
								}

								Cache.change(targetUUID, amount, null,"ADMIN_COMMAND", args[1], com);
								sender.sendMessage(sendMessage("prefix") + sendMessage("money_set")
										.replace("%player%", args[1])
										.replace("%amount%", amountFormatted));

								if (checkMessage("money_set_receive")) {
									String mess = sendMessage("prefix") + sendMessage("money_set_receive")
											.replace("%player%", args[1])
											.replace("%amount%", amountFormatted);

									if (target == null) {
										broadcastSendMessage(args[1], mess);
										return true;
									}

									target.sendMessage(mess);

								}
								break;
							}

							default: {
								sendHelpMessage(sender);
								break;
							}

						}
						break;
					}

					default: {
						sendHelpMessage(sender);
						break;
					}

				}
				break;
			}

			default: {
				sendHelpMessage(sender);
				break;
			}

		}

		return true;

	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean isDouble(String s) {
		try {
			Double.parseDouble(s);

			if (!DataFormat.isInteger) {
				return Double.parseDouble(s) >= 0.01;
			}

			if (Double.parseDouble(s) >= 1) {
				return !DataFormat.isMAX(DataFormat.formatString(s));
			}

		} catch (NumberFormatException ignored) {
		}

		return false;
	}

	public boolean check() {
		return !(Bukkit.getOnlinePlayers().isEmpty() & XConomy.isBungeecord());
	}

	@SuppressWarnings("ConstantConditions")
	public boolean checkMessage(String message) {
		return !MessagesManager.messageFile.getString(message).equals("");
	}

	@SuppressWarnings("ConstantConditions")
	public String sendMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message));
	}

	public void showVersion(CommandSender sender) {
		sender.sendMessage(sendMessage("prefix") + "§6 XConomy §f(Version: "
				+ XConomy.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + Messages.getAuthor());
	}

	private void sendHelpMessage(CommandSender sender) {
		sender.sendMessage(sendMessage("help_title_full"));
		sender.sendMessage(sendMessage("help1"));
		sender.sendMessage(sendMessage("help2"));
		sender.sendMessage(sendMessage("help3"));
		sender.sendMessage(sendMessage("help4"));
		if (sender.isOp() | sender.hasPermission("xconomy.admin.give")) {
			sender.sendMessage(sendMessage("help5"));
		}
		if (sender.isOp() | sender.hasPermission("xconomy.admin.take")) {
			sender.sendMessage(sendMessage("help6"));
		}
		if (sender.isOp() | sender.hasPermission("xconomy.admin.set")) {
			sender.sendMessage(sendMessage("help7"));
		}
	}

	public static void broadcastSendMessage(String s, String s1) {
		if (!XConomy.isBungeecord()) {
			return;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		try {
			output.writeUTF("message");
			output.writeUTF(XConomy.getSign());
			output.writeUTF(s);
			output.writeUTF(s1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		new SendMessTaskS(stream, null, null, null, null).runTaskAsynchronously(XConomy.getInstance());

	}

}
