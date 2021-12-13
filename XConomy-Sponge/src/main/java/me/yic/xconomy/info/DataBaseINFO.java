/*
 *  This file (ServerINFO.java) is a part of project XConomy
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.xconomy.info;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.lang.MessagesManager;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataBaseINFO {

    public static ConfigurationNode DataBaseINFO;
    public static boolean canasync = false;

    public static void load() {
        Path configpath =  Paths.get(XConomy.getInstance().configDir + System.getProperty("file.separator") + "database.yml");
        if(!Files.exists(configpath)){
            try{
                URL configurl = new URL(XConomy.jarPath + "!/database.yml");
                Files.copy(configurl.openStream(), configpath);
            }catch(IOException e){
                e.printStackTrace();
            }

        }

        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setPath(configpath).build();
        try {
            DataBaseINFO = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (getStorageType() != 1){
            canasync = !DataBaseINFO.getNode("Settings","disable-async").getBoolean();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static int getStorageType() {
        if (DataBaseINFO.getNode("Settings","storage-type").getString().equalsIgnoreCase("MySQL")) {
            return 2;
        }
        return 1;
    }

    public static boolean isMySQL() {
        return getStorageType() == 2;
    }

    public static String gethost() {
        if (getStorageType() == 1) {
            return DataBaseINFO.getNode("SQLite","path").getString();
        }else if (getStorageType() == 2) {
            return DataBaseINFO.getNode("MySQL","host").getString();
        }
        return "";
    }

    public static String getuser() {
        if (getStorageType() == 2) {
            return DataBaseINFO.getNode("MySQL","user").getString();
        }
        return "";
    }

    public static String getpass() {
        if (getStorageType() == 2) {
            return DataBaseINFO.getNode("MySQL","pass").getString();
        }
        return "";
    }

    public static String gettablesuffix() {
        if (getStorageType() == 2) {
            return DataBaseINFO.getNode("MySQL","table-suffix").getString();
        }
        return "";
    }


    @SuppressWarnings("ConstantConditions")
    public static String geturl() {
        if (getStorageType() == 2) {
            String url =  "jdbc:mysql://" + DataBaseINFO.getNode("MySQL","host").getString()
                    + ":" + DataBaseINFO.getNode("MySQL","port").getString() + "/"
                    + DataBaseINFO.getNode("MySQL","database").getString() + "?characterEncoding="
                    + DataBaseINFO.getNode("MySQL","property","encoding").getString() + "&useSSL="
                    + DataBaseINFO.getNode("MySQL","property","usessl").getString();
            if (DataBaseINFO.getNode("MySQL","property","timezone").getString() != null &&
                    !DataBaseINFO.getNode("MySQL","property","timezone").getString().equals("")) {
                url = url + "&serverTimezone=" + DataBaseINFO.getNode("MySQL","property","timezone").getString();
            }
            if (DataBaseINFO.getNode("MySQL","property","allowPublicKeyRetrieval").getBoolean()) {
                url = url + "&allowPublicKeyRetrieval=true";
            }
            return url;
        }
        return "";
    }

    public static void loggersysmess(String tag) {
        String mess = MessagesManager.systemMessage(tag);
        switch (getStorageType()) {
            case 1:
                XConomy.getInstance().logger(null, mess.replace("%type%", "SQLite"));
                break;
            case 2:
                XConomy.getInstance().logger(null, mess.replace("%type%", "MySQL"));
                break;
        }
    }

}