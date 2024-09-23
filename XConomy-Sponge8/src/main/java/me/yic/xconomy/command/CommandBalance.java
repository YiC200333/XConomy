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
package me.yic.xconomy.command;

import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBalance extends CommandResOutput implements CommandExecutor {
    public static final Parameter.Value<String> arg1 = Parameter.string().key("arg1").optional().build();
    public static final Parameter.Value<String> arg2 = Parameter.string().key("arg2").optional().build();
    public static final Parameter.Value<String> arg3 = Parameter.string().key("arg3").optional().build();
    public static final Parameter.Value<String> arg4 = Parameter.string().key("arg4").optional().build();
    public static final Parameter.Value<String> arg5 = Parameter.remainingJoinedStrings().key("arg5").optional().build();

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    @Override
    public CommandResult execute(CommandContext args) {
        CommandCause sender = args.cause();
        if (args.hasAny(arg1)) {
            if (args.hasAny(arg2)) {
                if (args.hasAny(arg3)) {
                    if (args.hasAny(arg4)) {
                        if (args.hasAny(arg5)) {
                            String amountortype = args.one(arg3).get().trim();
                            if (!isDouble(amountortype)) {
                                List<String> cmdl = new ArrayList<>();
                                cmdl.add(args.one(arg1).get().trim());
                                Collections.addAll(cmdl, args.one(arg5).get().trim().split(" "));
                                String[] cmd = new String[cmdl.size()];
                                return getResultonCommand(sender, "balance", cmdl.toArray(cmd));
                            } else {
                                String[] cmd = {args.one(arg1).get().trim(),
                                        args.one(arg2).get().trim(),
                                        amountortype,
                                        args.one(arg4).get().trim() + " " +
                                                args.one(arg5).get().trim()};
                                return getResultonCommand(sender, "balance", cmd);
                            }
                        } else {
                            String[] cmd = {args.one(arg1).get().trim(),
                                    args.one(arg2).get().trim(),
                                    args.one(arg3).get().trim(),
                                    args.one(arg4).get().trim()};
                            return getResultonCommand(sender, "balance", cmd);
                        }
                    } else {
                        String[] cmd = {args.one(arg1).get().trim(),
                                args.one(arg2).get().trim(),
                                args.one(arg3).get().trim()};
                        return getResultonCommand(sender, "balance", cmd);
                    }
                } else {
                    String[] cmd = {args.one(arg1).get().trim(),
                            args.one(arg2).get().trim()};
                    return getResultonCommand(sender, "balance", cmd);
                }
            } else {
                String[] cmd = {args.one(arg1).get().trim()};
                return getResultonCommand(sender, "balance", cmd);
            }
        } else {
            String[] cmd = {};
            return getResultonCommand(sender, "balance", cmd);
        }
    }
}
