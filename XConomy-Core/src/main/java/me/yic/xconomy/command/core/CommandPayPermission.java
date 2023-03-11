/*
 *  This file (CommandPayPermission.java) is a part of project XConomy
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

import me.yic.xconomy.adapter.comp.CSender;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.info.MessageConfig;
import me.yic.xconomy.info.PermissionINFO;

import java.util.UUID;

public class CommandPayPermission extends CommandCore{
    public static boolean onCommand(CSender sender, String[] args) {
        if (sender.isOp() || sender.hasPermission("xconomy.admin.permission")) {
            if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) {
                    boolean vv = !args[2].equalsIgnoreCase("false");
                    if (args[1].equalsIgnoreCase("*")) {
                        PermissionINFO.globalpayment = vv;
                        sendMessages(sender, translateColorCodes("global_permissions_change").replace("%permission%", "pay")
                                .replace("%value%", args[2]));
                        syncpr(1, null, vv);
                    } else {
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
        return true;
    }

}
