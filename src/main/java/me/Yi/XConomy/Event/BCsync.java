package me.Yi.XConomy.Event;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.Yi.XConomy.XConomy_b;
import me.Yi.XConomy.Task.SendMessTaskB;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BCsync implements Listener {
	// private mains plugin = mains.getInstance();

	@EventHandler
	public void on(PluginMessageEvent ev) {
	    if (!(ev.getSender() instanceof Server)) {
	        return;
	      }
		if (!ev.getTag().equalsIgnoreCase("xconomy:acb")) {
			return;
		}
		ByteArrayDataInput input = ByteStreams.newDataInput(ev.getData());
		Server ta = (Server) ev.getSender();
		//Map<String, ServerInfo> ss = ProxyServer.getInstance().getServers();
		//Collection<ServerInfo> valueCollection = ss.values();
		//List<ServerInfo> ssl = new ArrayList<ServerInfo>(valueCollection);
		//ssl.remove(((Server) ev.getSender()).getInfo());
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
				if (p!=null) {
					output.writeUTF(name);
					output.writeUTF(input.readUTF());
				} else {
					return;
				}
			}
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().severe("An I/O error occurred!");
		}
		//for (ServerInfo n : ssl) {
		//	n.sendData("xconomy:aca", stream.toByteArray());
		//}
        for ( ServerInfo s : ProxyServer.getInstance().getServers().values() ) {
            if ( !s.getName().equals( ta.getInfo().getName() ) && s.getPlayers().size() > 0 ) {
            	ProxyServer.getInstance().getScheduler().runAsync(XConomy_b.getInstance(), new SendMessTaskB(s, stream));
            }
        }

	}

}
