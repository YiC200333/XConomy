package me.yic.xconomy.lang;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.ServerINFO;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MessagesManager {
    private final XConomy plugin;
    public static FileConfiguration messageFile;
    public static FileConfiguration langFile;

    public MessagesManager(XConomy plugin) {
        this.plugin = plugin;
    }

    public void load() {
        String jarPath = "jar:file:" + this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        Reader reader = null;
        try {
            URL url = new URL(jarPath + "!/lang/" + ServerINFO.Lang.toLowerCase() + ".yml");
            InputStream is = url.openStream();
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                URL url = new URL(jarPath + "!/lang/english.yml");
                InputStream is = url.openStream();
                reader = new InputStreamReader(is, StandardCharsets.UTF_8);
                is.close();
            } catch (IOException ioException) {
                XConomy.getInstance().getLogger().info("System languages file read error");
                ioException.printStackTrace();
            }
        }
        if (reader == null) {
            XConomy.getInstance().getLogger().info("System languages file read error");
            return;
        }
        langFile = YamlConfiguration.loadConfiguration(reader);

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
        LanguagesManager.compare(ServerINFO.Lang, mfile);
        if (translate) {
            Languages.translatorName(ServerINFO.Lang, mfile);
        }
    }

    public static String systemMessage(String message) {
        return langFile.getString(Messages.gettag(message));
    }

    public static String getAuthor() {
        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            return "伊C";
        } else {
            return "YiC";
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static String getTranslatorS() {
        String trm = langFile.getString("translation-author");
        if (!trm.equalsIgnoreCase("None")) {
            return trm;
        } else {
            return null;
        }
    }
}
