/*
 *  This file (ReceivePerCheck.java) is a part of project XConomy
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
package me.yic.xconomy.task;

import me.yic.xconomy.adapter.comp.CPlayer;
import me.yic.xconomy.depend.economy.VaultHook;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ReceivePerCheck {

    public static boolean hasreceivepermission(CPlayer target, UUID targetUUID) {
        try {
            return CompletableFuture.supplyAsync(() -> exhasreceivepermission(target, targetUUID)).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean exhasreceivepermission(CPlayer target, UUID targetUUID) {
        if (!target.isOnline()) {
            return VaultHook.vaultPerm.playerHas(null,
                    Bukkit.getOfflinePlayer(targetUUID), "xconomy.user.pay.receive");
        } else{
            return target.hasPermission("xconomy.user.pay.receive");
        }
    }
}
