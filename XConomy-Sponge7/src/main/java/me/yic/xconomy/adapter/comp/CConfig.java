package me.yic.xconomy.adapter.comp;

import com.google.common.reflect.TypeToken;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings({"unused", "ConfusingArgumentToVarargsMethod"})
public class CConfig implements iConfig {
    private final ConfigurationNode fc;
    private ConfigurationLoader<ConfigurationNode> fcloader;

    public CConfig(ConfigurationNode fc){
        this.fc = fc;
    }

    public CConfig(File f){
        ConfigurationNode pfc = null;

        fcloader = YAMLConfigurationLoader.builder().setFlowStyle(DumperOptions.FlowStyle.BLOCK).setIndent(2).setFile(f).build();
        try {
            pfc = fcloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fc = pfc;
    }

    public CConfig(URL url){
        ConfigurationNode pfc = null;

        StringBuilder uuid = new StringBuilder();
        try {
            YAMLConfigurationLoader proloader = YAMLConfigurationLoader.builder().setURL(url).build();
            pfc = proloader.load();
        } catch (Exception ignored) {
            //e.printStackTrace();
        }
        fc = pfc;
    }

    public CConfig(String path, String subpath){
        ConfigurationNode pfc = null;

        try {
            URL url = new URL(XConomy.jarPath + "!" + path + subpath);
            InputStream is = url.openStream();
            is.close();
            YAMLConfigurationLoader sysloader = YAMLConfigurationLoader.builder().setURL(url).build();
            pfc = sysloader.load();
        } catch (IOException e) {
            try {
                URL url = new URL(XConomy.jarPath + "!" + path + "/english.yml");
                InputStream is = url.openStream();
                is.close();
                YAMLConfigurationLoader sysloader = YAMLConfigurationLoader.builder().setURL(url).build();
                pfc = sysloader.load();
            } catch (IOException ioException) {
                XConomy.getInstance().logger(null, 1, "System languages file read error");
                ioException.printStackTrace();
            }
        }
        fc = pfc;
    }

    @Override
    public ConfigurationNode getConfig(){
        return fc;
    }


    @Override
    public boolean contains(String path){
        String[] a = path.split("\\.");
        return !fc.getNode(a).isVirtual();
    }

    @Override
    public void createSection(String path){
    }

    @Override
    public void set(String path, Object value){
        String[] a = path.split("\\.");
        fc.getNode(a).setValue(value);
    }

    @Override
    public void save(File f) throws IOException {
        fcloader.save(fc);
    }

    @Override
    public String getString(String path){
        String[] a = path.split("\\.");
        return fc.getNode(a).getString();
    }

    @Override
    public Integer getInt(String path){
        String[] a = path.split("\\.");
        return fc.getNode(a).getInt();
    }

    @Override
    public boolean getBoolean(String path){
        String[] a = path.split("\\.");
        return fc.getNode(a).getBoolean();
    }

    @Override
    public double getDouble(String path){
        String[] a = path.split("\\.");
        return fc.getNode(a).getDouble();
    }

    @Override
    public long getLong(String path){
        String[] a = path.split("\\.");
        return fc.getNode(a).getLong();
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public List<String> getStringList(String path){
        String[] a = path.split("\\.");
        try {
            return fc.getNode(a).getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String path){
        LinkedHashMap<BigDecimal, String> ks = new LinkedHashMap<>();
        String[] a = path.split("\\.");
        try {
            ConfigurationNode section = fc.getNode(a);
            section.getChildrenMap().entrySet().stream().sorted(Comparator.comparingInt(key -> Integer.parseInt(key.getKey().toString())))
                    .forEach(key -> ks.put(new BigDecimal(key.getKey().toString()), key.getValue().getString()));

        } catch (Exception e) {
            return null;
        }
        return ks;
    }
}
