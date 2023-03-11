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

public class CommandPaytoggle extends CommandResOutput implements CommandExecutor {
    public static final Parameter.Value<String> arg1 = Parameter.string().key("arg1").optional().build();
    public static final Parameter.Value<String> arg2 = Parameter.string().key("arg2").optional().build();

    @SuppressWarnings({"OptionalGetWithoutIsPresent"})
    @Override
    public CommandResult execute(CommandContext args) {
        CommandCause sender = args.cause();
        if (args.hasAny(arg1)) {
            if (args.hasAny(arg2)) {
                String[] cmd = {args.one(arg1).get().trim(),
                        args.one(arg2).get().trim()};
                return getResultonCommand(sender, "paytoggle", cmd);
            } else {
                String[] cmd = {args.one(arg1).get().trim()};
                return getResultonCommand(sender, "paytoggle", cmd);
            }
        } else {
            String[] cmd = {};
            return getResultonCommand(sender, "paytoggle", cmd);
        }
    }
}
