/*
 *  This file (CommandPayToggle.java) is a part of project XConomy
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
import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.adapter.comp.CSender;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.lang.MessagesManager;

import java.util.UUID;

public class CommandPayToggle extends CommandCore{
    public static boolean onCommand(CSender sender, String[] args) {
        if (args.length == 0) {
            if (!sender.isPlayer()) {
                sendMessages(sender, PREFIX + MessagesManager.systemMessage("§6控制台无法使用该指令"));
                return true;
            }
            if (sender.isOp() || sender.hasPermission("xconomy.user.paytoggle")) {
                PermissionINFO.setRPaymentPermission(DataCon.getPlayerData(sender.toPlayer().getUniqueId()).getUniqueId());
                if (PermissionINFO.getRPaymentPermission(sender.toPlayer().getUniqueId())) {
                    sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_TRUE));
                    syncpr(2, sender.toPlayer().getUniqueId(), true);
                } else {
                    sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_FALSE));
                    syncpr(2, sender.toPlayer().getUniqueId(), false);
                }
            }
            return true;
        } else if (args.length == 1) {
            if (sender.isOp() || sender.hasPermission("xconomy.admin.paytoggle")) {
                if (check()) {
                    sendMessages(sender, PREFIX + MessagesManager.systemMessage("§cBC模式开启的情况下,无法在无人的服务器中使用OP命令"));
                    return true;
                }
                PlayerData pd = DataCon.getPlayerData(args[0]);
                if (pd == null) {
                    sendMessages(sender, PREFIX + translateColorCodes(MessageConfig.NO_ACCOUNT));
                } else {
                    UUID targetUUID = pd.getUniqueId();
                    CPlayer target = AdapterManager.PLUGIN.getplayer(pd);
                    String realname = pd.getName();
                    PermissionINFO.setRPaymentPermission(pd.getUniqueId());

                    String mess = translateColorCodes(MessageConfig.PAYTOGGLE_TRUE);
                    if (PermissionINFO.getRPaymentPermission(pd.getUniqueId())) {
                        syncpr(2, targetUUID, true);
                        sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_OTHER_TRUE)
                                .replace("%player%", realname));
                    } else {
                        syncpr(2, targetUUID, false);
                        sendMessages(sender, translateColorCodes(MessageConfig.PAYTOGGLE_OTHER_FALSE)
                                .replace("%player%", realname));

                        mess = translateColorCodes(MessageConfig.PAYTOGGLE_FALSE);
                    }
                    if (target.isOnline()) {
                        broadcastSendMessage(false, pd, mess);
                        return true;
                    }

                    target.sendMessage(mess);
                }
                return true;
            }
        }
        return true;
    }

}
