/*
 *  This file (Updater.java) is a part of project XConomy
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
package me.yic.xconomy.task;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.PluginINFO;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class Updater implements Runnable {

    public static boolean old = false;
    public static String newVersion = "none";

    @Override
    public void run() {
        try {
            URL url = new URL("https://ore.spongepowered.org/api/v1/projects/xconomy");
            URLConnection conn = url.openConnection();
            conn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT)");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
                    .setSource(() -> reader).build();
            newVersion = loader.load().getNode("recommended", "name").getString();
            is.close();

            List<String> versionList = Arrays.asList(newVersion.split("\\."));
            List<String> newVersionList = Arrays.asList(PluginINFO.VERSION.split("\\."));

            if (!compare(versionList, newVersionList)) {
                XConomy.getInstance().logger("已是最新版本", 0, null);
                return;
            }

            XConomy.getInstance().logger("发现新版本 ", 0, newVersion);
            XConomy.getInstance().logger(null, 0, "https://ore.spongepowered.org/YiC/XConomy");

            if (XConomy.Config.LANGUAGE.equalsIgnoreCase("Chinese")
                    | XConomy.Config.LANGUAGE.equalsIgnoreCase("ChineseTW")) {
                XConomy.getInstance().logger(null, 0, "https://www.mcbbs.net/thread-962904-1-1.html");
            }


        } catch (Exception exception) {
            XConomy.getInstance().logger("检查更新失败", 0, null);
        }
    }

    private static boolean compare(List<String> web, List<String> pl) {
        int v1 = 0;
        int v2 = 0;

        for (int i = 0; i < 5; i++) {
            if (web.size() >= i + 1) {
                v1 = Integer.parseInt(web.get(i));
            }

            if (pl.size() >= i + 1) {
                v2 = Integer.parseInt(pl.get(i));
            }

            if (v1 != (v2)) {
                break;
            }
        }

        int result = Integer.compare(v1 - v2, 0);
        if (result > 0) {
            old = true;
            return true;
        } else {
            return false;
        }

    }

}
