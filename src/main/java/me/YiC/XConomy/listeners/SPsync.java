package me.YiC.XConomy.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.YiC.XConomy.data.caches.Cache;
import me.YiC.XConomy.data.DataFormat;
import me.YiC.XConomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class SPsync implements PluginMessageListener {

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		if (!channel.equals("xconomy:aca")) {
			return;
		}

		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String type = input.readUTF();
		String sign = input.readUTF();
		if (!sign.equals(XConomy.getSign())) {
			return;
		}

		if (type.equalsIgnoreCase("balance")) {
			UUID u = UUID.fromString(input.readUTF());
			String bal = input.readUTF();
			Cache.bal.put(u, DataFormat.formatString(bal));
		} else if (type.equalsIgnoreCase("message")) {
			Player p = Bukkit.getPlayer(input.readUTF());
			String mess = input.readUTF();
			if (p != null) {
				p.sendMessage(mess);
			}
		}
	}

}
