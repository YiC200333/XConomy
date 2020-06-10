package me.Yi.XConomy;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;

import org.bukkit.scheduler.BukkitRunnable;

public class Updater extends BukkitRunnable {

	public static Integer isold = 0;
	public static String vs = "none";

	@Override
	public void run() {
		try {
			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=75669");
			URLConnection conn = url.openConnection();
			vs = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
			String nvs = XConomy.getInstance().getDescription().getVersion();
			if (Double.valueOf(vs) > Double.valueOf(nvs)) {
				isold = 1;
				if (XConomy.getInstance().lang().equalsIgnoreCase("Chinese")
						| XConomy.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
					XConomy.getInstance().logger("发现新版本 " + vs);
					XConomy.getInstance().logger("https://www.mcbbs.net/thread-962904-1-1.html");
				} else {
					XConomy.getInstance().logger("Discover the new version " + vs);
					XConomy.getInstance().logger("https://www.spigotmc.org/resources/xconomy.75669/");
				}
			} else {
				XConomy.getInstance().logger("已是最新版本");
			}

		} catch (Exception exception) {
			XConomy.getInstance().logger("检查更新失败");
		}
		return;
	}

}
