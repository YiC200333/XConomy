package me.yic.xconomy.utils;

import me.yic.xconomy.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EconomyCommand extends Command {
	    private final String name;
		public EconomyCommand(String name) {
			super(name);
			this.name = name;
			this.description = "XConomy.";
			this.usageMessage = "/<command>";
		}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
		return CommandHandler.onCommand(sender,name,args);
	}

}
