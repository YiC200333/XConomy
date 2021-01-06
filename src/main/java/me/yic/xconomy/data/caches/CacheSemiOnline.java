package me.yic.xconomy.data.caches;

import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CacheSemiOnline {
	public static File cachesubuuid;
	public static FileConfiguration CacheSubUUID;

	public static boolean createfile() {
		if (XConomy.issemionlinemode()) {
			File dataFolder = new File(XConomy.getInstance().getDataFolder(), "cache");
			dataFolder.mkdirs();
			cachesubuuid = new File(dataFolder, "cache_subuuid.yml");
			CacheSubUUID = YamlConfiguration.loadConfiguration(cachesubuuid);
			if (!cachesubuuid.exists()) {
				try {
						CacheSubUUID.save(cachesubuuid);
				} catch (IOException e) {
					e.printStackTrace();
					XConomy.getInstance().logger("缓存文件创建异常");
					return false;
				}
			}
		}
		return true;
	}


	public static void CacheSubUUID_checkUser(String mainu, Player pp) {
		if (CacheSubUUID.contains(mainu)) {
			if (!CacheSubUUID.get(mainu + ".SubUUID").equals(pp.getUniqueId().toString())){
				if (pp.isOnline()) {
					Bukkit.getScheduler().runTask(XConomy.getInstance(), () ->
							pp.kickPlayer("[XConomy] The player with the same name exists on the server (Three times)"));
				}
			}
		}else{
			CacheSubUUID.createSection(mainu + ".SubUUID");
			CacheSubUUID.set(mainu + ".SubUUID", pp.getUniqueId().toString());
		}
	}

	public static UUID CacheSubUUID_getsubuuid(String name) {
		if (CacheSubUUID.contains(name)) {
			return UUID.fromString(CacheSubUUID.getString(name + ".SubUUID"));
		}else{
			return null;
		}
	}

	public static void save() {
		try {
			if (XConomy.config.getBoolean("Settings.semi-online-mode")) {
				CacheSubUUID.save(cachesubuuid);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}