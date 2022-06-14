package me.yic.xconomy.comp;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("unused")
public class CConfig {

    public CConfig(File f){
    }

    public CConfig(URL url){

    }

    public CConfig(String path){

    }

    public boolean contains(String path){
        return false;
    }

    public void createSection(String path){
    }

    public void set(String path, Object value){
    }

    public void save(File f) throws IOException {
    }

    public String getString(String path){
        return "";
    }

    public Integer getInt(String path){
        return 0;
    }

    public boolean getBoolean(String path){
        return false;
    }

    public double getDouble(String path){
        return 0.0;
    }

    public long getLong(String path){
        return 0L;
    }

    public List<String> getStringList(String path){
        return null;
    }

    public LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String path){
        return null;
    }
}
