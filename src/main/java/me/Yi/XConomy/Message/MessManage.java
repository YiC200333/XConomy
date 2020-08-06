package me.Yi.XConomy.Message;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Yi.XConomy.XConomy;

public class MessManage {
	public static FileConfiguration mess;
	XConomy plugin;

	public MessManage(XConomy instance) {
		this.plugin = instance;
	}

	public void load() {
		File f = new File(XConomy.getInstance().getDataFolder(), "message.yml");
		boolean ft = false;
		if (!f.exists()) {
			try {
				f.createNewFile();
				ft = true;
				plugin.logger("已创建一个新的语言文件");
			} catch (IOException e) {
				e.printStackTrace();
				plugin.logger("语言文件创建异常");
			}
		}
		mess = YamlConfiguration.loadConfiguration(f);
		Langs.compare(plugin.lang(), f);
		if (ft) {
			Messages.tranname(plugin.lang(), f);
		}
	}
}
