/*
 *  This file (Command.java) is a part of project XConomy
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

import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.lang.MessagesManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.UUID;

public class Command implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) throws CommandException {
        if (!args.hasAny(Text.of("Player"))) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Text.of(sendMessage("prefix") + MessagesManager.systemMessage("§6控制台无法使用该指令")));
                return CommandResult.empty();
            }

            Player player = (Player) sender;


            if (XConomy.config.getNode("Settings.cache-correction").getBoolean()) {
                Cache.refreshFromCache(player.getUniqueId());
            }

            BigDecimal a = Cache.getBalanceFromCacheOrDB(player.getUniqueId());
            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("balance")
                    .replace("%balance%", DataFormat.shown((a)))));
            return CommandResult.success();
        }else{
            if (!(isOp(sender) || sender.hasPermission("xconomy.user.balance"))) {
                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("no_permission")));
                return CommandResult.empty();
            }
            String targetname = args.<String>getOne("Player").get();
            UUID targetUUID = Cache.translateUUID(targetname, null);
            if (targetUUID == null) {
                sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("noaccount")));
                return CommandResult.empty();
            }

            BigDecimal targetBalance = Cache.getBalanceFromCacheOrDB(targetUUID);
            sender.sendMessage(Text.of(sendMessage("prefix") + sendMessage("balance_other")
                    .replace("%player%", targetname)
                    .replace("%balance%", DataFormat.shown((targetBalance)))));

            return CommandResult.success();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String sendMessage(String message) {
        return MessagesManager.messageFile.getNode(message).getString().replace("&","§");
    }

    public static Boolean isOp(CommandSource p) {
        return p.hasPermission("xconomy.admin.op");
    }
}
