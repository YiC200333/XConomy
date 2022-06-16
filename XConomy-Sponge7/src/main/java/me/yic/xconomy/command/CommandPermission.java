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

import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class CommandPermission extends CommandCore implements CommandExecutor {

    @SuppressWarnings({"OptionalGetWithoutIsPresent", "NullableProblems"})
    @Override
    public CommandResult execute(CommandSource sender, CommandContext args) {
        if (args.hasAny(Text.of("arg1"))) {
            if (args.hasAny(Text.of("arg2"))) {
                if (args.hasAny(Text.of("arg3"))) {
                    String[] cmd = {args.<String>getOne("arg1").get().trim(),
                            args.<String>getOne("arg2").get().trim(),
                            args.<String>getOne("arg3").get().trim()};
                    return onCommand(sender, "paypermission", cmd);
                } else {
                    String[] cmd = {args.<String>getOne("arg1").get().trim(),
                            args.<String>getOne("arg2").get().trim()};
                    return onCommand(sender, "paypermission", cmd);
                }
            } else {
                String[] cmd = {args.<String>getOne("arg1").get().trim()};
                return onCommand(sender, "paypermission", cmd);
            }
        } else {
            String[] cmd = {};
            return onCommand(sender, "paypermission", cmd);
        }
    }
}
