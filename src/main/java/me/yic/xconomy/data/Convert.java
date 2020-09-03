package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Convert extends XConomy {

	public static File userData;
	public static File uidData;
	public static File baltopData;
	public static File nonPlayerData;
	public static FileConfiguration userFile;
	public static FileConfiguration uidFile;
	public static FileConfiguration baltopFile;
	public static FileConfiguration nonPlayerFile;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void convert(File dataFolder) {
		userData = new File(dataFolder, "data.yml");
		uidData = new File(dataFolder, "uid.yml");
		baltopData = new File(dataFolder, "baltop.yml");
		nonPlayerData = new File(dataFolder, "datanon.yml");
		userFile = YamlConfiguration.loadConfiguration(userData);
		uidFile = YamlConfiguration.loadConfiguration(uidData);
		nonPlayerFile = YamlConfiguration.loadConfiguration(nonPlayerData);
		baltopFile = YamlConfiguration.loadConfiguration(baltopData);

		if (userData.exists() | nonPlayerData.exists()) {
			if (userData.exists()) {
				getInstance().logger("Old data file found");
				getInstance().logger("Please wait for data conversion..........");
				ConfigurationSection section1 = userFile.getConfigurationSection("");
				for (String u : section1.getKeys(false)) {
					String na = userFile.getString(u + ".username");
					Double dd = Double.valueOf(userFile.getString(u + ".balance"));
					SQL.convertData(u, na, dd);
				}
			}
			if (config.getBoolean("Settings.non-player-account")) {
				if (nonPlayerData.exists()) {
					getInstance().logger("Old non-player data file found");
					getInstance().logger("Please wait for data conversion..........");
					ConfigurationSection section2 = nonPlayerFile.getConfigurationSection("");
					for (String n : section2.getKeys(false)) {
						Double dd = Double.valueOf(nonPlayerFile.getString(n + ".balance"));
						SQL.convertNonPlayerData(n, dd);

					}
				}
			}

			File oldDataFOlder = new File(dataFolder, "old");
			oldDataFOlder.mkdirs();

			File oldUserData = new File(oldDataFOlder, "data.yml");
			File oldUidData = new File(oldDataFOlder, "uid.yml");
			File oldBaltopData = new File(oldDataFOlder, "baltop.yml");
			File oldNonPlayerData = new File(oldDataFOlder, "datanon.yml");

			if (userData.exists() & !oldUserData.exists()) {
				userData.renameTo(oldUserData);
			}
			if (nonPlayerData.exists() & !oldNonPlayerData.exists()) {
				nonPlayerData.renameTo(oldNonPlayerData);
			}
			if (baltopData.exists() & !oldBaltopData.exists()) {
				baltopData.renameTo(oldBaltopData);
			}
			if (uidData.exists() & !oldUidData.exists()) {
				uidData.renameTo(oldUidData);
			}

			getInstance().logger("Data conversion complete");
		}
	}
}
