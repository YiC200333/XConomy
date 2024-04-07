/*
 *  This file (TabList.java) is a part of project XConomy
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
package me.yic.xconomy.listeners;

import me.yic.xconomy.AdapterManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabList implements TabCompleter {


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {

        final List<String> completions = new ArrayList<>();
        switch (commandName) {
            case "xconomy":
            case "xc": {
                if (args.length == 1) {
                    List<String> COMMANDS_xc = new ArrayList<>();
                    COMMANDS_xc.add("help");
                    if (commandSender.isOp()) {
                        COMMANDS_xc.add("reload");
                        COMMANDS_xc.add("deldata");
                    }
                    StringUtil.copyPartialMatches(args[0], COMMANDS_xc, completions);
                }
                Collections.sort(completions);
                break;
            }
            case "paypermission":
            case "payperm": {
                if (commandSender.isOp() || commandSender.hasPermission("xconomy.admin.permission")) {
                    List<String> COMMANDS_payperm = new ArrayList<>();
                    if (args.length == 1) {
                        COMMANDS_payperm.add("set");
                        COMMANDS_payperm.add("remove");
                        StringUtil.copyPartialMatches(args[0], COMMANDS_payperm, completions);
                    } else if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("remove")) {
                        if (args.length == 2) {
                            StringUtil.copyPartialMatches(args[1], AdapterManager.get_Tab_PlayerList(), completions);
                        } else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
                            COMMANDS_payperm.add("true");
                            COMMANDS_payperm.add("false");
                            StringUtil.copyPartialMatches(args[2], COMMANDS_payperm, completions);
                        }
                    }
                }
                Collections.sort(completions);
                break;
            }
            case "paytoggle":{
                if (commandSender.isOp() || commandSender.hasPermission("xconomy.admin.paytoggle")) {
                    if (args.length == 1) {
                        StringUtil.copyPartialMatches(args[0], AdapterManager.get_Tab_PlayerList(), completions);
                    }
                }
                Collections.sort(completions);
                break;
            }
            case "baltop":
            case "balancetop": {
                if (args.length == 1) {
                    List<String> COMMANDS_baltop = new ArrayList<>();
                    if (commandSender.isOp() || commandSender.hasPermission("xconomy.admin.balancetop")) {
                        COMMANDS_baltop.add("hide");
                        COMMANDS_baltop.add("display");
                    }
                    StringUtil.copyPartialMatches(args[0], COMMANDS_baltop, completions);
                } else if (args.length == 2) {
                    StringUtil.copyPartialMatches(args[1], AdapterManager.get_Tab_PlayerList(), completions);
                }
                Collections.sort(completions);
                break;
            }
            case "pay": {
                if (args.length == 1) {
                    if (commandSender.isOp() || commandSender.hasPermission("xconomy.user.pay")) {
                        StringUtil.copyPartialMatches(args[0], AdapterManager.get_Tab_PlayerList(), completions);
                    }
                }
                Collections.sort(completions);
                break;
            }
            case "money":
            case "bal":
            case "balance":
            case "economy":
            case "eco": {
                if (args.length == 1) {
                    List<String> COMMANDS_balance = new ArrayList<>();
                    if (commandSender.isOp() || commandSender.hasPermission("xconomy.user.balance.other")) {
                        COMMANDS_balance.addAll(AdapterManager.get_Tab_PlayerList());
                    }
                    if (commandSender.isOp() || commandSender.hasPermission("xconomy.admin.give")) {
                        COMMANDS_balance.add("give");
                    }
                    if (commandSender.isOp() || commandSender.hasPermission("xconomy.admin.take")) {
                        COMMANDS_balance.add("take");
                    }
                    if (commandSender.isOp() || commandSender.hasPermission("xconomy.admin.set")) {
                        COMMANDS_balance.add("set");
                    }
                    StringUtil.copyPartialMatches(args[0], COMMANDS_balance, completions);
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("take")
                            || args[0].equalsIgnoreCase("set")) {
                        if (commandSender.isOp() || commandSender.hasPermission("xconomy.user.balance.other")
                                || commandSender.hasPermission("xconomy.admin.give") || commandSender.hasPermission("xconomy.admin.take")
                                || commandSender.hasPermission("xconomy.admin.set")) {
                            StringUtil.copyPartialMatches(args[1], AdapterManager.get_Tab_PlayerList(), completions);
                        }
                    }
                } else if (args.length == 3) {
                    if (args[1].equals("*")) {
                        if (commandSender.isOp() || commandSender.hasPermission("xconomy.user.balance.other")
                                || commandSender.hasPermission("xconomy.admin.give") || commandSender.hasPermission("xconomy.admin.take")
                                || commandSender.hasPermission("xconomy.admin.set")) {
                            List<String> COMMANDS_balance_all = new ArrayList<>();
                            COMMANDS_balance_all.add("all");
                            COMMANDS_balance_all.add("online");
                            StringUtil.copyPartialMatches(args[2], COMMANDS_balance_all, completions);
                        }
                    }
                }
                Collections.sort(completions);
                break;
            }
        }
        return completions;
    }
}
