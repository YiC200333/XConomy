/*
 *  This file (CommandPay.java) is a part of project XConomy
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
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.adapter.comp.CSender;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.depend.NonPlayerPlugin;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.lang.MessagesManager;
import me.yic.xconomy.task.ReceivePerCheck;

import java.math.BigDecimal;
import java.util.UUID;

public class CommandPay extends CommandCore{
    public static boolean onCommand(CSender sender, String commandName, String[] args) {
        if (!sender.isPlayer()) {
            sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
            return true;
        }

        if (!sender.isOp()) {
            if (!PermissionINFO.getGlobalPayment()) {
                sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                return true;
            }

            if (PermissionINFO.getPaymentPermission(sender.toPlayer().getUniqueId()) == null) {
                if (!(sender.hasPermission("xconomy.user.pay"))) {
                    sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                    return true;
                }
            } else if (!PermissionINFO.getPaymentPermission(sender.toPlayer().getUniqueId())) {
                sendMessages(sender, PREFIX + translateColorCodes("no_permission"));
                return true;
            }
        }

        if (args.length != 2) {
            sendHelpMessage(sender, 1);
            return true;
        }

        if (NonPlayerPlugin.SimpleCheckNonPlayerAccount(args[0])) {
            sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
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

        BigDecimal taxamount = amount.multiply(XConomyLoad.Config.PAYMENT_TAX);

        //Cache.refreshFromCache(sender.toPlayer().getUniqueId());

        String amountFormatted = DataFormat.shown(amount);
        String taxamountFormatted = DataFormat.shown(taxamount);
        BigDecimal bal_sender = DataCon.getPlayerData(sender.toPlayer().getUniqueId()).getBalance();

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

        CPlayer target = AdapterManager.PLUGIN.getplayer(pd);
        UUID targetUUID = pd.getUniqueId();
        String realname = pd.getName();
        if (PermissionINFO.getRPaymentPermission(targetUUID)) {
            if (AdapterManager.foundvaultOfflinePermManager) {
                if (!ReceivePerCheck.hasreceivepermission(target, targetUUID)) {
                    sendMessages(sender, PREFIX + translateColorCodes("no_receive_permission"));
                    return true;
                }
            }
        } else {
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
        DataCon.changeplayerdata("PLAYER_COMMAND", sender.toPlayer().getUniqueId(), taxamount, false, com, null);
        sendMessages(sender, PREFIX + translateColorCodes("pay")
                .replace("%player%", realname)
                .replace("%amount%", amountFormatted));

        DataCon.changeplayerdata("PLAYER_COMMAND", targetUUID, amount, true, com, null);
        String mess = PREFIX + translateColorCodes("pay_receive")
                .replace("%player%", sender.getName())
                .replace("%amount%", amountFormatted);

        if (!target.isOnline()) {
            broadcastSendMessage(false, pd, mess);
            return true;
        }

        target.sendMessage(mess);
        return true;
    }

}
