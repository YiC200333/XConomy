package me.Yi.XConomy.Data;

import java.io.File;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import me.Yi.XConomy.XConomy;

public class Convert extends XConomy {

	public static File userdata;
	public static File uiddata;
	public static File topdata;
	public static File nonpdata;
	public static FileConfiguration pd;
	public static FileConfiguration pdu;
	public static FileConfiguration pdnon;
	public static FileConfiguration top;

	public static void conv(File dataFolder) {
		userdata = new File(dataFolder, "data.yml");
		uiddata = new File(dataFolder, "uid.yml");
		topdata = new File(dataFolder, "baltop.yml");
		nonpdata = new File(dataFolder, "datanon.yml");
		pd = YamlConfiguration.loadConfiguration(userdata);
		pdu = YamlConfiguration.loadConfiguration(uiddata);
		pdnon = YamlConfiguration.loadConfiguration(nonpdata);
		top = YamlConfiguration.loadConfiguration(topdata);
		if (userdata.exists() | nonpdata.exists()) {
			if (userdata.exists()) {
				getInstance().logger("Old data file found");
				getInstance().logger("Please wait for data conversion..........");
				ConfigurationSection section1 = pd.getConfigurationSection("");
				for (String u : section1.getKeys(false)) {
					String na = pd.getString(u + ".username");
					Double dd = Double.valueOf(pd.getString(u + ".balance"));
					SQL.converty(u, na, dd);
				}
			}
			if (config.getBoolean("Settings.non-player-account")) {
				if (nonpdata.exists()) {
					getInstance().logger("Old non-player data file found");
					getInstance().logger("Please wait for data conversion..........");
					ConfigurationSection section2 = pdnon.getConfigurationSection("");
					for (String n : section2.getKeys(false)) {
						Double dd = Double.valueOf(pdnon.getString(n + ".balance"));
						SQL.convertnon(n, dd);

					}
				}
			}
			File ndataFolder = new File(dataFolder, "old");
			ndataFolder.mkdirs();
			File userdatan = new File(ndataFolder, "data.yml");
			File nonpdatan = new File(ndataFolder, "datanon.yml");
			File topdatan = new File(ndataFolder, "baltop.yml");
			File uiddatan = new File(ndataFolder, "uid.yml");
			if (userdata.exists() & !userdatan.exists()) {
				userdata.renameTo(userdatan);
			}
			if (nonpdata.exists() & !nonpdatan.exists()) {
				nonpdata.renameTo(nonpdatan);
			}
			if (topdata.exists() & !topdatan.exists()) {
				topdata.renameTo(topdatan);
			}
			if (uiddata.exists() & !uiddatan.exists()) {
				uiddata.renameTo(uiddatan);
			}
			getInstance().logger("Data conversion complete");
		}
	}
}
