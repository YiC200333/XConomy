package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.adapter.iConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("unused")
public class CConfig implements iConfig {
    private final FileConfiguration fc;

    public CConfig(FileConfiguration fc){
        this.fc = fc;
    }

    public CConfig(File f){
        this.fc = YamlConfiguration.loadConfiguration(f);
    }

    public CConfig(URL url){
        FileConfiguration pfc = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        Reader br = null;
        StringBuilder uuid = new StringBuilder();
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            if (200 == conn.getResponseCode()) {
                is = conn.getInputStream();
                br = new InputStreamReader(is, StandardCharsets.UTF_8);
                pfc = YamlConfiguration.loadConfiguration(br);
            } else {
                throw new Exception("ResponseCode is an error code:" + conn.getResponseCode());
            }
        } catch (Exception ignored) {
            //e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        fc = pfc;
    }

    public CConfig(String path, String subpath){
        FileConfiguration pfc = null;

        String jarPath = "jar:file:" + XConomy.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        Reader reader = null;
        InputStream is = null;
        try {
            URL url = new URL(jarPath + "!" + path + subpath);
            is = url.openStream();
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                URL url = new URL(jarPath + "!" + path + "/english.yml");
                is = url.openStream();
                reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            } catch (IOException ioException) {
                XConomy.getInstance().getLogger().warning("System languages file read error");
                ioException.printStackTrace();
            }
        }
        if (reader == null) {
            XConomy.getInstance().getLogger().warning("System languages file read error");
            fc = pfc;
            return;
        }
        pfc = YamlConfiguration.loadConfiguration(reader);

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fc = pfc;
    }

    @Override
    public FileConfiguration getConfig() {
        return fc;
    }

    @Override
    public boolean contains(String path){
        return fc.contains(path);
    }

    @Override
    public void createSection(String path){
        fc.createSection(path);
    }

    @Override
    public void set(String path, Object value){
        fc.set(path, value);
    }

    @Override
    public void save(File f) throws IOException {
        fc.save(f);
    }

    @Override
    public String getString(String path){
        return fc.getString(path);
    }

    @Override
    public Integer getInt(String path){
        return fc.getInt(path);
    }

    @Override
    public boolean getBoolean(String path){
        return fc.getBoolean(path);
    }

    @Override
    public double getDouble(String path){
        return fc.getDouble(path);
    }

    @Override
    public long getLong(String path){
        return fc.getLong(path);
    }

    @Override
    public List<String> getStringList(String path){
        return fc.getStringList(path);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String path){
        LinkedHashMap<BigDecimal, String> ks = new LinkedHashMap<>();
        try {
            ConfigurationSection section = fc.getConfigurationSection(path);
            section.getKeys(false).stream().map(BigDecimal::new).sorted().forEach(key -> ks.put(key, getString(path + "." + key)));
        } catch (Exception e) {
            return null;
        }
        return ks;
    }
}
