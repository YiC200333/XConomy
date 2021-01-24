/*
 *  This file (CompletableFutureTask.java) is a part of project XConomy
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

import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureTask {

    public static boolean hasreceivepermission(Player target, UUID targetUUID) {
            if (target == null || !target.isOnline()) {
                CompletableFuture<Boolean> future = new CompletableFuture<>();
                new Thread(() -> future.complete(XConomy.vaultPerm.playerHas(null,
                        Bukkit.getOfflinePlayer(targetUUID), "xconomy.user.pay.receive"))).start();

                try {
                    return future.get();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

            } else return target.hasPermission("xconomy.user.pay.receive");
        return false;
    }
}
