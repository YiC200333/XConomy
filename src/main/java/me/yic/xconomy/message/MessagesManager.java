package me.yic.xconomy.message;

import me.yic.xconomy.XConomy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MessagesManager {
    private final XConomy plugin;
    public static FileConfiguration messageFile;

    public MessagesManager(XConomy plugin) {
        this.plugin = plugin;
    }

    public void load() {
        String jarPath = "jar:file:" + this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        Reader reader = null;
        try {
            URL url = new URL(jarPath + "!/lang/" + XConomy.getInstance().lang().toLowerCase() + ".yml");
            InputStream is = url.openStream();
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                URL url = new URL(jarPath + "!/lang/english.yml");
                InputStream is = url.openStream();
                reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            } catch (IOException ioException) {
                XConomy.getInstance().getLogger().info("System languages file read error");
                ioException.printStackTrace();
            }
        }
        if (reader == null) {
            XConomy.getInstance().getLogger().info("System languages file read error");
            return;
        }
        Messages.langFile = YamlConfiguration.loadConfiguration(reader);

        File mfile = new File(XConomy.getInstance().getDataFolder(), "message.yml");
        boolean translate = false;
        if (!mfile.exists()) {
            try {
                if (!mfile.createNewFile()) {
                    plugin.logger("create-language-file-fail", null);
                }
                translate = true;
                plugin.logger("create-language-file-success", null);
            } catch (IOException e) {
                e.printStackTrace();
                plugin.logger("create-language-file-fail", null);
            }
        }

        messageFile = YamlConfiguration.loadConfiguration(mfile);
        Languages.compare(plugin.lang(), mfile);
        if (translate) {
            Languages.translatorName(plugin.lang(), mfile);
        }
    }

    public static String getAuthor() {
        if (XConomy.getInstance().lang().equalsIgnoreCase("Chinese")
                | XConomy.getInstance().lang().equalsIgnoreCase("ChineseTW")) {
            return "伊C";
        } else {
            return "YiC";
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String getTranslatorS() {
        String trm = Messages.langFile.getString("translation-author");
        if (!trm.equalsIgnoreCase("None")) {
            return trm;
        } else {
            return null;
        }
    }
}
