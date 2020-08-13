package me.Yi.XConomy;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataFormat;
import me.Yi.XConomy.Message.MessManage;
import me.Yi.XConomy.Message.Messages;
import me.Yi.XConomy.Task.SendMessTaskS;

public class cmd implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("xconomy")) {
			if (sender.isOp()) {
				if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
					XConomy.getInstance().reloadmess();
					sender.sendMessage(sendmess("prefix") + Messages.sysmess("§amessage.yml重载成功"));
				} else {
					showver(sender);
				}
			} else {
				showver(sender);
			}
			return true;
		}
		if (command.getName().equalsIgnoreCase("balancetop")) {
			if (sender.isOp() | sender.hasPermission("xconomy.user.balancetop")) {
				if (Cache.baltop.isEmpty()) {
					sender.sendMessage(sendmess("prefix") + sendmess("top_nodata"));
				} else {
					sender.sendMessage(sendmess("top_title"));
					if (XConomy.config.getBoolean("Settings.mysql")) {
						sender.sendMessage(sendmess("sum_text").replace("%balance%",
								DataFormat.shown((Cache.sumbalance))));
					}
					int x = 0;
					List<String> topname = Cache.baltop_papi;
					for (String e : topname) {
						x = x + 1;
						sender.sendMessage(sendmess("top_text").replace("%index%", String.valueOf(x))
								.replace("%player%", e).replace("%balance%", DataFormat.shown((Cache.baltop.get(e)))));
					}

				}
			} else {
				sender.sendMessage(sendmess("prefix") + sendmess("no_permission"));
			}

			return true;
		}
		if (command.getName().equalsIgnoreCase("money") | command.getName().equalsIgnoreCase("balance")) {
			if (args.length == 0 & sender instanceof Player) {
				if (sender.isOp() | sender.hasPermission("xconomy.user.balance")) {
					Player p = (Player) sender;
					BigDecimal a = Cache.getbal(p.getUniqueId());
					sender.sendMessage(
							sendmess("prefix") + sendmess("balance").replace("%balance%", DataFormat.shown((a))));
				} else {
					sender.sendMessage(sendmess("prefix") + sendmess("no_permission"));
				}
			} else if (args.length == 1) {
				if (sender.isOp() | sender.hasPermission("xconomy.user.balance.other")) {
					UUID uls = Cache.translateuid(args[0]);
					if (uls != null) {
						BigDecimal a = Cache.getbal(uls);
						sender.sendMessage(sendmess("prefix") + sendmess("balance_other").replace("%player%", args[0])
								.replace("%balance%", DataFormat.shown((a))));

					} else {
						sender.sendMessage(sendmess("prefix") + sendmess("noaccount"));
					}
				} else {
					sender.sendMessage(sendmess("prefix") + sendmess("no_permission"));
				}
			} else if (sender.isOp() | sender.hasPermission("xconomy.admin.give")
					| sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set")) {
				if (args.length == 3) {
					if (args[0].equalsIgnoreCase("give") | args[0].equalsIgnoreCase("take")
							| args[0].equalsIgnoreCase("set")) {
						if (check()) {
							if (isright(args[2])) {
								BigDecimal amount = DataFormat.formatsb(args[2]);
								String messam = DataFormat.shown(amount);
								Player p = Bukkit.getPlayer(args[1]);
								UUID uls = Cache.translateuid(args[1]);
								if (uls != null) {
									if (args[0].equalsIgnoreCase("give")) {
										if (sender.isOp() | sender.hasPermission("xconomy.admin.give")) {
											Cache.change(uls, amount, 1);
											sender.sendMessage(sendmess("prefix") + sendmess("money_give")
													.replace("%player%", args[1]).replace("%amount%", messam));
											String mess = sendmess("prefix") + sendmess("money_give_receive")
													.replace("%player%", args[1]).replace("%amount%", messam);
											if (p != null) {
												p.sendMessage(mess);
											} else {
												bcsendmess(args[1], mess);
											}
										} else {
											helpm(sender);
										}
									} else if (args[0].equalsIgnoreCase("take")) {
										if (sender.isOp() | sender.hasPermission("xconomy.admin.take")) {
											BigDecimal bal = Cache.getbal(uls);
											if (bal.compareTo(amount)>=0) {
												Cache.change(uls, amount, 2);
												sender.sendMessage(sendmess("prefix") + sendmess("money_take")
														.replace("%player%", args[1]).replace("%amount%", messam));
												String mess = sendmess("prefix") + sendmess("money_take_receive")
														.replace("%player%", args[1]).replace("%amount%", messam);
												if (p != null) {
													p.sendMessage(mess);
												} else {
													bcsendmess(args[1], mess);
												}
											} else {
												sender.sendMessage(sendmess("prefix") + sendmess("money_take_fail")
														.replace("%player%", args[1]).replace("%amount%", messam));
											}
										} else {
											helpm(sender);
										}
									} else if (args[0].equalsIgnoreCase("set")) {
										if (sender.isOp() | sender.hasPermission("xconomy.admin.set")) {
											Cache.change(uls, amount, 3);
											sender.sendMessage(sendmess("prefix") + sendmess("money_set")
													.replace("%player%", args[1]).replace("%amount%", messam));
											String mess = sendmess("prefix") + sendmess("money_set_receive")
													.replace("%player%", args[1]).replace("%amount%", messam);
											if (p != null) {
												p.sendMessage(mess);
											} else {
												bcsendmess(args[1], mess);
											}
										} else {
											helpm(sender);
										}
									}
								} else {
									sender.sendMessage(sendmess("prefix") + sendmess("noaccount"));
								}
							} else {
								sender.sendMessage(sendmess("prefix") + sendmess("invalid"));
							}
						} else {
							sender.sendMessage(sendmess("prefix") + Messages.sysmess("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
						}
					} else {
						helpm(sender);
					}
				} else {
					helpm(sender);
				}
			} else {
				helpm(sender);
			}

			return true;

		}
		if (command.getName().equalsIgnoreCase("pay")) {
			if (args.length == 2) {
				if (sender instanceof Player) {
					if (sender.isOp() | sender.hasPermission("xconomy.user.pay")) {
						if (isright(args[1])) {
							BigDecimal amount = DataFormat.formatsb(args[1]);
							String messam = DataFormat.shown(amount);
							BigDecimal bal = Cache.getbal(((Player) sender).getUniqueId());
							if (!sender.getName().equalsIgnoreCase(args[0])) {
								if (bal.compareTo(amount) >= 0) {
									Player p = Bukkit.getPlayer(args[0]);
									UUID uls = Cache.translateuid(args[0]);
									if (uls != null) {
										Cache.change(((Player) sender).getUniqueId(), amount, 2);
										sender.sendMessage(sendmess("prefix") + sendmess("pay")
												.replace("%player%", args[0]).replace("%amount%", messam));
										Cache.change(uls, amount, 1);
										String mess = sendmess("prefix") + sendmess("pay_receive")
												.replace("%player%", sender.getName()).replace("%amount%", messam);
										if (p != null) {
											p.sendMessage(mess);
										} else {
											bcsendmess(args[0], mess);
										}
									} else {
										sender.sendMessage(sendmess("prefix") + sendmess("noaccount"));
									}

								} else {
									sender.sendMessage(
											sendmess("prefix") + sendmess("pay_fail").replace("%amount%", messam));
								}
							} else {
								sender.sendMessage(sendmess("prefix") + sendmess("pay_self"));
							}
						} else {
							sender.sendMessage(sendmess("prefix") + sendmess("invalid"));
						}
					} else {
						sender.sendMessage(sendmess("prefix") + sendmess("no_permission"));
					}
				} else {
					sender.sendMessage(sendmess("prefix") + Messages.sysmess("§6控制台无法使用该指令"));
				}
			} else {
				helpm(sender);
			}
		}
		return true;

	}

	public boolean isright(String str) {
		try {
			Double.parseDouble(str);
			if (DataFormat.isi) {
				if (Double.parseDouble(str) >= 1) {
					return DataFormat.right(DataFormat.formatsb(str));
				} else {
					return false;
				}
			} else {
				if (Double.parseDouble(str) >= 0.01) {
					return true;
				} else {
					return false;
				}
			}
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public boolean check() {
		if (Bukkit.getOnlinePlayers().isEmpty() & XConomy.isbc()) {
			return false;
		}
		return true;
	}

	public String sendmess(String mess) {
		String xxx = ChatColor.translateAlternateColorCodes('&', MessManage.mess.getString(mess));
		return xxx;
	}

	public void showver(CommandSender sender) {
		sender.sendMessage(sendmess("prefix") + "§6 XConomy §f(Version: "
				+ XConomy.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + Messages.getau());
	}

	private void helpm(CommandSender sender) {
		sender.sendMessage(sendmess("help_title_full"));
		sender.sendMessage(sendmess("help1"));
		sender.sendMessage(sendmess("help2"));
		sender.sendMessage(sendmess("help3"));
		sender.sendMessage(sendmess("help4"));
		if (sender.isOp() | sender.hasPermission("xconomy.admin.give")) {
			sender.sendMessage(sendmess("help5"));
		}
		if (sender.isOp() | sender.hasPermission("xconomy.admin.take")) {
			sender.sendMessage(sendmess("help6"));
		}
		if (sender.isOp() | sender.hasPermission("xconomy.admin.set")) {
			sender.sendMessage(sendmess("help7"));
		}
	}

	public static void bcsendmess(String u, String b) {
		if (XConomy.isbc()) {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream output = new DataOutputStream(stream);
			try {
				output.writeUTF("message");
				output.writeUTF(XConomy.getsign());
				output.writeUTF(u);
				output.writeUTF(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new SendMessTaskS(stream).runTaskAsynchronously(XConomy.getInstance());
		}
	}

}
