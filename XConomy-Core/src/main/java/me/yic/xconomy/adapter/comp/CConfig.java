package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.adapter.iConfig;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;


@SuppressWarnings("unused")
public class CConfig implements iConfig {

    public CConfig(File f){
    }

    public CConfig(URL url){
    }

    public CConfig(String path){
    }

    @Override
    public Object getConfig() {
        return null;
    }

    @Override
    public boolean contains(String path) {
        return false;
    }

    @Override
    public void createSection(String path) {

    }

    @Override
    public void set(String path, Object value) {

    }

    @Override
    public void save(File f) throws IOException {

    }

    @Override
    public String getString(String path) {
        return null;
    }

    @Override
    public Integer getInt(String path) {
        return null;
    }

    @Override
    public boolean getBoolean(String path) {
        return false;
    }

    @Override
    public double getDouble(String path) {
        return 0;
    }

    @Override
    public long getLong(String path) {
        return 0;
    }

    @Override
    public List<String> getStringList(String path) {
        return null;
    }

    @Override
    public LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String path) {
        return null;
    }
}
