package me.Yi.XConomy.Event;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import me.Yi.XConomy.XConomy;
import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.DataFormat;

public class SPsync implements PluginMessageListener {

	@Override
	public void onPluginMessageReceived(String channel, Player arg1, byte[] message) {
		if (!channel.equals("xconomy:aca")) {
			return;
		}
		ByteArrayDataInput input = ByteStreams.newDataInput(message);
		String type = input.readUTF();
		String sign = input.readUTF();
		if (!sign.equals(XConomy.getsign())) {
			return;
		}
		if (type.equalsIgnoreCase("balance")) {
			UUID u = UUID.fromString(input.readUTF());
			Double bal = Double.valueOf(input.readUTF());
			Cache.bal.put(u, DataFormat.formatd(bal));
		} else if (type.equalsIgnoreCase("message")) {
			Player p = Bukkit.getPlayer(input.readUTF());
			String mess = input.readUTF();
			if (p != null) {
				p.sendMessage(mess);
			}
		}
	}

}
