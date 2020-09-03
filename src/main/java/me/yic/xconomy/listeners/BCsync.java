package me.yic.xconomy.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.task.SendMessTaskB;
import me.yic.xconomy.XConomyBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BCsync implements Listener {

	@SuppressWarnings("UnstableApiUsage")
	@EventHandler
	public void on(PluginMessageEvent event) {
		if (!(event.getSender() instanceof Server)) {
			return;
		}

		if (!event.getTag().equalsIgnoreCase("xconomy:acb")) {
			return;
		}

		ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
		Server senderServer = (Server) event.getSender();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(stream);
		String type = input.readUTF();
		try {
			if (type.equalsIgnoreCase("balance")) {
				output.writeUTF("balance");
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
				output.writeUTF(input.readUTF());
			} else if (type.equalsIgnoreCase("message")) {
				output.writeUTF("message");
				output.writeUTF(input.readUTF());
				String name = input.readUTF();
				ProxiedPlayer p = ProxyServer.getInstance().getPlayer(name);
				if (p != null) {
					output.writeUTF(name);
					output.writeUTF(input.readUTF());
				} else {
					return;
				}
			}
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().severe("An I/O error occurred!");
		}

		for (ServerInfo s : ProxyServer.getInstance().getServers().values()) {
			if (!s.getName().equals(senderServer.getInfo().getName()) && s.getPlayers().size() > 0) {
				ProxyServer.getInstance().getScheduler().runAsync(XConomyBungee.getInstance(), new SendMessTaskB(s, stream));
			}
		}

	}

}
