/*
 *  This file (CommandHandler.java) is a part of project XConomy
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.xconomy;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.utils.ServerINFO;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandHandler {

    public static boolean onCommand(CommandSender sender, String commandName, String[] args) {
        switch (commandName) {
            case "xconomy": {
                if (sender.isOp()) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        XConomy.getInstance().reloadMessages();
                        sender.sendMessage(sendMessage("prefix") + MessagesManager.systemMessage("§amessage.yml重载成功"));
                        return true;
                    }
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender, 1);
                    return true;
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender, Integer.valueOf(args[1]));
                    return true;
                }
                showVersion(sender);
                break;
            }

            case "balancetop": {
                if (args.length == 0) {

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
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("hide") || args[0].equalsIgnoreCase("display")) {

                        if (!(sender.isOp() || sender.hasPermission("xconomy.admin.balancetop"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        UUID targetUUID = Cache.translateUUID(args[1], null);

                        if (targetUUID == null) {
                            sender.sendMessage(sendMessage("prefix") + sendMessage("noaccount"));
                            return true;
                        }

                        if (args[0].equalsIgnoreCase("hide")) {
                            DataCon.setTopBalHide(targetUUID, 1);
                            sender.sendMessage(sendMessage("prefix") + sendMessage("top_hidden").replace("%player%", args[1]));
                        } else if (args[0].equalsIgnoreCase("display")) {
                            DataCon.setTopBalHide(targetUUID, 0);
                            sender.sendMessage(sendMessage("prefix") + sendMessage("top_displayed").replace("%player%", args[1]));
                        }

                        break;
                    }
                }
            }

            case "pay": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(sendMessage("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                    return true;
                }

                if (!(sender.isOp() || sender.hasPermission("xconomy.user.pay"))) {
                    sender.sendMessage(sendMessage("prefix") + sendMessage("no_permission"));
                    return true;
                }

                if (args.length != 2) {
                    sendHelpMessage(sender, 1);
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

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
                    return true;
                }

                String amountFormatted = DataFormat.shown(amount);
                BigDecimal bal_sender = Cache.getBalanceFromCacheOrDB(((Player) sender).getUniqueId());

                if (bal_sender.compareTo(amount) < 0) {
                    sender.sendMessage(sendMessage("prefix") + sendMessage("pay_fail")
                            .replace("%amount%", amountFormatted));
                    return true;
                }

                Player target = Cache.getplayer(args[0]);
                UUID targetUUID = Cache.translateUUID(args[0], null);
                if (targetUUID == null) {
                    sender.sendMessage(sendMessage("prefix") + sendMessage("noaccount"));
                    return true;
                }
                
                if(target == null || !target.isOnline()) {
                  // Offline receiver permission node check with Vault API
                  Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                      if(!XConomy.vaultPerm.playerHas(null, Bukkit.getOfflinePlayer(targetUUID), "xconomy.user.receive")) {
                        sender.sendMessage(sendMessage("prefix") + sendMessage("no_receive_permission"));
                        return;
                      }
                      
                      // Same process as in lines 206 to 221
                      BigDecimal bal_target = Cache.getBalanceFromCacheOrDB(targetUUID);
                      if (DataFormat.isMAX(bal_target.add(amount))) {
                          sender.sendMessage(sendMessage("prefix") + sendMessage("over_maxnumber"));
                          return;
                      }

                      String com = commandName + " " + args[0] + " " + args[1];
                      Cache.change("PLAYER_COMMAND", ((Player) sender).getUniqueId(), sender.getName(), amount, false, com);
                      sender.sendMessage(sendMessage("prefix") + sendMessage("pay")
                              .replace("%player%", args[0])
                              .replace("%amount%", amountFormatted));

                      Cache.change("PLAYER_COMMAND", targetUUID, args[0], amount, true, com);
                      String mess = sendMessage("prefix") + sendMessage("pay_receive")
                              .replace("%player%", sender.getName())
                              .replace("%amount%", amountFormatted);

                      broadcastSendMessage(false, targetUUID, mess);
                    }});
                  return true;
                }else if (!target.hasPermission("xconomy.user.receive")) {
                  sender.sendMessage(sendMessage("prefix") + sendMessage("no_receive_permission"));
                  return true;
                }

                BigDecimal bal_target = Cache.getBalanceFromCacheOrDB(targetUUID);
                if (DataFormat.isMAX(bal_target.add(amount))) {
                    sender.sendMessage(sendMessage("prefix") + sendMessage("over_maxnumber"));
                    return true;
                }

                String com = commandName + " " + args[0] + " " + args[1];
                Cache.change("PLAYER_COMMAND", ((Player) sender).getUniqueId(), sender.getName(), amount, false, com);
                sender.sendMessage(sendMessage("prefix") + sendMessage("pay")
                        .replace("%player%", args[0])
                        .replace("%amount%", amountFormatted));

                Cache.change("PLAYER_COMMAND", targetUUID, args[0], amount, true, com);
                String mess = sendMessage("prefix") + sendMessage("pay_receive")
                        .replace("%player%", sender.getName())
                        .replace("%amount%", amountFormatted);

                target.sendMessage(mess);
                break;
            }

            case "money":
            case "balance":
            case "economy":
            case "eco": {

                switch (args.length) {
                    case 0: {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(sendMessage("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                            return true;
                        }

                        if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance"))) {
                            sender.sendMessage(sendMessage("prefix") + sendMessage("no_permission"));
                            return true;
                        }

                        Player player = (Player) sender;


                        if (XConomy.config.getBoolean("Settings.cache-correction")) {
                            Cache.refreshFromCache(player.getUniqueId());
                        }

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

                        UUID targetUUID = Cache.translateUUID(args[0], null);
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

                    case 3:
                    case 4: {
                        if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give")
                                | sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (check()) {
                            sender.sendMessage(sendMessage("prefix") + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        if (!isDouble(args[2])) {
                            sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(args[2]);
                        String amountFormatted = DataFormat.shown(amount);
                        Player target = Cache.getplayer(args[1]);
                        UUID targetUUID = Cache.translateUUID(args[1], null);
                        String reason = null;
                        if (args.length == 4) {
                            reason = args[3];
                        }

                        if (targetUUID == null) {
                            sender.sendMessage(sendMessage("prefix") + sendMessage("noaccount"));
                            return true;
                        }

                        String com = commandName + " " + args[0] + " " + args[1] + " " + args[2];
                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
                                    return true;
                                }

                                BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID);
                                if (DataFormat.isMAX(bal.add(amount))) {
                                    sender.sendMessage(sendMessage("prefix") + sendMessage("over_maxnumber"));
                                    return true;
                                }

                                Cache.change("ADMIN_COMMAND", targetUUID, args[1], amount, true, com);
                                sender.sendMessage(sendMessage("prefix") + sendMessage("money_give")
                                        .replace("%player%", args[1])
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | args.length == 4) {

                                    String message = sendMessage("prefix") + sendMessage("money_give_receive")
                                            .replace("%player%", args[1])
                                            .replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        message = sendMessage("prefix") + reason;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, targetUUID, message);
                                        return true;
                                    }

                                    target.sendMessage(message);

                                }
                                break;
                            }

                            case "take": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
                                    return true;
                                }

                                BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID);
                                if (bal.compareTo(amount) < 0) {
                                    sender.sendMessage(sendMessage("prefix") + sendMessage("money_take_fail")
                                            .replace("%player%", args[1])
                                            .replace("%amount%", amountFormatted));

                                    return true;
                                }

                                Cache.change("ADMIN_COMMAND", targetUUID, args[1], amount, false, com);
                                sender.sendMessage(sendMessage("prefix") + sendMessage("money_take")
                                        .replace("%player%", args[1])
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | args.length == 4) {
                                    String mess = sendMessage("prefix") + sendMessage("money_take_receive")
                                            .replace("%player%", args[1]).replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        mess = sendMessage("prefix") + reason;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, targetUUID, mess);
                                        return true;
                                    }

                                    target.sendMessage(mess);

                                }
                                break;
                            }

                            case "set": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.set"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                Cache.change("ADMIN_COMMAND", targetUUID, args[1], amount, null, com);
                                sender.sendMessage(sendMessage("prefix") + sendMessage("money_set")
                                        .replace("%player%", args[1])
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | args.length == 4) {
                                    String mess = sendMessage("prefix") + sendMessage("money_set_receive")
                                            .replace("%player%", args[1])
                                            .replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        mess = sendMessage("prefix") + reason;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, targetUUID, mess);
                                        return true;
                                    }

                                    target.sendMessage(mess);

                                }
                                break;
                            }

                            default: {
                                sendHelpMessage(sender, 1);
                                break;
                            }

                        }
                        break;
                    }

                    case 5: {
                        if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give")
                                | sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (!args[1].equals("*")) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (!(args[2].equalsIgnoreCase("all") | args[2].equalsIgnoreCase("online"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        if (ServerINFO.IsSemiOnlineMode && args[2].equalsIgnoreCase("online")) {
                            sender.sendMessage(sendMessage("prefix") + MessagesManager.systemMessage("§c该指令不支持在半正版模式中使用"));
                            return true;
                        }

                        if (check()) {
                            sender.sendMessage(sendMessage("prefix") + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        if (!isDouble(args[3])) {
                            sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(args[3]);

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sender.sendMessage(sendMessage("prefix") + sendMessage("invalid"));
                            return true;
                        }

                        String target = "AllPlayer";
                        if (args[2].equalsIgnoreCase("online")) {
                            target = "OnlinePlayer";
                        }

                        String amountFormatted = DataFormat.shown(amount);

                        String com = commandName + " " + args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + args[4];

                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                Cache.changeall(args[2], "ADMIN_COMMAND", amount, true, com);
                                sender.sendMessage(sendMessage("prefix") + sendMessage("money_give")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = sendMessage("prefix") + args[4];
                                Bukkit.broadcastMessage(message);
                                broadcastSendMessage(true, null, message);
                                break;
                            }

                            case "take": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                Cache.changeall(args[2], "ADMIN_COMMAND", amount, false, com);
                                sender.sendMessage(sendMessage("prefix") + sendMessage("money_take")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = sendMessage("prefix") + args[4];
                                Bukkit.broadcastMessage(message);
                                broadcastSendMessage(true, null, message);

                                break;
                            }

                            default: {
                                sendHelpMessage(sender, 1);
                                break;
                            }

                        }

                        break;
                    }

                    default: {
                        sendHelpMessage(sender, 1);
                        break;
                    }

                }
                break;
            }

            default: {
                sendHelpMessage(sender, 1);
                break;
            }

        }

        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            BigDecimal value = new BigDecimal(s);

            if (value.compareTo(BigDecimal.ONE) >= 0) {
                return !DataFormat.isMAX(DataFormat.formatString(s));
            }

        } catch (NumberFormatException ignored) {
            return false;
        }

        return true;
    }

    public static boolean check() {
        return Bukkit.getOnlinePlayers().isEmpty() & ServerINFO.IsBungeeCordMode;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    @SuppressWarnings("ConstantConditions")
    public static String sendMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', MessagesManager.messageFile.getString(message));
    }

    public static void showVersion(CommandSender sender) {
        sender.sendMessage(sendMessage("prefix") + "§6 XConomy §f(Version: "
                + XConomy.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + MessagesManager.getAuthor());
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(sendMessage("prefix") + "§7 Translator (system): §f" + trs);
        }
    }

    private static void sendHelpMessage(CommandSender sender, Integer num) {
        List<String> helplist = new ArrayList<>();
        helplist.add(sendMessage("help1"));
        helplist.add(sendMessage("help2"));
        helplist.add(sendMessage("help3"));
        helplist.add(sendMessage("help4"));
        if (sender.isOp() | sender.hasPermission("xconomy.admin.give")) {
            helplist.add(sendMessage("help5"));
            helplist.add(sendMessage("help8"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.take")) {
            helplist.add(sendMessage("help6"));
            helplist.add(sendMessage("help9"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.set")) {
            helplist.add(sendMessage("help7"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.balancetop")) {
            helplist.add(sendMessage("help10"));
        }
        Integer maxipages;
        if (helplist.size() % 5 == 0) {
            maxipages = helplist.size() / 5;
        } else {
            maxipages = helplist.size() / 5 + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        sender.sendMessage(sendMessage("help_title_full").replace("%page%", num.toString() + "/" + maxipages.toString()));
        int indexpage = 0;
        while (indexpage < 5) {
            if (helplist.size() > indexpage + (num - 1) * 5) {
                sender.sendMessage(helplist.get(indexpage + (num - 1) * 5));
            }
            indexpage += 1;
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void broadcastSendMessage(boolean ispublic, UUID u, String message) {
        if (!ServerINFO.IsBungeeCordMode) {
            return;
        }
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        if (!ispublic) {
            output.writeUTF("message");
            output.writeUTF(XConomy.getSign());
            output.writeUTF(u.toString());
            output.writeUTF(message);
        } else {
            output.writeUTF("broadcast");
            output.writeUTF(XConomy.getSign());
            output.writeUTF(message);
        }
        Cache.SendMessTask(output, null, null, null, null, null, null,
                null, null);

    }

}
