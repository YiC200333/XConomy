package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iConfig;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

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
    private final YamlConfigurationLoader fcloader;

    public CConfig(ConfigurationNode fc) {
        this.fcloader = null;
        this.fc = fc;
    }

    public CConfig(File f) {
        ConfigurationNode pfc = null;

        fcloader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2).file(f).build();
        try {
            pfc = fcloader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fc = pfc;
    }

    public CConfig(URL url) {
        this.fcloader = null;
        ConfigurationNode pfc = null;

        StringBuilder uuid = new StringBuilder();
        try {
            YamlConfigurationLoader proloader = YamlConfigurationLoader.builder().url(url).build();
            pfc = proloader.load();
        } catch (Exception ignored) {
            //e.printStackTrace();
        }
        fc = pfc;
    }

    @SuppressWarnings("ConstantConditions")
    public CConfig(String path, String subpath) {
        this.fcloader = null;
        ConfigurationNode pfc = null;

        try {
            URL url = this.getClass().getClassLoader().getResource(path + subpath);
            InputStream is = url.openStream();
            is.close();
            YamlConfigurationLoader sysloader = YamlConfigurationLoader.builder().url(url).build();
            pfc = sysloader.load();
        } catch (IOException e) {
            try {
                URL url = this.getClass().getClassLoader().getResource(path + "/english.yml");
                InputStream is = url.openStream();
                is.close();
                YamlConfigurationLoader sysloader = YamlConfigurationLoader.builder().url(url).build();
                pfc = sysloader.load();
            } catch (IOException ioException) {
                XConomy.getInstance().logger(null, 1, "System languages file read error");
                ioException.printStackTrace();
            }
        }
        fc = pfc;
    }

    @Override
    public ConfigurationNode getConfig() {
        return fc;
    }


    @Override
    public boolean contains(String path) {
        String[] a = path.split("\\.");
        return !fc.node(a).virtual();
    }

    @Override
    public void createSection(String path) {
    }

    @Override
    public void set(String path, Object value) {
        String[] a = path.split("\\.");
        try {
            fc.node(a).set(value);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getString(String path) {
        String[] a = path.split("\\.");
        return fc.node(a).getString();
    }

    @Override
    public Integer getInt(String path) {
        String[] a = path.split("\\.");
        return fc.node(a).getInt();
    }

    @Override
    public boolean getBoolean(String path) {
        String[] a = path.split("\\.");
        return fc.node(a).getBoolean();
    }

    @Override
    public double getDouble(String path) {
        String[] a = path.split("\\.");
        return fc.node(a).getDouble();
    }

    @Override
    public long getLong(String path) {
        String[] a = path.split("\\.");
        return fc.node(a).getLong();
    }

    @Override
    public void save() throws Exception {
        if (fcloader == null) {
            throw new Exception("The file is null");
        }
        fcloader.save(fc);
    }

    @Override
    public List<String> getStringList(String path) {
        String[] a = path.split("\\.");
        try {
            return fc.node(a).getList(String.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String path) {
        LinkedHashMap<BigDecimal, String> ks = new LinkedHashMap<>();
        String[] a = path.split("\\.");
        try {
            ConfigurationNode section = fc.node(a);
            section.childrenMap().entrySet().stream().sorted(Comparator.comparingInt(key -> Integer.parseInt(key.getKey().toString())))
                    .forEach(key -> ks.put(new BigDecimal(key.getKey().toString()), key.getValue().getString()));

        } catch (Exception e) {
            return null;
        }
        return ks;
    }
}
