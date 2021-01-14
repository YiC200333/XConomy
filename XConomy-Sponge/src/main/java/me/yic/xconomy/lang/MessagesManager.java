package me.yic.xconomy.lang;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.ServerINFO;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MessagesManager {
    private final XConomy plugin;
    public static ConfigurationNode messageFile;
    public static ConfigurationNode langFile;
    public static ConfigurationLoader<CommentedConfigurationNode> loader;

    public MessagesManager(XConomy plugin) {
        this.plugin = plugin;
    }

    public void load(){
        try {
            URL url = new URL(XConomy.jarPath + "!/lang/" + ServerINFO.Lang.toLowerCase() + ".yml");
            YAMLConfigurationLoader sysloader = YAMLConfigurationLoader.builder().setURL(url).build();
            langFile = sysloader.load();
        } catch (IOException e) {
            try {
                URL url = new URL(XConomy.jarPath + "!/lang/english.yml");
                YAMLConfigurationLoader sysloader = YAMLConfigurationLoader.builder().setURL(url).build();
                langFile = sysloader.load();
            } catch (IOException ioException) {
                XConomy.getInstance().logger(null,"System languages file read error");
                ioException.printStackTrace();
            }
        }

        Path mfile =  Paths.get(XConomy.getInstance().configDir + "/message.json");
        boolean translate = false;
        loader = HoconConfigurationLoader.builder().setPath(mfile).build();

        if (!Files.exists(mfile)) {
            loader.createEmptyNode();
                translate = true;
                plugin.logger("已创建一个新的语言文件", null);
        }

        try {
            messageFile = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LanguagesManager.compare(ServerINFO.Lang);
        if (translate) {
            LanguagesManager.translatorName(ServerINFO.Lang, mfile.toFile());
        }
    }

    public static String getAuthor() {
        if (ServerINFO.Lang.equalsIgnoreCase("Chinese")
                | ServerINFO.Lang.equalsIgnoreCase("ChineseTW")) {
            return "伊C";
        } else {
            return "YiC";
        }
    }

    public static String systemMessage(String message) {
        return langFile.getNode(Messages.gettag(message)).getString();
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
