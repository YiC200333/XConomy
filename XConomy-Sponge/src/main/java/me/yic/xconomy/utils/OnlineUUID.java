/*
 *  This file (OnlineUUID.java) is a part of project XConomy
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
package me.yic.xconomy.utils;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.net.URL;

public class OnlineUUID {
    public static String doGetUUID(Player pp) {
        String URL = "https://api.mojang.com/users/profiles/minecraft/" + pp.getName();
        StringBuilder uuid = new StringBuilder();
        try {
            URL url = new URL(URL);
            YAMLConfigurationLoader proloader = YAMLConfigurationLoader.builder().setURL(url).build();
            ConfigurationNode proFile = proloader.load();
            String u = proFile.getNode("id").getString();
            if (u != null) {
                for (int i = 0; i <= 31; i++) {
                    uuid.append(u.charAt(i));
                    if (i == 7 || i == 11 || i == 15 || i == 19) {
                        uuid.append("-");
                    }
                }
            }
        } catch (Exception e) {
            kickplayer(pp);
            e.printStackTrace();
        }
        return uuid.toString();
    }

    private static void kickplayer(Player pp) {
        if (pp != null) {
            pp.kick(Text.of("Failed to Get profile"));
        }
    }
}

