package me.Yi.XConomy.Task;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import me.Yi.XConomy.XConomy;

public class Updater extends BukkitRunnable {

	public static boolean isold = false;
	public static String vs = "none";

	@Override
	public void run() {
		try {
			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=75669");
			URLConnection conn = url.openConnection();
			String vs = new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
			List<String> vsls = Arrays.asList(vs.split("\\."));
			List<String> nvs = Arrays.asList(XConomy.getInstance().getDescription().getVersion().split("\\."));
			if (compare(vsls, nvs)) {
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

	private static boolean compare(List<String> web, List<String> pl) {
		for (int i = 0; i < 5; i++) {
			Integer v1 = 0;
			Integer v2 = 0;
			if (web.size() >=i+1) {
				v1 = Integer.parseInt(web.get(i));
			}
			if (pl.size() >=i+1) {
				v2 = Integer.parseInt(pl.get(i));
			}
			Integer result = Integer.compare(v1 - v2, 0);
			if (result > 0) {
				isold = true;
				return true;
			}
		}
		return false;
	}

}
