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

import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.syncdata.SyncMessage;
import me.yic.xconomy.data.syncdata.SyncPermission;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.info.SyncType;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.CompletableFutureTask;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.RGBColor;
import me.yic.xconomy.utils.SendPluginMessage;
import me.yic.xconomy.utils.UUIDMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandHandler {
    private static String PREFIX = translateColorCodes("prefix");

    public static boolean onCommand(CommandSender sender, String commandName, String[] args) {
        switch (commandName) {
            case "xconomy": {
                if (sender.isOp()) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        MessagesManager.loadlangmess();
                        PREFIX = translateColorCodes("prefix");
                        sendMessages(sender, PREFIX + MessagesManager.systemMessage("§amessage.yml重载成功"));
                        return true;
                    }
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender, 1);
                    return true;
                }
                if (args.length == 2 && args[0].equalsIgnoreCase("help")) {
                    if (isDouble(args[1])) {
                        if (Integer.parseInt(args[1]) > 0) {
                            sendHelpMessage(sender, Integer.valueOf(args[1]));
                        } else {
                            sendHelpMessage(sender, 1);
                        }
                    } else {
                        sendHelpMessage(sender, 1);
                    }
                    return true;
                }
                showVersion(sender);
                break;
            }

            case "paypermission": {
                if (sender.isOp() || sender.hasPermission("xconomy.admin.permission")) {
                    if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                        if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                            boolean vv = !args[2].equalsIgnoreCase("false");
                            if (args[1].equalsIgnoreCase("*")) {
                                PermissionINFO.globalpayment = vv;
                                sendMessages(sender, translateColorCodes("global_permissions_change").replace("%permission%", "pay")
                                        .replace("%value%", args[2]));
                                syncpr(1, null, vv);
                            }else{
                                PlayerData pd = DataCon.getPlayerData(args[1]);
                                if (pd == null) {
                                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                                } else {
                                    UUID targetUUID = pd.getUniqueId();
                                    String realname = pd.getName();
                                    PermissionINFO.setPaymentPermission(targetUUID, vv);
                                    sendMessages(sender, translateColorCodes("personal_permissions_change").replace("%permission%", "pay")
                                            .replace("%player%", realname).replace("%value%", args[2]));
                                    syncpr(1, targetUUID, vv);
                                }
                            }
                            return true;
                        }
                    } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                        PlayerData pd = DataCon.getPlayerData(args[1]);
                        UUID targetUUID = pd.getUniqueId();
                        if (targetUUID == null) {
                            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                        } else {
                            String realname = pd.getName();
                            PermissionINFO.setPaymentPermission(targetUUID, null);
                            sendMessages(sender, translateColorCodes("personal_permissions_change").replace("%permission%", "pay")
                                    .replace("%player%", realname).replace("%value%", "Default"));
                            syncpr(1, targetUUID, null);
                        }
                        return true;
                    }
                    sendHelpMessage(sender, 1);
                    return true;
                }
                break;
            }

            case "paytoggle": {
                if (args.length == 0) {
                    if (!(sender instanceof Player)) {
                        sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                        return true;
                    }
                    if (sender.isOp() || sender.hasPermission("xconomy.user.paytoggle")) {
                        PermissionINFO.setRPaymentPermission(((Player) sender).getUniqueId());
                        if (PermissionINFO.getRPaymentPermission(((Player) sender).getUniqueId())) {
                            sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_TRUE));
                            syncpr(2, ((Player) sender).getUniqueId(), true);
                        } else {
                            sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_FALSE));
                            syncpr(2, ((Player) sender).getUniqueId(), false);
                        }
                    }
                    return true;
                } else if (args.length == 1) {
                    if (sender.isOp() || sender.hasPermission("xconomy.admin.paytoggle")) {
                        if (check()) {
                            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }
                        PlayerData pd = DataCon.getPlayerData(args[0]);
                        if (pd == null) {
                            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                        } else {
                            UUID targetUUID = pd.getUniqueId();
                            Player target = DataCon.getplayer(pd);
                            String realname = pd.getName();
                            PermissionINFO.setRPaymentPermission(((Player) sender).getUniqueId());

                            String mess = translateColorCodes(MessageConfig.PAYTOGGLE_TRUE);
                            if (PermissionINFO.getRPaymentPermission(((Player) sender).getUniqueId())) {
                                syncpr(2, targetUUID, true);
                                sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_OTHER_TRUE)
                                        .replace("%player%", realname));
                            } else {
                                syncpr(2, targetUUID, false);
                                sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_OTHER_FALSE)
                                        .replace("%player%", realname));

                                mess = translateColorCodes(MessageConfig.PAYTOGGLE_FALSE);
                            }
                            if (target == null) {
                                broadcastSendMessage(false, pd, mess);
                                return true;
                            }

                            target.sendMessage(mess);
                        }
                        return true;
                    }
                }
                break;
            }

            case "balancetop": {
                if (args.length == 0 || args.length == 1) {

                    if (!(sender.isOp() || sender.hasPermission("xconomy.user.balancetop"))) {
                        sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                        return true;
                    }

                    if (Cache.baltop.isEmpty()) {
                        sendMessages(sender, PREFIX + translateColorCodes("top_nodata"));
                        return true;
                    }

                    if (args.length == 0) {
                        sendRankingMessage(sender, 1);
                    } else {
                        if (isDouble(args[0])) {
                            if (Integer.parseInt(args[0]) > 0) {
                                sendRankingMessage(sender, Integer.valueOf(args[0]));
                            } else {
                                sendRankingMessage(sender, 1);
                            }
                        } else {
                            sendRankingMessage(sender, 1);
                        }
                    }

                    break;
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("hide") || args[0].equalsIgnoreCase("display")) {

                        if (!(sender.isOp() || sender.hasPermission("xconomy.admin.balancetop"))) {
                            sendHelpMessage(sender, 1);
                            return true;
                        }

                        PlayerData pd = DataCon.getPlayerData(args[1]);

                        if (pd == null) {
                            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                            return true;
                        }

                        UUID targetUUID = pd.getUniqueId();
                        if (args[0].equalsIgnoreCase("hide")) {
                            DataLink.setTopBalHide(targetUUID, 1);
                            sendMessages(sender, PREFIX + translateColorCodes("top_hidden").replace("%player%", args[1]));
                        } else if (args[0].equalsIgnoreCase("display")) {
                            DataLink.setTopBalHide(targetUUID, 0);
                            sendMessages(sender, PREFIX + translateColorCodes("top_displayed").replace("%player%", args[1]));
                        }

                        break;
                    }
                }
            }

            case "pay": {
                if (!(sender instanceof Player)) {
                    sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                    return true;
                }

                if (!sender.isOp()) {
                    if (!PermissionINFO.getGlobalPayment()) {
                        sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                        return true;
                    }

                    if (PermissionINFO.getPaymentPermission(((Player) sender).getUniqueId()) == null) {
                        if (!(sender.hasPermission("xconomy.user.pay"))) {
                            sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                            return true;
                        }
                    } else if (!PermissionINFO.getPaymentPermission(((Player) sender).getUniqueId())) {
                        sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                        return true;
                    }
                }

                if (args.length != 2) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (sender.getName().equalsIgnoreCase(args[0])) {
                    sendMessages(sender, PREFIX + translateColorCodes("pay_self"));
                    return true;
                }

                if (!isDouble(args[1])) {
                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                    return true;
                }

                BigDecimal amount = DataFormat.formatString(args[1]);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                    return true;
                }

                BigDecimal taxamount = amount.multiply(XConomy.Config.PAYMENT_TAX);

                //Cache.refreshFromCache(((Player) sender).getUniqueId());

                String amountFormatted = DataFormat.shown(amount);
                String taxamountFormatted = DataFormat.shown(taxamount);
                BigDecimal bal_sender = DataCon.getPlayerData(((Player) sender).getUniqueId()).getBalance();

                if (bal_sender.compareTo(taxamount) < 0) {
                    sendMessages(sender, PREFIX + translateColorCodes("pay_fail")
                            .replace("%amount%", taxamountFormatted));
                    return true;
                }

                PlayerData pd = DataCon.getPlayerData(args[0]);
                if (pd == null) {
                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                    return true;
                }

                Player target = DataCon.getplayer(pd);
                UUID targetUUID = pd.getUniqueId();
                String realname = pd.getName();
                if (PermissionINFO.getRPaymentPermission(targetUUID)) {
                    if (XConomy.foundvaultOfflinePermManager) {
                        if (!CompletableFutureTask.hasreceivepermission(target, targetUUID)) {
                            sendMessages(sender, PREFIX + translateColorCodes("no_receive_permission"));
                            return true;
                        }
                    }
                }else{
                    sendMessages(sender, PREFIX + translateColorCodes("no_receive_permission"));
                    return true;
                }

                //Cache.refreshFromCache(targetUUID);

                BigDecimal bal_target = pd.getBalance();
                if (DataFormat.isMAX(bal_target.add(amount))) {
                    sendMessages(sender, PREFIX + translateColorCodes("over_maxnumber"));
                    return true;
                }

                String com = commandName + " " + args[0] + " " + amount;
                DataCon.changeplayerdata("PLAYER_COMMAND", ((Player) sender).getUniqueId(), taxamount, false, com);
                sendMessages(sender, PREFIX + translateColorCodes("pay")
                        .replace("%player%", realname)
                        .replace("%amount%", amountFormatted));

                DataCon.changeplayerdata("PLAYER_COMMAND", targetUUID, amount, true, com);
                String mess = PREFIX + translateColorCodes("pay_receive")
                        .replace("%player%", sender.getName())
                        .replace("%amount%", amountFormatted);

                if (target == null) {
                    broadcastSendMessage(false, pd, mess);
                    return true;
                }

                target.sendMessage(mess);
                break;
            }

            case "money":
            case "balance":
            case "economy":
            case "eco": {

                int commndlength = args.length;
                StringBuilder reasonmessages = null;
                if (sender.isOp() | sender.hasPermission("xconomy.admin.give")
                        | sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set")) {
                    if (args.length >= 4) {
                        if (args.length == 4) {
                            reasonmessages = new StringBuilder(args[3]);
                        } else {
                            reasonmessages = new StringBuilder();
                            if (isDouble(args[2])) {
                                commndlength = 4;
                                int count = 3;
                                while (count < args.length) {
                                    reasonmessages.append(args[count]).append(" ");
                                    count += 1;
                                }
                            } else {
                                if (args.length == 5) {
                                    reasonmessages = new StringBuilder(args[4]);
                                } else {
                                    commndlength = 5;
                                    int count = 4;
                                    while (count < args.length) {
                                        reasonmessages.append(args[count]).append(" ");
                                        count += 1;
                                    }
                                }
                            }
                        }
                    }
                }

                switch (commndlength) {
                    case 0: {
                        if (!(sender instanceof Player)) {
                            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                            return true;
                        }

                        if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance"))) {
                            sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                            return true;
                        }

                        Player player = (Player) sender;

                        //Cache.refreshFromCache(player.getUniqueId());

                        BigDecimal a = DataCon.getPlayerData(player.getUniqueId()).getBalance();
                        sendMessages(sender, PREFIX + translateColorCodes("balance")
                                .replace("%balance%", DataFormat.shown((a))));

                        break;
                    }

                    case 1: {
                        if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance.other"))) {
                            sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                            return true;
                        }

                        PlayerData pd = DataCon.getPlayerData(args[0]);
                        if (pd == null) {
                            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                            return true;
                        }
                        String realname = pd.getName();

                        BigDecimal targetBalance = pd.getBalance();
                        sendMessages(sender, PREFIX + translateColorCodes("balance_other")
                                .replace("%player%", realname)
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
                            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        if (!isDouble(args[2])) {
                            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(args[2]);
                        String amountFormatted = DataFormat.shown(amount);
                        PlayerData pd = DataCon.getPlayerData(args[1]);

                        if (pd == null) {
                            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                            return true;
                        }

                        Player target = DataCon.getplayer(pd);
                        UUID targetUUID = pd.getUniqueId();
                        String realname = pd.getName();

                        String com = commandName + " " + args[0] + " " + args[1] + " " + amount;
                        if (commndlength == 4) {
                            com += " " + reasonmessages;
                        }
                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                                    return true;
                                }

                                //Cache.refreshFromCache(targetUUID);

                                BigDecimal bal = pd.getBalance();
                                if (DataFormat.isMAX(bal.add(amount))) {
                                    sendMessages(sender, PREFIX + translateColorCodes("over_maxnumber"));
                                    if (target != null) {
                                        sendMessages(target, PREFIX + translateColorCodes("over_maxnumber_receive"));
                                    }
                                    return true;
                                }

                                DataCon.changeplayerdata("ADMIN_COMMAND", targetUUID, amount, true, com);
                                sendMessages(sender, PREFIX + translateColorCodes("money_give")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | commndlength == 4) {

                                    String message = PREFIX + translateColorCodes("money_give_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (commndlength == 4) {
                                        message = PREFIX + reasonmessages;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, pd, message);
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
                                    sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                                    return true;
                                }

                                //Cache.refreshFromCache(targetUUID);
                                BigDecimal bal = pd.getBalance();
                                if (bal.compareTo(amount) < 0) {
                                    sendMessages(sender, PREFIX + translateColorCodes("money_take_fail")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted));

                                    return true;
                                }

                                DataCon.changeplayerdata("ADMIN_COMMAND", targetUUID, amount, false, com);
                                sendMessages(sender, PREFIX + translateColorCodes("money_take")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_take_receive") | commndlength == 4) {
                                    String mess = PREFIX + translateColorCodes("money_take_receive")
                                            .replace("%player%", realname).replace("%amount%", amountFormatted);

                                    if (commndlength == 4) {
                                        mess = PREFIX + reasonmessages;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, pd, mess);
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

                                DataCon.changeplayerdata("ADMIN_COMMAND", targetUUID, amount, null, com);
                                sendMessages(sender, PREFIX + translateColorCodes("money_set")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_set_receive") | commndlength == 4) {
                                    String mess = PREFIX + translateColorCodes("money_set_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (commndlength == 4) {
                                        mess = PREFIX + reasonmessages;
                                    }

                                    if (target == null) {
                                        broadcastSendMessage(false, pd, mess);
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

                        if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE) && args[2].equalsIgnoreCase("online")) {
                            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§c该指令不支持在半正版模式中使用"));
                            return true;
                        }

                        if (check()) {
                            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        if (!isDouble(args[3])) {
                            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(args[3]);

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sendMessages(sender, PREFIX + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        String target = "AllPlayer";
                        if (args[2].equalsIgnoreCase("online")) {
                            target = "OnlinePlayer";
                        }

                        String amountFormatted = DataFormat.shown(amount);

                        String com = commandName + " " + args[0] + " " + args[1] + " " + args[2] + " " + amount + " " + reasonmessages;

                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                DataCon.changeallplayerdata(args[2], "ADMIN_COMMAND", amount, true, com);
                                sendMessages(sender, PREFIX + translateColorCodes("money_give")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = PREFIX + reasonmessages;
                                Bukkit.broadcastMessage(message);
                                broadcastSendMessage(true, null, message);
                                break;
                            }

                            case "take": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                DataCon.changeallplayerdata(args[2], "ADMIN_COMMAND", amount, false, com);
                                sendMessages(sender, PREFIX + translateColorCodes("money_take")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = PREFIX + reasonmessages;
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

    private static boolean isDouble(String s) {
        if (s.matches(".*[a-zA-Z].*")) {
            return false;
        }
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
        return Bukkit.getOnlinePlayers().isEmpty() && XConomy.Config.BUNGEECORD_ENABLE && !XConomy.Config.DISABLE_CACHE;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    public static void sendMessages(Player sender, String name, double amount) {
        sendMessages(sender, PREFIX + translateColorCodes("pay_receive")
                .replace("%player%", name)
                .replace("%amount%", DataFormat.shown(amount)));
    }

    private static void sendMessages(CommandSender sender, String message) {
        if (!message.replace(PREFIX, "").equalsIgnoreCase("")) {
            if (message.contains("\\n")) {
                String[] messs = message.split("\\\\n");
                sender.sendMessage(messs);
            } else {
                sender.sendMessage(message);
            }
        }
    }

    public static String translateColorCodes(MessageConfig message) {
        return ChatColor.translateAlternateColorCodes('&', RGBColor.translateHexColorCodes(MessagesManager.messageFile.getString(message.toString())));
    }

    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', RGBColor.translateHexColorCodes(MessagesManager.messageFile.getString(message)));
    }

    public static void showVersion(CommandSender sender) {
        sender.sendMessage(PREFIX + "§6 XConomy §f(Version: "
                + XConomy.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + MessagesManager.getAuthor());
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(PREFIX + "§7 Translator (system): §f" + trs);
        }
    }

    private static void sendHelpMessage(CommandSender sender, Integer num) {
        List<String> helplist = new ArrayList<>();
        helplist.add(translateColorCodes("help1"));
        helplist.add(translateColorCodes("help2"));
        helplist.add(translateColorCodes("help3"));
        helplist.add(translateColorCodes("help4"));
        if (sender.isOp() | sender.hasPermission("xconomy.admin.give")) {
            helplist.add(translateColorCodes("help5"));
            helplist.add(translateColorCodes("help8"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.take")) {
            helplist.add(translateColorCodes("help6"));
            helplist.add(translateColorCodes("help9"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.set")) {
            helplist.add(translateColorCodes("help7"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.balancetop")) {
            helplist.add(translateColorCodes("help10"));
        }
        helplist.add(translateColorCodes("help11"));
        if (sender.isOp() | sender.hasPermission("xconomy.admin.paytoggle")) {
            helplist.add(translateColorCodes("help12"));
        }
        if (sender.isOp() | sender.hasPermission("xconomy.admin.permission")) {
            helplist.add(translateColorCodes("help13"));
            helplist.add(translateColorCodes("help14"));
        }
        Integer maxipages;
        if (helplist.size() % XConomy.Config.LINES_PER_PAGE == 0) {
            maxipages = helplist.size() / XConomy.Config.LINES_PER_PAGE;
        } else {
            maxipages = helplist.size() / XConomy.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        sendMessages(sender, translateColorCodes("help_title_full").replace("%page%", num + "/" + maxipages));
        int indexpage = 0;
        while (indexpage < XConomy.Config.LINES_PER_PAGE) {
            if (helplist.size() > indexpage + (num - 1) * XConomy.Config.LINES_PER_PAGE) {
                sender.sendMessage(helplist.get(indexpage + (num - 1) * XConomy.Config.LINES_PER_PAGE));
            }
            indexpage += 1;
        }
    }

    private static void sendRankingMessage(CommandSender sender, Integer num) {
        Integer maxipages;
        int listsize = Cache.baltop_papi.size();
        if (listsize % XConomy.Config.LINES_PER_PAGE == 0) {
            maxipages = listsize / XConomy.Config.LINES_PER_PAGE;
        } else {
            maxipages = listsize / XConomy.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        int endindex = num * XConomy.Config.LINES_PER_PAGE;
        if (endindex >= listsize) {
            endindex = listsize;
        }
        List<String> topNames = Cache.baltop_papi.subList(num * XConomy.Config.LINES_PER_PAGE - XConomy.Config.LINES_PER_PAGE, endindex);

        sendMessages(sender, translateColorCodes("top_title").replace("%page%", num + "/" + maxipages));
        sendMessages(sender, translateColorCodes("sum_text")
                .replace("%balance%", DataFormat.shown((Cache.sumbalance))));
        int placement = 0;
        for (String topName : topNames) {
            placement++;
            sendMessages(sender, translateColorCodes("top_text")
                    .replace("%index%", String.valueOf(num * XConomy.Config.LINES_PER_PAGE - XConomy.Config.LINES_PER_PAGE + placement))
                    .replace("%player%", topName)
                    .replace("%balance%", DataFormat.shown((Cache.baltop.get(topName)))));
        }

        sendMessages(sender, translateColorCodes("top_subtitle"));

    }


    public static void broadcastSendMessage(boolean ispublic, PlayerData pd, String message) {
        if (!XConomy.Config.BUNGEECORD_ENABLE) {
            return;
        }

        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            if (!ispublic) {
                if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE) ) {
                    oos.writeObject(new SyncMessage(XConomy.Config.BUNGEECORD_SIGN, SyncType.MESSAGE_SEMI, pd.getName(), message));
                } else {
                    oos.writeObject(new SyncMessage(XConomy.Config.BUNGEECORD_SIGN, SyncType.MESSAGE, pd.getUniqueId(), message));
                }
            } else {
                oos.writeObject(new SyncMessage(XConomy.Config.BUNGEECORD_SIGN, SyncType.BROADCAST, "", message));
            }
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendPluginMessage.SendMessTask("xconomy:acb", output);

    }

    public static void syncpr(int type, UUID u, Boolean value) {
        if (!XConomy.Config.BUNGEECORD_ENABLE) {
            return;
        }

        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            oos.writeObject(new SyncPermission(XConomy.Config.BUNGEECORD_SIGN, u, type, value));
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendPluginMessage.SendMessTask("xconomy:acb", output);

    }

}
