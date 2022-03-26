/*
 *  This file (CacheSemiOnline.java) is a part of project XConomy
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
package me.yic.xconomy.data.caches;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.utils.UUIDMode;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class CacheSemiOnline {
    public static YAMLConfigurationLoader loader;
    public static ConfigurationNode CacheSubUUID;

    public static boolean createfile() {
        if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
            File dataFolder = new File(XConomy.getInstance().configDir.toFile(), "cache");
            if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                XConomy.getInstance().logger("文件夹创建异常", 1, null);
                return false;
            }
            File cfile = new File(dataFolder, "cache_subuuid.json");
            loader = YAMLConfigurationLoader.builder().setPath(Paths.get(cfile.toURI())).build();

            if (!cfile.exists()) {
                loader.createEmptyNode();
            }

            try {
                CacheSubUUID = loader.load();
                loader.save(CacheSubUUID);
            } catch (IOException e) {
                e.printStackTrace();
                XConomy.getInstance().logger("缓存文件创建异常", 1, null);
                return false;
            }
        }
        return true;
    }


    @SuppressWarnings("ConstantConditions")
    public static void CacheSubUUID_checkUser(String mainu, Player pp) {
        if (!CacheSubUUID.getNode(mainu).isVirtual()) {
            if (!CacheSubUUID.getNode(mainu, "SubUUID").getString().equals(pp.getUniqueId().toString())) {
                if (pp.isOnline()) {
                        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() ->
                            pp.kick(Text.of("[XConomy] The player with the same name exists on the server (Three times)")));
                }
            } else {
                Cache.removefromCache(Cache.getSubUUID(UUID.fromString(mainu)));
                Cache.insertIntoSUUIDCache(UUID.fromString(mainu), pp.getUniqueId());
            }
        } else {
            CacheSubUUID.getNode(mainu, "SubUUID").setValue(pp.getUniqueId().toString());
            Cache.removefromCache(Cache.getSubUUID(UUID.fromString(mainu)));
            Cache.insertIntoSUUIDCache(UUID.fromString(mainu), pp.getUniqueId());
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static UUID CacheSubUUID_getsubuuid(String uuid) {
        if (!CacheSubUUID.getNode(uuid).isVirtual()) {
            return UUID.fromString(CacheSubUUID.getNode(uuid, "SubUUID").getString());
        } else {
            return null;
        }
    }


    public static void save() {
        try {
            if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)) {
                loader.save(CacheSubUUID);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}