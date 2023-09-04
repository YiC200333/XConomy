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
package me.yic.xconomy.command.core;

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.adapter.comp.CSender;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.data.syncdata.SyncMessage;
import me.yic.xconomy.data.syncdata.SyncPermission;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.info.SyncType;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.utils.UUIDMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandCore {

    protected static String PREFIX = translateColorCodes("prefix");

    public static boolean onCommand(CSender sender, String commandName, String[] args) {
        switch (commandName) {
            case "xconomy": {
                if (sender.isOp()) {
                    if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                        MessagesManager.loadlangmess();
                        PREFIX = translateColorCodes("prefix");
                        sendMessages(sender, PREFIX + MessagesManager.systemMessage("§amessage.yml重载成功"));
                        return true;
                    }
                    if (args.length == 2 && args[0].equalsIgnoreCase("deldata")) {

                        if (check()) {
                            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                            return true;
                        }

                        PlayerData pd = DataCon.getPlayerData(args[1]);
                        if (pd == null) {
                            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                            return true;
                        }

                        DataCon.deletePlayerData(pd);

                        sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.DELETE_DATA_ADMIN).replace("%player%", pd.getName()));

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
                return CommandPayPermission.onCommand(sender, args);
            }

            case "paytoggle": {
                return CommandPayToggle.onCommand(sender, args);
            }

            case "balancetop": {
                return CommandBalancetop.onCommand(sender, args);
            }

            case "pay": {
                return CommandPay.onCommand(sender, commandName, args);
            }

            case "money":
            case "balance":
            case "economy":
            case "eco": {
                return CommandBalance.onCommand(sender, commandName, args);
            }

            default: {
                sendHelpMessage(sender, 1);
                break;
            }

        }

        return true;
    }

    protected static boolean isDouble(String s) {
        if (s.length() > 20){
            return false;
        }
        if (s.matches(".*[a-zA-Z].*")) {
            return false;
        }

        BigDecimal value;
        if (DataFormat.isint){
            try {
                Integer.parseInt(s);
                value = new BigDecimal(s);
            } catch (NumberFormatException ignored) {
                return false;
            }
        }else {
            try {
                Double.parseDouble(s);
                Pattern pattern = Pattern.compile("\\.\\d+");
                Matcher matcher = pattern.matcher(s);

                if (matcher.find()) {
                    String decimalPart = matcher.group();
                    int decimalPlaces = decimalPart.length() - 1;
                    if (decimalPlaces > 2){
                        return false;
                    }
                }
                value = new BigDecimal(s);
            } catch (NumberFormatException ignored) {
                return false;
            }
        }

        if (value.compareTo(BigDecimal.ZERO) > 0) {
            return !DataFormat.isMAX(DataFormat.formatString(s));
        }

        return true;
    }

    public static boolean check() {
        return AdapterManager.BanModiftyBalance();
    }

    public static boolean checkMessage(String message) {
        return !MessagesManager.messageFile.getString(message).equals("");
    }

    protected static void sendMessages(CPlayer sender, String message) {
        if (!message.replace(PREFIX, "").equalsIgnoreCase("")) {
            if (message.contains("\\n")) {
                String[] messs = message.split("\\\\n");
                sender.sendMessage(messs);
            } else {
                sender.sendMessage(message);
            }
        }
    }

    protected static void sendMessages(CSender sender, String message) {
        if (!message.replace(PREFIX, "").equalsIgnoreCase("")) {
            if (message.contains("\\n")) {
                String[] messs = message.split("\\\\n");
                sender.sendMessage(messs);
            } else {
                sender.sendMessage(message);
            }
        }
    }

    protected static String translateColorCodes(MessageConfig message) {
        return AdapterManager.translateColorCodes(message);
    }

    protected static String translateColorCodes(String message) {
        return AdapterManager.translateColorCodes(message);
    }

    public static void showVersion(CSender sender) {
        sender.sendMessage(PREFIX + "§6XConomy §f(Version: "
                + XConomy.PVersion + ") §6|§7 Author: §f" + MessagesManager.getAuthor());
        String trs = MessagesManager.getTranslatorS();
        if (trs != null) {
            sender.sendMessage(PREFIX + "§7Translator (system): §f" + trs);
        }
    }

    protected static void sendHelpMessage(CSender sender, Integer num) {
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
        if (helplist.size() % XConomyLoad.Config.LINES_PER_PAGE == 0) {
            maxipages = helplist.size() / XConomyLoad.Config.LINES_PER_PAGE;
        } else {
            maxipages = helplist.size() / XConomyLoad.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        sendMessages(sender, translateColorCodes("help_title_full").replace("%page%", num + "/" + maxipages));
        int indexpage = 0;
        while (indexpage < XConomyLoad.Config.LINES_PER_PAGE) {
            if (helplist.size() > indexpage + (num - 1) * XConomyLoad.Config.LINES_PER_PAGE) {
                sender.sendMessage(helplist.get(indexpage + (num - 1) * XConomyLoad.Config.LINES_PER_PAGE));
            }
            indexpage += 1;
        }
    }

    protected static void sendRankingMessage(CSender sender, Integer num) {
        Integer maxipages;
        int listsize = Cache.baltop_papi.size();
        if (listsize % XConomyLoad.Config.LINES_PER_PAGE == 0) {
            maxipages = listsize / XConomyLoad.Config.LINES_PER_PAGE;
        } else {
            maxipages = listsize / XConomyLoad.Config.LINES_PER_PAGE + 1;
        }
        if (num > maxipages) {
            num = maxipages;
        }
        int endindex = num * XConomyLoad.Config.LINES_PER_PAGE;
        if (endindex >= listsize) {
            endindex = listsize;
        }
        List<String> topNames = Cache.baltop_papi.subList(num * XConomyLoad.Config.LINES_PER_PAGE - XConomyLoad.Config.LINES_PER_PAGE, endindex);

        sendMessages(sender, translateColorCodes("top_title").replace("%page%", num + "/" + maxipages));
        sendMessages(sender, translateColorCodes("sum_text")
                .replace("%balance%", DataFormat.shown((Cache.sumbalance))));
        int placement = 0;
        for (String topName : topNames) {
            placement++;
            sendMessages(sender, translateColorCodes("top_text")
                    .replace("%index%", String.valueOf(num * XConomyLoad.Config.LINES_PER_PAGE - XConomyLoad.Config.LINES_PER_PAGE + placement))
                    .replace("%player%", topName)
                    .replace("%balance%", DataFormat.shown((Cache.baltop.get(topName)))));
        }

        sendMessages(sender, translateColorCodes("top_subtitle"));

    }


    protected static void broadcastSendMessage(boolean ispublic, PlayerData pd, String message) {
        if (!XConomyLoad.getSyncData_Enable()) {
            return;
        }
        if (check() && AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            return;
        }

        SyncMessage sm;
        if (!ispublic) {
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                sm = new SyncMessage(SyncType.MESSAGE_SEMI, pd.getName(), message);
            } else {
                sm = new SyncMessage(SyncType.MESSAGE, pd.getUniqueId(), message);
            }
        } else {
            sm = new SyncMessage(SyncType.BROADCAST, "", message);
        }

       DataCon.SendMessTask(sm);
    }

    protected static void syncpr(int type, UUID u, Boolean value) {
        if (!XConomyLoad.getSyncData_Enable()) {
            return;
        }

        if (check() && AdapterManager.PLUGIN.getOnlinePlayersisEmpty()) {
            return;
        }

        SyncPermission output = new SyncPermission(u, type, value);

        DataCon.SendMessTask(output);
    }

}
