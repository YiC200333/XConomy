package me.yic.xconomy;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class CommandSponge implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String[] argsb = {};
        //boolean aa = CommandHandler.onCommand(src, "balance", argsb);
        return CommandResult.empty();
    }
}
