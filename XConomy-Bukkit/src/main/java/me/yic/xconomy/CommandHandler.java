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
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.CompletableFutureTask;
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.RGBColor;
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
                        MessagesManager.load();
                        sendMessages(sender, translateColorCodes("prefix") + MessagesManager.systemMessage("§amessage.yml重载成功"));
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

                if (sender.isOp() || sender.hasPermission("xconomy.admin.permission")) {
                    if (args.length >= 4 && args[0].equalsIgnoreCase("permission")) {
                        if (args.length == 5 && args[1].equalsIgnoreCase("set")) {
                            if (args[2].equalsIgnoreCase("pay")) {
                                if (args[4].equalsIgnoreCase("true") || args[4].equalsIgnoreCase("false")) {
                                    boolean vv = !args[4].equalsIgnoreCase("false");
                                    if (args[3].equalsIgnoreCase("*")) {
                                        PermissionINFO.globalpayment = vv;
                                        sendMessages(sender, translateColorCodes("global_permissions_change").replace("%permission%", "pay")
                                                .replace("%value%", args[4]));
                                    } else {
                                        PlayerData pd = DataCon.getPlayerData(args[3]);
                                        UUID targetUUID = pd.getUniqueId();
                                        if (targetUUID == null) {
                                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_account"));
                                        } else {
                                            String realname = pd.getName();
                                            PermissionINFO.setPaymentPermission(targetUUID, vv);
                                            sendMessages(sender, translateColorCodes("personal_permissions_change").replace("%permission%", "pay")
                                                    .replace("%player%", realname).replace("%value%", args[4]));
                                        }
                                    }
                                    return true;
                                }

                            }
                        } else if (args.length == 4 && args[1].equalsIgnoreCase("remove")) {
                            if (args[2].equalsIgnoreCase("pay")) {
                                PlayerData pd = DataCon.getPlayerData(args[3]);
                                UUID targetUUID = pd.getUniqueId();
                                if (targetUUID == null) {
                                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_account"));
                                } else {
                                    String realname = pd.getName();
                                    PermissionINFO.setPaymentPermission(targetUUID, null);
                                    sendMessages(sender, translateColorCodes("personal_permissions_change").replace("%permission%", "pay")
                                            .replace("%player%", realname).replace("%value%", "Default"));
                                }
                                return true;
                            }
                        }
                        sendHelpMessage(sender, 1);
                        return true;
                    }
                }
                showVersion(sender);
                break;
            }

            case "balancetop": {
                if (args.length == 0 || args.length == 1) {

                    if (!(sender.isOp() || sender.hasPermission("xconomy.user.balancetop"))) {
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_permission"));
                        return true;
                    }

                    if (Cache.baltop.isEmpty()) {
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("top_nodata"));
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
                        UUID targetUUID = pd.getUniqueId();

                        if (targetUUID == null) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_account"));
                            return true;
                        }

                        if (args[0].equalsIgnoreCase("hide")) {
                            DataLink.setTopBalHide(targetUUID, 1);
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("top_hidden").replace("%player%", args[1]));
                        } else if (args[0].equalsIgnoreCase("display")) {
                            DataLink.setTopBalHide(targetUUID, 0);
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("top_displayed").replace("%player%", args[1]));
                        }

                        break;
                    }
                }
            }

            case "pay": {
                if (!(sender instanceof Player)) {
                    sendMessages(sender, translateColorCodes("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                    return true;
                }

                if (!sender.isOp()) {
                    if (!PermissionINFO.getGlobalPayment()) {
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_permission"));
                        return true;
                    }

                    if (PermissionINFO.getPaymentPermission(((Player) sender).getUniqueId()) == null) {
                        if (!(sender.hasPermission("xconomy.user.pay"))) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_permission"));
                            return true;
                        }
                    } else if (!PermissionINFO.getPaymentPermission(((Player) sender).getUniqueId())) {
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_permission"));
                        return true;
                    }
                }

                if (args.length != 2) {
                    sendHelpMessage(sender, 1);
                    return true;
                }

                if (sender.getName().equalsIgnoreCase(args[0])) {
                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("pay_self"));
                    return true;
                }

                if (!isDouble(args[1])) {
                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
                    return true;
                }

                BigDecimal amount = DataFormat.formatString(args[1]);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
                    return true;
                }

                BigDecimal taxamount = amount.multiply(XConomy.Config.PAYMENT_TAX);

                //Cache.refreshFromCache(((Player) sender).getUniqueId());

                String amountFormatted = DataFormat.shown(amount);
                String taxamountFormatted = DataFormat.shown(taxamount);
                BigDecimal bal_sender = DataCon.getPlayerData(((Player) sender).getUniqueId()).getbalance();

                if (bal_sender.compareTo(taxamount) < 0) {
                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("pay_fail")
                            .replace("%amount%", taxamountFormatted));
                    return true;
                }

                Player target = DataCon.getplayer(args[0]);
                PlayerData pd = DataCon.getPlayerData(args[0]);
                UUID targetUUID = pd.getUniqueId();
                if (targetUUID == null) {
                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_account"));
                    return true;
                }

                String realname = pd.getName();
                if (XConomy.foundvaultOfflinePermManager) {
                    if (!CompletableFutureTask.hasreceivepermission(target, targetUUID)) {
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_receive_permission"));
                        return true;
                    }
                }

                //Cache.refreshFromCache(targetUUID);

                BigDecimal bal_target = DataCon.getPlayerData(targetUUID).getbalance();
                if (DataFormat.isMAX(bal_target.add(amount))) {
                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("over_maxnumber"));
                    return true;
                }

                String com = commandName + " " + args[0] + " " + amount;
                DataCon.change("PLAYER_COMMAND", ((Player) sender).getUniqueId(), taxamount, false, com);
                sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("pay")
                        .replace("%player%", realname)
                        .replace("%amount%", amountFormatted));

                DataCon.change("PLAYER_COMMAND", targetUUID, amount, true, com);
                String mess = translateColorCodes("prefix") + translateColorCodes("pay_receive")
                        .replace("%player%", sender.getName())
                        .replace("%amount%", amountFormatted);

                if (target == null) {
                    broadcastSendMessage(false, targetUUID, mess);
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
                            sendMessages(sender, translateColorCodes("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                            return true;
                        }

                        if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance"))) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_permission"));
                            return true;
                        }

                        Player player = (Player) sender;

                        //Cache.refreshFromCache(player.getUniqueId());

                        BigDecimal a = DataCon.getPlayerData(player.getUniqueId()).getbalance();
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("balance")
                                .replace("%balance%", DataFormat.shown((a))));

                        break;
                    }

                    case 1: {
                        if (!(sender.isOp() || sender.hasPermission("xconomy.user.balance.other"))) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_permission"));
                            return true;
                        }

                        PlayerData pd = DataCon.getPlayerData(args[0]);
                        UUID targetUUID = pd.getUniqueId();
                        if (targetUUID == null) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_account"));
                            return true;
                        }
                        String realname = pd.getName();

                        BigDecimal targetBalance = DataCon.getPlayerData(targetUUID).getbalance();
                        sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("balance_other")
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
                            sendMessages(sender, translateColorCodes("prefix") + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        if (!isDouble(args[2])) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(args[2]);
                        String amountFormatted = DataFormat.shown(amount);
                        Player target = DataCon.getplayer(args[1]);
                        PlayerData pd = DataCon.getPlayerData(args[1]);
                        UUID targetUUID = pd.getUniqueId();

                        if (targetUUID == null) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("no_account"));
                            return true;
                        }

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
                                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
                                    return true;
                                }

                                //Cache.refreshFromCache(targetUUID);

                                BigDecimal bal = DataCon.getPlayerData(targetUUID).getbalance();
                                if (DataFormat.isMAX(bal.add(amount))) {
                                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("over_maxnumber"));
                                    return true;
                                }

                                DataCon.change("ADMIN_COMMAND", targetUUID, amount, true, com);
                                sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("money_give")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | commndlength == 4) {

                                    String message = translateColorCodes("prefix") + translateColorCodes("money_give_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (commndlength == 4) {
                                        message = translateColorCodes("prefix") + reasonmessages;
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
                                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
                                    return true;
                                }

                                //Cache.refreshFromCache(targetUUID);
                                BigDecimal bal = DataCon.getPlayerData(targetUUID).getbalance();
                                if (bal.compareTo(amount) < 0) {
                                    sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("money_take_fail")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted));

                                    return true;
                                }

                                DataCon.change("ADMIN_COMMAND", targetUUID, amount, false, com);
                                sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("money_take")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | commndlength == 4) {
                                    String mess = translateColorCodes("prefix") + translateColorCodes("money_take_receive")
                                            .replace("%player%", realname).replace("%amount%", amountFormatted);

                                    if (commndlength == 4) {
                                        mess = translateColorCodes("prefix") + reasonmessages;
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

                                DataCon.change("ADMIN_COMMAND", targetUUID, amount, null, com);
                                sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("money_set")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted));

                                if (checkMessage("money_give_receive") | commndlength == 4) {
                                    String mess = translateColorCodes("prefix") + translateColorCodes("money_set_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (commndlength == 4) {
                                        mess = translateColorCodes("prefix") + reasonmessages;
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

                        if (XConomy.Config.IS_SEMIONLINEMODE && args[2].equalsIgnoreCase("online")) {
                            sendMessages(sender, translateColorCodes("prefix") + MessagesManager.systemMessage("§c该指令不支持在半正版模式中使用"));
                            return true;
                        }

                        if (check()) {
                            sendMessages(sender, translateColorCodes("prefix") + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        if (!isDouble(args[3])) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
                            return true;
                        }

                        BigDecimal amount = DataFormat.formatString(args[3]);

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("invalid_amount"));
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

                                DataCon.changeall(args[2], "ADMIN_COMMAND", amount, true, com);
                                sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("money_give")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = translateColorCodes("prefix") + reasonmessages;
                                Bukkit.broadcastMessage(message);
                                broadcastSendMessage(true, null, message);
                                break;
                            }

                            case "take": {
                                if (!(sender.isOp() | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return true;
                                }

                                DataCon.changeall(args[2], "ADMIN_COMMAND", amount, false, com);
                                sendMessages(sender, translateColorCodes("prefix") + translateColorCodes("money_take")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted));

                                String message = translateColorCodes("prefix") + reasonmessages;
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
        if (s.matches(".*[a-zA-Z].*")){
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

    private static void sendMessages(CommandSender sender, String message) {
       if (message.contains("\\n")){
           String[] messs = message.split("\\\\n");
           sender.sendMessage(messs);
        }else{
           sender.sendMessage(message);
       }
    }

    public static String translateColorCodes(String message) {
        return ChatColor.translateAlternateColorCodes('&', RGBColor.translateHexColorCodes(MessagesManager.messageFile.getString(message)));
    }

    public static void showVersion(CommandSender sender) {
        sender.sendMessage(translateColorCodes("prefix") + "§6 XConomy §f(Version: "
                + XConomy.getInstance().getDescription().getVersion() + ") §6|§7 Author: §f" + MessagesManager.getAuthor());
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(translateColorCodes("prefix") + "§7 Translator (system): §f" + trs);
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
        Integer maxipages;
        if (helplist.size() % XConomy.Config.LINES_PER_PAGE == 0) {
            maxipages = helplist.size() /  XConomy.Config.LINES_PER_PAGE;
        } else {
            maxipages = helplist.size() /  XConomy.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        sendMessages(sender, translateColorCodes("help_title_full").replace("%page%", num + "/" + maxipages));
        int indexpage = 0;
        while (indexpage <  XConomy.Config.LINES_PER_PAGE) {
            if (helplist.size() > indexpage + (num - 1) *  XConomy.Config.LINES_PER_PAGE) {
                sender.sendMessage(helplist.get(indexpage + (num - 1) *  XConomy.Config.LINES_PER_PAGE));
            }
            indexpage += 1;
        }
    }

    private static void sendRankingMessage(CommandSender sender, Integer num) {
        Integer maxipages;
        int listsize = Cache.baltop_papi.size();
        if (listsize %  XConomy.Config.LINES_PER_PAGE == 0) {
            maxipages = listsize /  XConomy.Config.LINES_PER_PAGE;
        } else {
            maxipages = listsize /  XConomy.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        int endindex = num *  XConomy.Config.LINES_PER_PAGE;
        if (endindex >= listsize) {
            endindex = listsize;
        }
        List<String> topNames = Cache.baltop_papi.subList(num *  XConomy.Config.LINES_PER_PAGE -  XConomy.Config.LINES_PER_PAGE, endindex);

        sendMessages(sender, translateColorCodes("top_title").replace("%page%", num + "/" + maxipages));
        sendMessages(sender, translateColorCodes("sum_text")
                .replace("%balance%", DataFormat.shown((Cache.sumbalance))));
        int placement = 0;
        for (String topName : topNames) {
            placement++;
            sendMessages(sender, translateColorCodes("top_text")
                    .replace("%index%", String.valueOf(num *  XConomy.Config.LINES_PER_PAGE -  XConomy.Config.LINES_PER_PAGE + placement))
                    .replace("%player%", topName)
                    .replace("%balance%", DataFormat.shown((Cache.baltop.get(topName)))));
        }

        if (checkMessage("top_subtitle"))
            sendMessages(sender, translateColorCodes("top_subtitle"));

    }

    @SuppressWarnings("UnstableApiUsage")
    public static void broadcastSendMessage(boolean ispublic, UUID u, String message) {
        if (!XConomy.Config.BUNGEECORD_ENABLE) {
            return;
        }

        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF(XConomy.Config.BUNGEECORD_SIGN);
        output.writeUTF(XConomy.syncversion);
        if (!ispublic) {
            if (XConomy.Config.IS_SEMIONLINEMODE){
                output.writeUTF("message#semi");
            }else {
                output.writeUTF("message");
            }
                output.writeUTF(u.toString());
                output.writeUTF(message);
        } else {
            output.writeUTF("broadcast");
            output.writeUTF(message);
        }
        Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(XConomy.getInstance(),
                "xconomy:acb", output.toByteArray());

    }

}
