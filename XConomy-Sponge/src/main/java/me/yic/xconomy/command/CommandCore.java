/*
 *  This file (CommandCore.java) is a part of project XConomy
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
package me.yic.xconomy.command;

import me.yic.xconomy.XConomy;
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
import me.yic.xconomy.utils.PlayerData;
import me.yic.xconomy.utils.SendPluginMessage;
import me.yic.xconomy.utils.UUIDMode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandCore {
    private static String PREFIX = translateColorCodes("prefix");

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static CommandResult onCommand(CommandSource sender, String commandName, String[] args) {
        switch (commandName) {
            case "xconomy": {
                if (isOp(sender)) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        MessagesManager.loadlangmess();
                        PREFIX = translateColorCodes("prefix");
                        sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§amessage.yml重载成功")));
                        return CommandResult.success();
                    }
                }
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    sendHelpMessage(sender, 1);
                    return CommandResult.success();
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
                    return CommandResult.success();
                }
                showVersion(sender);
                break;
            }

            case "paypermission": {
                if (isOp(sender) || sender.hasPermission("xconomy.admin.permission")) {
                    if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                        if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                            boolean vv = !args[2].equalsIgnoreCase("false");
                            if (args[1].equalsIgnoreCase("*")) {
                                PermissionINFO.globalpayment = vv;
                                sender.sendMessage(Text.of(translateColorCodes("global_permissions_change").replace("%permission%", "pay")
                                        .replace("%value%", args[2])));
                                syncpr(1, null, vv);
                            }else{
                                PlayerData pd = DataCon.getPlayerData(args[1]);
                                if (pd == null) {
                                    sender.sendMessages(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                                } else {
                                    UUID targetUUID = pd.getUniqueId();
                                    String realname = pd.getName();
                                    PermissionINFO.setPaymentPermission(targetUUID, vv);
                                    sender.sendMessage(Text.of(translateColorCodes("personal_permissions_change").replace("%permission%", "pay")
                                            .replace("%player%", realname).replace("%value%", args[2])));
                                    syncpr(1, targetUUID, vv);
                                }
                            }
                            return CommandResult.success();
                        }
                    } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                        PlayerData pd = DataCon.getPlayerData(args[1]);
                        if (pd == null) {
                            sender.sendMessages(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                        } else {
                            UUID targetUUID = pd.getUniqueId();
                            String realname = pd.getName();
                            PermissionINFO.setPaymentPermission(targetUUID, null);
                            sender.sendMessage(Text.of(translateColorCodes("personal_permissions_change").replace("%permission%", "pay")
                                    .replace("%player%", realname).replace("%value%", "Default")));
                            syncpr(1, targetUUID, null);
                        }
                        return CommandResult.success();
                    }
                    sendHelpMessage(sender, 1);
                    return CommandResult.success();
                }
                break;
            }

            case "paytoggle": {
                if (args.length == 0) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令")));
                        return CommandResult.success();
                    }
                    if (isOp(sender) || sender.hasPermission("xconomy.user.paytoggle")) {
                        PermissionINFO.setRPaymentPermission(((Player) sender).getUniqueId());
                        if (PermissionINFO.getRPaymentPermission(((Player) sender).getUniqueId())) {
                            sender.sendMessages(Text.of(translateColorCodes(MessageConfig.PAYTOGGLE_TRUE)));
                            syncpr(2, ((Player) sender).getUniqueId(), true);
                        } else {
                            sender.sendMessages(Text.of(translateColorCodes(MessageConfig.PAYTOGGLE_FALSE)));
                            syncpr(2, ((Player) sender).getUniqueId(), false);
                        }
                    }
                    return CommandResult.success();
                } else if (args.length == 1) {
                    if (isOp(sender)  || sender.hasPermission("xconomy.admin.paytoggle")) {
                        if (check()) {
                            sender.sendMessages(Text.of(PREFIX + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令")));
                            return CommandResult.success();
                        }
                        PlayerData pd = DataCon.getPlayerData(args[0]);
                        if (pd == null) {
                            sender.sendMessages(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                        } else {
                            UUID targetUUID = pd.getUniqueId();
                            User target = DataLink.getplayer(pd);
                            String realname = pd.getName();
                            PermissionINFO.setRPaymentPermission(((Player) sender).getUniqueId());

                            String mess = translateColorCodes(MessageConfig.PAYTOGGLE_TRUE);
                            if (PermissionINFO.getRPaymentPermission(((Player) sender).getUniqueId())) {
                                syncpr(2, targetUUID, true);
                                sender.sendMessages(Text.of(translateColorCodes(MessageConfig.PAYTOGGLE_OTHER_TRUE)
                                        .replace("%player%", realname)));
                            } else {
                                syncpr(2, targetUUID, false);
                                sender.sendMessages(Text.of(translateColorCodes(MessageConfig.PAYTOGGLE_OTHER_FALSE)
                                        .replace("%player%", realname)));

                                mess = translateColorCodes(MessageConfig.PAYTOGGLE_FALSE);
                            }
                            if (target == null) {
                                broadcastSendMessage(false, targetUUID, mess);
                                return CommandResult.success();
                            }

                            target.getPlayer().get().sendMessage(Text.of(mess));
                        }
                        return CommandResult.success();
                    }
                }
                break;
            }

            case "balancetop": {
                if (args.length == 0 || args.length == 1) {

                    if (!(isOp(sender) || sender.hasPermission("xconomy.user.balancetop"))) {
                        sender.sendMessage(Text.of(PREFIX + translateColorCodes("no_permission")));
                        return CommandResult.success();
                    }

                    if (Cache.baltop.isEmpty()) {
                        sender.sendMessage(Text.of(PREFIX + translateColorCodes("top_nodata")));
                        return CommandResult.success();
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

                        if (!(isOp(sender) || sender.hasPermission("xconomy.admin.balancetop"))) {
                            sendHelpMessage(sender, 1);
                            return CommandResult.success();
                        }

                        PlayerData pd = DataCon.getPlayerData(args[1]);

                        if (pd == null) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                            return CommandResult.success();
                        }

                        UUID targetUUID = pd.getUniqueId();
                        if (args[0].equalsIgnoreCase("hide")) {
                            DataLink.setTopBalHide(targetUUID, 1);
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("top_hidden").replace("%player%", args[1])));
                        } else if (args[0].equalsIgnoreCase("display")) {
                            DataLink.setTopBalHide(targetUUID, 0);
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("top_displayed").replace("%player%", args[1])));
                        }

                        break;
                    }
                }
            }

            case "pay": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令")));
                    return CommandResult.success();
                }

                if (!isOp(sender)) {
                    if (!PermissionINFO.getGlobalPayment()) {
                        sender.sendMessage(Text.of(PREFIX + translateColorCodes("no_permission")));
                        return CommandResult.success();
                    }
                    if (PermissionINFO.getPaymentPermission(((Player) sender).getUniqueId()) == null) {
                        if (!(sender.hasPermission("xconomy.user.pay"))) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("no_permission")));
                            return CommandResult.success();
                        }
                    } else if (!PermissionINFO.getPaymentPermission(((Player) sender).getUniqueId())) {
                        sender.sendMessage(Text.of(sender, PREFIX + translateColorCodes("no_permission")));
                        return CommandResult.success();
                    }
                }

                if (args.length != 2) {
                    sendHelpMessage(sender, 1);
                    return CommandResult.success();
                }

                if (sender.getName().equalsIgnoreCase(args[0])) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("pay_self")));
                    return CommandResult.success();
                }

                if (!isDouble(args[1])) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                    return CommandResult.success();
                }

                BigDecimal amount = DataFormat.formatString(args[1]);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                    return CommandResult.success();
                }

                BigDecimal taxamount = amount.multiply(XConomy.Config.PAYMENT_TAX);

                String amountFormatted = DataFormat.shown(amount);
                String taxamountFormatted = DataFormat.shown(taxamount);
                BigDecimal bal_sender = DataCon.getPlayerData(((Player) sender).getUniqueId()).getBalance();

                if (bal_sender.compareTo(taxamount) < 0) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("pay_fail")
                            .replace("%amount%", taxamountFormatted)));
                    return CommandResult.success();
                }

                PlayerData pd = DataCon.getPlayerData(args[0]);
                if (pd == null) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                    return CommandResult.success();
                }
                User target = DataLink.getplayer(pd);
                UUID targetUUID = pd.getUniqueId();
                String realname = pd.getName();

                if (target !=null && !target.hasPermission("xconomy.user.pay.receive")) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("no_receive_permission")));
                    return CommandResult.success();
                }

                BigDecimal bal_target = pd.getBalance();
                if (DataFormat.isMAX(bal_target.add(amount))) {
                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("over_maxnumber")));
                    return CommandResult.success();
                }

                String com = commandName + " " + args[0] + " " + amount;
                DataCon.changeplayerdata("PLAYER_COMMAND", ((Player) sender).getUniqueId(), taxamount, false, com, null);
                sender.sendMessage(Text.of(PREFIX + translateColorCodes("pay")
                        .replace("%player%", realname)
                        .replace("%amount%", amountFormatted)));

                DataCon.changeplayerdata("PLAYER_COMMAND", targetUUID, amount, true, com, null);
                String mess = PREFIX + translateColorCodes("pay_receive")
                        .replace("%player%", sender.getName())
                        .replace("%amount%", amountFormatted);

                if (target == null || !target.isOnline()) {
                    broadcastSendMessage(false, targetUUID, mess);
                    return CommandResult.success();
                }

                target.getPlayer().get().sendMessage(Text.of(mess));
                break;
            }

            case "money":
            case "balance":
            case "economy":
            case "eco": {

                switch (args.length) {
                    case 0: {
                        if (!(sender instanceof Player)) {
                            sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令")));
                            return CommandResult.success();
                        }

                        if (!(isOp(sender) || sender.hasPermission("xconomy.user.balance"))) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("no_permission")));
                            return CommandResult.success();
                        }

                        Player player = (Player) sender;

                        //Cache.refreshFromCache(player.getUniqueId());

                        BigDecimal a = DataCon.getPlayerData(player.getUniqueId()).getBalance();
                        sender.sendMessage(Text.of(PREFIX + translateColorCodes("balance")
                                .replace("%balance%", DataFormat.shown((a)))));

                        break;
                    }

                    case 1: {
                        if (!(isOp(sender) || sender.hasPermission("xconomy.user.balance.other"))) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("no_permission")));
                            return CommandResult.success();
                        }

                        PlayerData pd = DataCon.getPlayerData(args[0]);
                        if (pd == null) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                            return CommandResult.success();
                        }
                        String realname = pd.getName();

                        BigDecimal targetBalance = pd.getBalance();
                        sender.sendMessage(Text.of(PREFIX + translateColorCodes("balance_other")
                                .replace("%player%", realname)
                                .replace("%balance%", DataFormat.shown((targetBalance)))));

                        break;
                    }

                    case 3:
                    case 4: {
                        if (!(isOp(sender) | sender.hasPermission("xconomy.admin.give")
                                | sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set"))) {
                            sendHelpMessage(sender, 1);
                            return CommandResult.success();
                        }

                        if (check()) {
                            sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令")));
                            return CommandResult.success();
                        }

                        if (!isDouble(args[2])) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                            return CommandResult.success();
                        }

                        BigDecimal amount = DataFormat.formatString(args[2]);
                        String amountFormatted = DataFormat.shown(amount);
                        PlayerData pd = DataCon.getPlayerData(args[1]);
                        if (pd == null) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT)));
                            return CommandResult.success();
                        }

                        User target = DataLink.getplayer(pd);
                        UUID targetUUID = pd.getUniqueId();
                        String realname = pd.getName();

                        String com = commandName + " " + args[0] + " " + args[1] + " " + amount;
                        StringBuilder reason = null;
                        if (args.length == 4) {
                            reason = new StringBuilder(args[3]);
                            com += " " + reason;
                        }

                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                                    return CommandResult.success();
                                }

                                BigDecimal bal = pd.getBalance();
                                if (DataFormat.isMAX(bal.add(amount))) {
                                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("over_maxnumber")));
                                    if (target != null && target.isOnline()) {
                                        target.getPlayer().get().sendMessage(Text.of(PREFIX + translateColorCodes("over_maxnumber_receive")));
                                    }
                                    return CommandResult.success();
                                }

                                DataCon.changeplayerdata("ADMIN_COMMAND", targetUUID, amount, true, com, reason);
                                sender.sendMessage(Text.of(PREFIX + translateColorCodes("money_give")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted)));

                                if (checkMessage("money_give_receive") | args.length == 4) {

                                    String message = PREFIX + translateColorCodes("money_give_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        message = PREFIX + reason;
                                    }

                                    if (target == null || !target.isOnline()) {
                                        broadcastSendMessage(false, targetUUID, message);
                                        return CommandResult.success();
                                    }

                                    target.getPlayer().get().sendMessage(Text.of(message));

                                }
                                break;
                            }

                            case "take": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                                    return CommandResult.success();
                                }

                                BigDecimal bal = pd.getBalance();
                                if (bal.compareTo(amount) < 0) {
                                    sender.sendMessage(Text.of(PREFIX + translateColorCodes("money_take_fail")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted)));

                                    return CommandResult.success();
                                }

                                DataCon.changeplayerdata("ADMIN_COMMAND", targetUUID, amount, false, com, reason);
                                sender.sendMessage(Text.of(PREFIX + translateColorCodes("money_take")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted)));

                                if (checkMessage("money_take_receive") | args.length == 4) {
                                    String mess = PREFIX + translateColorCodes("money_take_receive")
                                            .replace("%player%", realname).replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        mess = PREFIX + reason;
                                    }

                                    if (target == null || !target.isOnline()) {
                                        broadcastSendMessage(false, targetUUID, mess);
                                        return CommandResult.success();
                                    }

                                    target.getPlayer().get().sendMessage(Text.of(mess));

                                }
                                break;
                            }

                            case "set": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.set"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                DataCon.changeplayerdata("ADMIN_COMMAND", targetUUID, amount, null, com, reason);
                                sender.sendMessage(Text.of(PREFIX + translateColorCodes("money_set")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted)));

                                if (checkMessage("money_set_receive") | args.length == 4) {
                                    String mess = PREFIX + translateColorCodes("money_set_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        mess = PREFIX + reason;
                                    }

                                    if (target == null || !target.isOnline()) {
                                        broadcastSendMessage(false, targetUUID, mess);
                                        return CommandResult.success();
                                    }

                                    target.getPlayer().get().sendMessage(Text.of(mess));

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
                        if (!(isOp(sender) | sender.hasPermission("xconomy.admin.give")
                                | sender.hasPermission("xconomy.admin.take") | sender.hasPermission("xconomy.admin.set"))) {
                            sendHelpMessage(sender, 1);
                            return CommandResult.success();
                        }

                        if (!args[1].equals("*")) {
                            sendHelpMessage(sender, 1);
                            return CommandResult.success();
                        }

                        if (!(args[2].equalsIgnoreCase("all") | args[2].equalsIgnoreCase("online"))) {
                            sendHelpMessage(sender, 1);
                            return CommandResult.success();
                        }

                        if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE) && args[2].equalsIgnoreCase("online")) {
                            sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§c该指令不支持在半正版模式中使用")));
                            return CommandResult.success();
                        }

                        if (check()) {
                            sender.sendMessage(Text.of(PREFIX + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令")));
                            return CommandResult.success();
                        }

                        if (!isDouble(args[3])) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                            return CommandResult.success();
                        }

                        BigDecimal amount = DataFormat.formatString(args[3]);

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sender.sendMessage(Text.of(PREFIX + translateColorCodes("invalid_amount")));
                            return CommandResult.success();
                        }

                        String target = "AllPlayer";
                        if (args[2].equalsIgnoreCase("online")) {
                            target = "OnlinePlayer";
                        }

                        String amountFormatted = DataFormat.shown(amount);

                        String com = commandName + " " + args[0] + " " + args[1] + " " + args[2] + " " + amount + " " + args[4];

                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                DataCon.changeallplayerdata(args[2], "ADMIN_COMMAND", amount, true, com, new StringBuilder(args[4]));
                                sender.sendMessage(Text.of(PREFIX + translateColorCodes("money_give")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted)));

                                String message = PREFIX + args[4];
                                Sponge.getServer().getBroadcastChannel().send(Text.of(message));
                                broadcastSendMessage(true, null, message);
                                break;
                            }

                            case "take": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                DataCon.changeallplayerdata(args[2], "ADMIN_COMMAND", amount, false, com, new StringBuilder(args[4]));
                                sender.sendMessage(Text.of(PREFIX + translateColorCodes("money_take")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted)));

                                String message = PREFIX + args[4];
                                Sponge.getServer().getBroadcastChannel().send(Text.of(message));
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

        return CommandResult.success();
    }


    public static boolean isOp(CommandSource p) {
        return p.hasPermission("xconomy.op");
    }

    @SuppressWarnings("booleanMethodIsAlwaysInverted")
    public static boolean isDouble(String s) {
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
        return Sponge.getServer().getOnlinePlayers().isEmpty() && XConomy.Config.BUNGEECORD_ENABLE && !XConomy.Config.DISABLE_CACHE;
    }

    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    public static void sendMessages(Player sender, String name, double amount) {
        String mess = PREFIX + translateColorCodes("pay_receive")
                .replace("%player%", name)
                .replace("%amount%", DataFormat.shown(amount));
        sender.sendMessage(Text.of(mess));
    }

    public static String translateColorCodes(String message) {
        return MessagesManager.messageFile.getString(message).replace("&", "§");
    }

    public static String translateColorCodes(MessageConfig message) {
        return MessagesManager.messageFile.getString(message.toString()).replace("&", "§");
    }


    public static void showVersion(CommandSource sender) {
        sender.sendMessage(Text.of(PREFIX + "§6 XConomy §f(Version: "
                + XConomy.PVersion + ") §6|§7 Author: §f" + MessagesManager.getAuthor()));
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(Text.of(PREFIX + "§7 Translator (system): §f" + trs));
        }
    }

    public static void sendHelpMessage(CommandSource sender, Integer num) {
        List<String> helplist = new ArrayList<>();
        helplist.add(translateColorCodes("help1"));
        helplist.add(translateColorCodes("help2"));
        helplist.add(translateColorCodes("help3"));
        helplist.add(translateColorCodes("help4"));
        if (isOp(sender) | sender.hasPermission("xconomy.admin.give")) {
            helplist.add(translateColorCodes("help5"));
            helplist.add(translateColorCodes("help8"));
        }
        if (isOp(sender) | sender.hasPermission("xconomy.admin.take")) {
            helplist.add(translateColorCodes("help6"));
            helplist.add(translateColorCodes("help9"));
        }
        if (isOp(sender) | sender.hasPermission("xconomy.admin.set")) {
            helplist.add(translateColorCodes("help7"));
        }
        if (isOp(sender) | sender.hasPermission("xconomy.admin.balancetop")) {
            helplist.add(translateColorCodes("help10"));
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
        sender.sendMessage(Text.of(translateColorCodes("help_title_full").replace("%page%", num + "/" + maxipages)));
        int indexpage = 0;
        while (indexpage < XConomy.Config.LINES_PER_PAGE) {
            if (helplist.size() > indexpage + (num - 1) * XConomy.Config.LINES_PER_PAGE) {
                sender.sendMessage(Text.of(helplist.get(indexpage + (num - 1) * XConomy.Config.LINES_PER_PAGE)));
            }
            indexpage += 1;
        }
    }

    private static void sendRankingMessage(CommandSource sender, Integer num) {
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

        sender.sendMessage(Text.of(translateColorCodes("top_title").replace("%page%", num + "/" + maxipages)));
        sender.sendMessage(Text.of(translateColorCodes("sum_text")
                .replace("%balance%", DataFormat.shown((Cache.sumbalance)))));
        int placement = 0;
        for (String topName : topNames) {
            placement++;
            sender.sendMessage(Text.of(translateColorCodes("top_text")
                    .replace("%index%", String.valueOf(num * XConomy.Config.LINES_PER_PAGE - XConomy.Config.LINES_PER_PAGE + placement))
                    .replace("%player%", topName)
                    .replace("%balance%", DataFormat.shown((Cache.baltop.get(topName))))));
        }

        if (checkMessage("top_subtitle"))
            sender.sendMessage(Text.of(translateColorCodes("top_subtitle")));

    }


    public static void broadcastSendMessage(boolean ispublic, UUID u, String message) {
        if (!XConomy.Config.BUNGEECORD_ENABLE) {
            return;
        }

        if (Sponge.getServer().getOnlinePlayers().isEmpty()) {
            return;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeUTF(XConomy.syncversion);
            if (!ispublic) {
                if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                    oos.writeObject(new SyncMessage(XConomy.Config.BUNGEECORD_SIGN, SyncType.MESSAGE_SEMI, u, message));
                } else {
                    oos.writeObject(new SyncMessage(XConomy.Config.BUNGEECORD_SIGN, SyncType.MESSAGE, u, message));
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

        if (Sponge.getServer().getOnlinePlayers().isEmpty()) {
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
