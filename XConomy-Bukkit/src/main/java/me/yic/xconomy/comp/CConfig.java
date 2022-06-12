package me.yic.xconomy.comp;

import me.yic.xconomy.XConomy;
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
public class CConfig {
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
        } catch (Exception e) {
            e.printStackTrace();
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

    public CConfig(String path){
        FileConfiguration pfc = null;

        String jarPath = "jar:file:" + XConomy.getInstance().getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        Reader reader = null;
        InputStream is = null;
        try {
            URL url = new URL(jarPath + path);
            is = url.openStream();
            reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            try {
                URL url = new URL(jarPath + "!/lang/english.yml");
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

    public boolean contains(String ... path){
        return fc.contains(path[0]);
    }

    public void createSection(String ... path){
        fc.createSection(path[0]);
    }

    public void set(Object value, String... path){
        fc.set(path[0], value);
    }

    public void save(File f) throws IOException {
        fc.save(f);
    }

    public String getString(String ... path){
        return fc.getString(path[0]);
    }

    public Integer getInt(String ... path){
        return fc.getInt(path[0]);
    }

    public boolean getBoolean(String ... path){
        return fc.getBoolean(path[0]);
    }

    public double getDouble(String ... path){
        return fc.getDouble(path[0]);
    }

    public long getLong(String ... path){
        return fc.getLong(path[0]);
    }

    public List<String> getStringList(String ... path){
        return fc.getStringList(path[0]);
    }

    @SuppressWarnings("ConstantConditions")
    public LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String ... path){
        LinkedHashMap<BigDecimal, String> ks = new LinkedHashMap<>();
        try {
            ConfigurationSection section = fc.getConfigurationSection(path[0]);
            section.getKeys(false).stream().map(BigDecimal::new).sorted().forEach(key -> ks.put(key, getString(path[0] + "." + key)));
        } catch (Exception e) {
            return null;
        }
        return ks;
    }
}
