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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.utils.PluginINFO;
import me.yic.xconomy.utils.ServerINFO;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandCore {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static CommandResult onCommand(CommandSource sender, String commandName, String[] args) {
        switch (commandName) {
            case "xconomy": {
                if (isOp(sender)) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        XConomy.getInstance().reloadMessages();
                        sender.sendMessage(Text.of(sendMessage("prefix") + MessagesManager.systemMessage("§amessage.yml重载成功")));
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

            case "balancetop": {
                if (args.length == 0 || args.length == 1) {

                    if (!(isOp(sender) || sender.hasPermission("xconomy.user.balancetop"))) {
                        sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_permission")));
                        return CommandResult.success();
                    }

                    if (Cache.baltop.isEmpty()) {
                        sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("top_nodata")));
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

                        UUID targetUUID = Cache.translateUUID(args[1], null);

                        if (targetUUID == null) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_account")));
                            return CommandResult.success();
                        }

                        if (args[0].equalsIgnoreCase("hide")) {
                            DataCon.setTopBalHide(targetUUID, 1);
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("top_hidden").replace("%player%", args[1])));
                        } else if (args[0].equalsIgnoreCase("display")) {
                            DataCon.setTopBalHide(targetUUID, 0);
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("top_displayed").replace("%player%", args[1])));
                        }

                        break;
                    }
                }
            }

            case "pay": {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令")));
                    return CommandResult.success();
                }

                if (!(isOp(sender) || sender.hasPermission("xconomy.user.pay"))) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_permission")));
                    return CommandResult.success();
                }

                if (args.length != 2) {
                    sendHelpMessage(sender, 1);
                    return CommandResult.success();
                }

                if (sender.getName().equalsIgnoreCase(args[0])) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("pay_self")));
                    return CommandResult.success();
                }

                if (!isDouble(args[1])) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
                    return CommandResult.success();
                }

                BigDecimal amount = DataFormat.formatString(args[1]);

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
                    return CommandResult.success();
                }

                String amountFormatted = DataFormat.shown(amount);
                BigDecimal bal_sender = Cache.getBalanceFromCacheOrDB(((Player) sender).getUniqueId());

                if (bal_sender.compareTo(amount) < 0) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("pay_fail")
                            .replace("%amount%", amountFormatted)));
                    return CommandResult.success();
                }

                User target = Cache.getplayer(args[0]);
                UUID targetUUID = Cache.translateUUID(args[0], null);
                if (targetUUID == null) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_account")));
                    return CommandResult.success();
                }
                String realname = Cache.getrealname(args[0]);

                if (!target.hasPermission("xconomy.user.pay.receive")) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_receive_permission")));
                    return CommandResult.success();
                }

                BigDecimal bal_target = Cache.getBalanceFromCacheOrDB(targetUUID);
                if (DataFormat.isMAX(bal_target.add(amount))) {
                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("over_maxnumber")));
                    return CommandResult.success();
                }

                String com = commandName + " " + args[0] + " " + amount;
                Cache.change("PLAYER_COMMAND", ((Player) sender).getUniqueId(), sender.getName(), amount, false, com);
                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("pay")
                        .replace("%player%", realname)
                        .replace("%amount%", amountFormatted)));

                Cache.change("PLAYER_COMMAND", targetUUID, realname, amount, true, com);
                String mess = sendMessage("prefix") + sendMessage("pay_receive")
                        .replace("%player%", sender.getName())
                        .replace("%amount%", amountFormatted);

                if (!target.isOnline()) {
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
                            sender.sendMessage(Text.of(sendMessage("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令")));
                            return CommandResult.success();
                        }

                        if (!(isOp(sender) || sender.hasPermission("xconomy.user.balance"))) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_permission")));
                            return CommandResult.success();
                        }

                        Player player = (Player) sender;


                        if (XConomy.config.getNode("Settings", "cache-correction").getBoolean()) {
                            Cache.refreshFromCache(player.getUniqueId());
                        }

                        BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId());
                        sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("balance")
                                .replace("%balance%", DataFormat.shown((a)))));

                        break;
                    }

                    case 1: {
                        if (!(isOp(sender) || sender.hasPermission("xconomy.user.balance.other"))) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_permission")));
                            return CommandResult.success();
                        }

                        UUID targetUUID = Cache.translateUUID(args[0], null);
                        if (targetUUID == null) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_account")));
                            return CommandResult.success();
                        }
                        String realname = Cache.getrealname(args[0]);

                        BigDecimal targetBalance = Cache.getBalanceFromCacheOrDB(targetUUID);
                        sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("balance_other")
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
                            sender.sendMessage(Text.of(sendMessage("prefix") + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令")));
                            return CommandResult.success();
                        }

                        if (!isDouble(args[2])) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
                            return CommandResult.success();
                        }

                        BigDecimal amount = DataFormat.formatString(args[2]);
                        String amountFormatted = DataFormat.shown(amount);
                        User target = Cache.getplayer(args[1]);
                        UUID targetUUID = Cache.translateUUID(args[1], null);
                        if (targetUUID == null) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_account")));
                            return CommandResult.success();
                        }

                        String realname = Cache.getrealname(args[1]);

                        String com = commandName + " " + args[0] + " " + args[1] + " " + amount;
                        String reason = null;
                        if (args.length == 4) {
                            reason = args[3];
                            com += " " + reason;
                        }

                        switch (args[0].toLowerCase()) {
                            case "give": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.give"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
                                    return CommandResult.success();
                                }

                                BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID);
                                if (DataFormat.isMAX(bal.add(amount))) {
                                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("over_maxnumber")));
                                    return CommandResult.success();
                                }

                                Cache.change("ADMIN_COMMAND", targetUUID, realname, amount, true, com);
                                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("money_give")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted)));

                                if (checkMessage("money_give_receive") | args.length == 4) {

                                    String message = sendMessage("prefix") + sendMessage("money_give_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        message = sendMessage("prefix") + reason;
                                    }

                                    if (!target.isOnline()) {
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
                                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
                                    return CommandResult.success();
                                }

                                BigDecimal bal = Cache.getBalanceFromCacheOrDB(targetUUID);
                                if (bal.compareTo(amount) < 0) {
                                    sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("money_take_fail")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted)));

                                    return CommandResult.success();
                                }

                                Cache.change("ADMIN_COMMAND", targetUUID, realname, amount, false, com);
                                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("money_take")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted)));

                                if (checkMessage("money_give_receive") | args.length == 4) {
                                    String mess = sendMessage("prefix") + sendMessage("money_take_receive")
                                            .replace("%player%", realname).replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        mess = sendMessage("prefix") + reason;
                                    }

                                    if (!target.isOnline()) {
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

                                Cache.change("ADMIN_COMMAND", targetUUID, realname, amount, null, com);
                                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("money_set")
                                        .replace("%player%", realname)
                                        .replace("%amount%", amountFormatted)));

                                if (checkMessage("money_give_receive") | args.length == 4) {
                                    String mess = sendMessage("prefix") + sendMessage("money_set_receive")
                                            .replace("%player%", realname)
                                            .replace("%amount%", amountFormatted);

                                    if (args.length == 4) {
                                        mess = sendMessage("prefix") + reason;
                                    }

                                    if (!target.isOnline()) {
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

                        if (check()) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + MessagesManager.systemMessage("§cBungeeCord模式开启的情况下,无法在无人的服务器中使用OP命令")));
                            return CommandResult.success();
                        }

                        if (!isDouble(args[3])) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
                            return CommandResult.success();
                        }

                        BigDecimal amount = DataFormat.formatString(args[3]);

                        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("invalid_amount")));
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

                                Cache.changeall(args[2], "ADMIN_COMMAND", amount, true, com);
                                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("money_give")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted)));

                                String message = sendMessage("prefix") + args[4];
                                Sponge.getServer().getBroadcastChannel().send(Text.of(message));
                                broadcastSendMessage(true, null, message);
                                break;
                            }

                            case "take": {
                                if (!(isOp(sender) | sender.hasPermission("xconomy.admin.take"))) {
                                    sendHelpMessage(sender, 1);
                                    return CommandResult.success();
                                }

                                Cache.changeall(args[2], "ADMIN_COMMAND", amount, false, com);
                                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("money_take")
                                        .replace("%player%", target)
                                        .replace("%amount%", amountFormatted)));

                                String message = sendMessage("prefix") + args[4];
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
        return Sponge.getServer().getOnlinePlayers().isEmpty() & ServerINFO.IsBungeeCordMode;
    }

    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    @SuppressWarnings("ConstantConditions")
    public static String sendMessage(String message) {
        return MessagesManager.messageFile.getNode(message).getString().replace("&", "§");
    }

    public static void showVersion(CommandSource sender) {
        sender.sendMessage(Text.of(sendMessage("prefix") + "§6 XConomy §f(Version: "
                + PluginINFO.VERSION + ") §6|§7 Author: §f" + MessagesManager.getAuthor()));
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(Text.of(sendMessage("prefix") + "§7 Translator (system): §f" + trs));
        }
    }

    public static void sendHelpMessage(CommandSource sender, Integer num) {
        List<String> helplist = new ArrayList<>();
        helplist.add(sendMessage("help1"));
        helplist.add(sendMessage("help2"));
        helplist.add(sendMessage("help3"));
        helplist.add(sendMessage("help4"));
        if (isOp(sender) | sender.hasPermission("xconomy.admin.give")) {
            helplist.add(sendMessage("help5"));
            helplist.add(sendMessage("help8"));
        }
        if (isOp(sender) | sender.hasPermission("xconomy.admin.take")) {
            helplist.add(sendMessage("help6"));
            helplist.add(sendMessage("help9"));
        }
        if (isOp(sender) | sender.hasPermission("xconomy.admin.set")) {
            helplist.add(sendMessage("help7"));
        }
        if (isOp(sender) | sender.hasPermission("xconomy.admin.balancetop")) {
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
        sender.sendMessage(Text.of(sendMessage("help_title_full").replace("%page%", num + "/" + maxipages)));
        int indexpage = 0;
        while (indexpage < 5) {
            if (helplist.size() > indexpage + (num - 1) * 5) {
                sender.sendMessage(Text.of(helplist.get(indexpage + (num - 1) * 5)));
            }
            indexpage += 1;
        }
    }

    private static void sendRankingMessage(CommandSource sender, Integer num) {
        Integer maxipages;
        int listsize = Cache.baltop_papi.size();
        if (listsize % 5 == 0) {
            maxipages = listsize / 5;
        } else {
            maxipages = listsize / 5 + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        int endindex = num * 5;
        if (endindex >= listsize) {
            endindex = listsize;
        }
        List<String> topNames = Cache.baltop_papi.subList(num * 5 - 5, endindex);

        sender.sendMessage(Text.of(sendMessage("top_title").replace("%page%", num + "/" + maxipages)));
        sender.sendMessage(Text.of(sendMessage("sum_text")
                .replace("%balance%", DataFormat.shown((Cache.sumbalance)))));
        int placement = 0;
        for (String topName : topNames) {
            placement++;
            sender.sendMessage(Text.of(sendMessage("top_text")
                    .replace("%index%", String.valueOf(num * 5 - 5 + placement))
                    .replace("%player%", topName)
                    .replace("%balance%", DataFormat.shown((Cache.baltop.get(topName))))));
        }

        if (checkMessage("top_subtitle"))
            sender.sendMessage(Text.of(sendMessage("top_subtitle")));

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
        Sponge.getChannelRegistrar().getOrCreateRaw(XConomy.getInstance(), "xconomy:acb").sendTo(
                Sponge.getServer().getOnlinePlayers().iterator().next(), buf -> buf.writeBytes(output.toByteArray()));

    }
}
