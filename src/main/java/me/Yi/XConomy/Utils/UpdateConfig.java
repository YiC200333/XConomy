package me.Yi.XConomy.Utils;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class UpdateConfig {
	public static boolean update(FileConfiguration config,File cc) {
		boolean x = false;
		FileConfiguration ck = YamlConfiguration.loadConfiguration(cc);
		if (!ck.contains("Settings.check-update")) {
			config.createSection("Settings.check-update");
			config.set("Settings.check-update", true);
			x = true;
		}
		if (!ck.contains("MySQL.table_suffix")) {
			config.createSection("MySQL.table_suffix");
			config.set("MySQL.table_suffix", "");
			x = true;
		}
		if (!ck.contains("Settings.non-player-account")) {
			config.createSection("Settings.non-player-account");
			config.set("Settings.non-player-account", false);
			x = true;
		}
		if (!ck.contains("Currency")) {
			config.createSection("Currency.singular-name");
			config.set("Currency.singular-name", "dollar");
			config.createSection("Currency.plural-name");
			config.set("Currency.plural-name", "dollars");
			config.createSection("Currency.integer-bal");
			if (ck.contains("Settings.integer")) {
				config.set("Currency.integer-bal", config.getBoolean("Settings.integer"));
			} else {
				config.set("Currency.integer-bal", false);
			}
			config.createSection("Currency.display-format");
			config.set("Currency.display-format", "%balance% %currencyname%");
			x = true;
		}
		if (!ck.contains("Currency.thousands-separator")) {
			config.createSection("Currency.thousands-separator");
			config.set("Currency.thousands-separator", ",");
			x = true;
		}
		if (!ck.contains("Pool-Settings.maximum-lifetime")) {
			config.createSection("Pool-Settings.maximum-pool-size");
			config.set("Pool-Settings.maximum-pool-size", 10);
			config.createSection("Pool-Settings.minimum-idle");
			config.set("Pool-Settings.minimum-idle", 10);
			config.createSection("Pool-Settings.maximum-lifetime");
			config.set("Pool-Settings.maximum-lifetime", 180000);
			x = true;
		}
		if (!ck.contains("Pool-Settings.idle-timeout")) {
			config.createSection("Pool-Settings.idle-timeout");
			config.set("Pool-Settings.idle-timeout", 60000);
			x = true;
		}
		if (!ck.contains("Settings.refresh-time")) {
			config.createSection("Settings.refresh-time");
			config.set("Settings.refresh-time", 300);
			x = true;
		}
		if (!ck.contains("Pool-Settings.usepool")) {
			config.createSection("Pool-Settings.usepool");
			if (ck.contains("MySQL.usepool")) {
				config.set("Pool-Settings.usepool", config.getBoolean("MySQL.usepool"));
			} else {
				config.set("Pool-Settings.usepool", true);
			}
			x = true;
		}
		if (!ck.contains("SQLite.address")) {
			config.createSection("SQLite.address");
			config.set("SQLite.address", "Default");
			x = true;
		}
		return x;
	}
}
