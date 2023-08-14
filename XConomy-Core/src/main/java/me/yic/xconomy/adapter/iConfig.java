package me.yic.xconomy.adapter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("unused")
public interface iConfig {

    Object getConfig();

    boolean contains(String path);

    void createSection(String path);

    void set(String path, Object value);

    void save() throws Exception;

    String getString(String path);

    Integer getInt(String path);

    boolean getBoolean(String path);

    double getDouble(String path);

    long getLong(String path);

    List<String> getStringList(String path);

    LinkedHashMap<BigDecimal, String> getConfigurationSectionSort(String path);
}
