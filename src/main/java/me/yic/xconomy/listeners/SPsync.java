package me.yic.xconomy.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.XConomy;
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
		} else if (type.equalsIgnoreCase("balanceall")) {
			String targettype = input.readUTF();
			String amount = input.readUTF();
			String isadds = input.readUTF();
			if (targettype.equalsIgnoreCase("all")){
				Cache.bal.clear();
			}else if (targettype.equalsIgnoreCase("online")){
				Cache.bal.clear();
				Boolean isadd = null;
				if (isadds.equalsIgnoreCase("add")) {
					isadd = true;
				} else if (isadds.equalsIgnoreCase("add")) {
					isadd = false;
				}
				DataCon.saveall("online",DataFormat.formatString(amount),isadd,null);
			}
		}else if (type.equalsIgnoreCase("broadcast")) {
			String mess = input.readUTF();
			Bukkit.broadcastMessage(mess);
		}
	}

}
