package me.Yi.XConomy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Yi.XConomy.Message.Messages;

public class cmd_fail implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("money") | command.getName().equalsIgnoreCase("balance")) {
				String m1 = "XConomy 不支持 Vault 变量的 baltop 功能";
				String m2 = "请在 PlaceholderAPI 的 config.yml 中设置 expansions.vault.baltop.enabled 为 false";
				sender.sendMessage(Messages.sysmess(m1));
				sender.sendMessage(Messages.sysmess(m2));
			}
		return true;
	}
}
