/*
 *  This file (DataLink.java) is a part of project XConomy
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
package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.adapter.iDataLink;
import me.yic.xconomy.data.redis.RedisThread;
import me.yic.xconomy.data.sql.*;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.utils.RedisConnection;
import me.yic.xconomy.utils.UUIDMode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class DataLink implements iDataLink {

    @Override
    public boolean create() {
        switch (XConomyLoad.DConfig.getStorageType()) {
            case 1:
                XConomy.getInstance().logger("数据保存方式", 0, " - SQLite");
                SQLSetup.setupSqLiteAddress();

                File dataFolder = XConomy.getInstance().getPDataFolder();
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    XConomy.getInstance().logger("文件夹创建异常", 1, null);
                    return false;
                }
                break;

            case 2:
                XConomy.getInstance().logger("数据保存方式", 0, " - MySQL");
                SQLSetup.setupMySqlTable();
                break;

        }

        if (SQL.con()) {
            if (XConomyLoad.DConfig.getStorageType() == 2) {
                SQL.getwaittimeout();
            }
            SQL.createTable();
            SQLUpdateTable.updataTable_record();
            XConomyLoad.DConfig.loggersysmess("连接正常");
        } else {
            XConomyLoad.DConfig.loggersysmess("连接异常");
            return false;
        }


        if (XConomyLoad.DConfig.CacheType().equalsIgnoreCase("Redis")) {
            if (RedisConnection.connectredis()) {
                RedisThread rThread = new RedisThread();
                rThread.start();
            } else {
                return false;
            }
        }

        XConomy.getInstance().logger("XConomy加载成功", 0, null);
        return true;
    }

    @Override
    public CPlayer getplayer(PlayerData pd) {
        Optional<Object> p = Optional.empty();
        if (pd != null) {
            if (XConomyLoad.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)){
                p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(pd.getName()));
            }else{
                p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(pd.getUniqueId()));
            }
        }
        return new CPlayer((User) p.orElse(null));
    }

    @Override
    public void updatelogininfo(UUID uid) {
        if (XConomyLoad.DConfig.canasync) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> SQLLogin.updatelogininfo(uid));
        } else {
            SQLLogin.updatelogininfo(uid);
        }
    }

    @Override
    public void selectlogininfo(CPlayer pp) {
        if (XConomyLoad.DConfig.canasync) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> SQLLogin.getPlayerlogin(pp));
        } else {
            SQLLogin.getPlayerlogin(pp);
        }
    }


    @Override
    public void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> {
                    if (targettype.equalsIgnoreCase("all")) {
                        SQL.saveall(targettype, null, amount, isAdd, ri);
                    } else if (targettype.equalsIgnoreCase("online")) {
                        List<UUID> ol = new ArrayList<>();
                        for (Player pp : Sponge.getServer().getOnlinePlayers()) {
                            ol.add(pp.getUniqueId());
                        }
                        SQL.saveall(targettype, ol, amount, isAdd, ri);
                    }
                }
        );
    }

}
