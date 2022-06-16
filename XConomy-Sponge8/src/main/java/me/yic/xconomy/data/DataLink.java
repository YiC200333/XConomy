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
package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.comp.CPlayer;
import me.yic.xconomy.data.sql.SQL;
import me.yic.xconomy.data.sql.SQLCreateNewAccount;
import me.yic.xconomy.data.sql.SQLLogin;
import me.yic.xconomy.data.sql.SQLUpdateTable;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.utils.PlayerData;
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
public class DataLink {

    public static boolean create() {
        switch (XConomy.DConfig.getStorageType()) {
            case 1:
                XConomy.getInstance().logger("数据保存方式", 0, " - SQLite");
                setupSqLiteAddress();

                File dataFolder = XConomy.getInstance().getPDataFolder();
                if (!dataFolder.exists() && !dataFolder.mkdirs()) {
                    XConomy.getInstance().logger("文件夹创建异常", 1, null);
                    return false;
                }
                break;

            case 2:
                XConomy.getInstance().logger("数据保存方式", 0, " - MySQL");
                setupMySqlTable();
                break;

        }

        if (SQL.con()) {
            if (XConomy.DConfig.getStorageType() == 2) {
                SQL.getwaittimeout();
            }
            SQL.createTable();
            SQLUpdateTable.updataTable_record();
            XConomy.DConfig.loggersysmess("连接正常");
        } else {
            XConomy.DConfig.loggersysmess("连接异常");
            return false;
        }

        XConomy.getInstance().logger("XConomy加载成功", 0, null);
        return true;
    }

    public static User getplayer(PlayerData pd) {
        Optional<Object> p = Optional.empty();
        if (pd != null) {
            if (XConomy.Config.UUIDMODE.equals(UUIDMode.SEMIONLINE)){
                p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(pd.getName()));
            }else{
                p = Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(pd.getUniqueId()));
            }
        }
        return (User) p.orElse(null);
    }

    public static boolean newPlayer(UUID uid, String name) {
        return SQLCreateNewAccount.newPlayer(uid, name, null);
    }

    public static void newPlayer(Player a) {
        SQLCreateNewAccount.newPlayer(new CPlayer(a));
    }

    public static void updatelogininfo(UUID uid) {
        if (XConomy.DConfig.canasync) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> SQLLogin.updatelogininfo(uid));
        } else {
            SQLLogin.updatelogininfo(uid);
        }
    }

    public static void selectlogininfo(Player pp) {
        if (XConomy.DConfig.canasync) {
            Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(() -> SQLLogin.getPlayerlogin(pp));
        } else {
            SQLLogin.getPlayerlogin(pp);
        }
    }

    public static <T> void getPlayerData(T key) {
        if (key instanceof UUID) {
            SQL.getPlayerData((UUID) key);
        } else if (key instanceof String) {
            SQL.getPlayerData((String) key);
        }
    }

    public static void getBalNonPlayer(String u) {
        SQL.getNonPlayerData(u);
    }

    public static void getTopBal() {
        SQL.getBaltop();
    }

    public static void setTopBalHide(UUID u, int type) {
        SQL.hidetop(u, type);
    }

    public static String getBalSum() {
        if (SQL.sumBal() == null) {
            return "0.0";
        }
        return SQL.sumBal();
    }

    public static void save(PlayerData pd, Boolean isAdd, BigDecimal amount, RecordInfo ri) {
        SQL.save(pd, isAdd, amount, ri);
    }


    public static void saveall(String targettype, BigDecimal amount, Boolean isAdd, RecordInfo ri) {
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

    public static void saveNonPlayer(String account, BigDecimal amount,
                                     BigDecimal newbalance, Boolean isAdd, RecordInfo ri) {
        SQL.saveNonPlayer(account, amount, newbalance, isAdd, ri);
    }

    private static void setupMySqlTable() {
        if (XConomy.DConfig.gettablesuffix() != null & !XConomy.DConfig.gettablesuffix().equals("")) {
            SQL.tableName = "xconomy_" + XConomy.DConfig.gettablesuffix().replace("%sign%", XConomy.Config.BUNGEECORD_SIGN);
            SQL.tableNonPlayerName = "xconomynon_" + XConomy.DConfig.gettablesuffix().replace("%sign%", XConomy.Config.BUNGEECORD_SIGN);
            SQL.tableRecordName = "xconomyrecord_" + XConomy.DConfig.gettablesuffix().replace("%sign%", XConomy.Config.BUNGEECORD_SIGN);
            SQL.tableLoginName = "xconomylogin_" + XConomy.DConfig.gettablesuffix().replace("%sign%", XConomy.Config.BUNGEECORD_SIGN);
            SQL.tableUUIDName = "xconomyuuid_" + XConomy.DConfig.gettablesuffix().replace("%sign%", XConomy.Config.BUNGEECORD_SIGN);
        }
    }

    private static void setupSqLiteAddress() {
        if (XConomy.DConfig.gethost().equalsIgnoreCase("Default")) {
            return;
        }

        File folder = new File(XConomy.DConfig.gethost());
        if (folder.exists()) {
            SQL.database.userdata = new File(folder, "data.db");
        } else {
            XConomy.getInstance().logger("自定义文件夹路径不存在", 1, null);
        }

    }
}
