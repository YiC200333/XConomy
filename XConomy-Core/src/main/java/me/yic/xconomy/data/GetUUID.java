/*
 *  This file (GetUUID.java) is a part of project XConomy
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
package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.comp.CConfig;
import me.yic.xconomy.comp.CPlayer;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GetUUID {
    private static final Map<String, UUID> cache = new ConcurrentHashMap<>();

    private static String doGetUUID(CPlayer pp, String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        CConfig pro;
        StringBuilder uuid = new StringBuilder();
        try {
            pro = new CConfig(new URL(url));
            String u = pro.getString("id");
            for (int i = 0; i <= 31; i++) {
                uuid.append(u.charAt(i));
                if (i == 7 || i == 11 || i == 15 || i == 19) {
                    uuid.append("-");
                }
            }
        }catch (Exception e){
            kickplayer(pp);
        }
        return uuid.toString();
    }

    private static void kickplayer(CPlayer pp) {
        if (pp != null && pp.isOnline()) {
            pp.kickPlayer("Failed to Get profile");
        }
    }

    public static UUID getUUID(CPlayer pp, String name) {
        UUID u = null;
        switch (XConomy.Config.UUIDMODE) {
            case ONLINE:
                if (CacheContainsKey(name)) {
                    if (pp.getUniqueId().toString().equalsIgnoreCase(getUUIDFromCache(name).toString())) {
                        return getUUIDFromCache(name);
                    }
                }


                try {
                    u = UUID.fromString(doGetUUID(pp, name));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                if (u != null) {
                    insertIntoCache(name, u);
                }
                break;
            case OFFLINE:
                u = getOfflineUUID(name);
                break;
        }
            return u;
        }

        private static UUID getOfflineUUID (String name){
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
        }

        private static void insertIntoCache ( final String name, final UUID uuid){
            if (XConomy.Config.USERNAME_IGNORE_CASE) {
                cache.put(name.toLowerCase(), uuid);
            } else {
                cache.put(name, uuid);
            }
        }

        private static boolean CacheContainsKey ( final String name){
            if (XConomy.Config.USERNAME_IGNORE_CASE) {
                return cache.containsKey(name.toLowerCase());
            }
            return cache.containsKey(name);
        }


        private static UUID getUUIDFromCache ( final String name){
            if (XConomy.Config.USERNAME_IGNORE_CASE) {
                return cache.get(name.toLowerCase());
            } else {
                return cache.get(name);
            }
        }

        public static void removeUUIDFromCache(final String name){
            if (CacheContainsKey(name)) {
                if (XConomy.Config.USERNAME_IGNORE_CASE) {
                    cache.remove(name.toLowerCase());
                } else {
                    cache.remove(name);
                }
            }
        }
    }

