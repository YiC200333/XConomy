/*
 *  This file (XConomyAPI.java) is a part of project XConomy
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
package me.yic.xconomy.api;

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.DataLink;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.syncdata.PlayerData;
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.info.SyncChannalType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("unused")
public class XConomyAPI {

    public String getversion() {
        return XConomy.PVersion;
    }

    @Deprecated
    public boolean isbungeecordmode() {
        return XConomyLoad.Config.SYNCDATA_TYPE.equals(SyncChannalType.BUNGEECORD);
    }

    public SyncChannalType getSyncChannalType() {
        return XConomyLoad.Config.SYNCDATA_TYPE;
    }

    public BigDecimal formatdouble(String amount) {
        return DataFormat.formatString(amount);
    }

    public String getdisplay(BigDecimal balance) {
        return DataFormat.shown(balance);
    }

    public boolean createPlayerData(UUID uid, String name) {
        return DataLink.newPlayer(uid, name);
    }

    public PlayerData getPlayerData(UUID uid) {
        return DataCon.getPlayerData(uid);
    }

    public PlayerData getPlayerData(String name) {
        return DataCon.getPlayerData(name);
    }

    public BigDecimal getorcreateAccountBalance(String account) {
        return DataCon.getAccountBalance(account);
    }

    public boolean ismaxnumber(BigDecimal amount) {
        return DataFormat.isMAX(amount);
    }


    public int changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd) {
        return changePlayerBalance(u, playername, amount, isadd, null);
    }

    public int changePlayerBalance(UUID u, String playername, BigDecimal amount, Boolean isadd, String pluginname) {
        if (XConomyLoad.getSyncData_Enable() & AdapterManager.BanModiftyBalance()) {
            return 1;
        }
        BigDecimal bal = getPlayerData(u).getBalance();
        if (isadd != null) {
            if (isadd) {
                if (ismaxnumber(bal.add(amount))) {
                    return 3;
                }
            } else {
                if (bal.compareTo(amount) < 0) {
                    return 2;
                }
            }
        }
        DataCon.changeplayerdata("PLUGIN_API", u, amount, isadd, pluginname, null);
        return 0;
    }

    public int changeAccountBalance(String account, BigDecimal amount, Boolean isadd) {
        return changeAccountBalance(account, amount, isadd, null);
    }

    public int changeAccountBalance(String account, BigDecimal amount, Boolean isadd, String pluginname) {
        BigDecimal bal = getorcreateAccountBalance(account);
        if (isadd != null) {
            if (isadd) {
                if (ismaxnumber(bal.add(amount))) {
                    return 3;
                }
            } else {
                if (bal.compareTo(amount) < 0) {
                    return 2;
                }
            }
        }
        DataCon.changeaccountdata("PLUGIN_API", account, amount, isadd, pluginname);
        return 0;
    }

    public List<String> getbalancetop() {
        return Cache.baltop_papi;
    }

    public BigDecimal getsumbalance() {
        return Cache.sumbalance;
    }

    public boolean getglobalpermission(String permission) {
        if(permission.equalsIgnoreCase("pay")){
            return PermissionINFO.getGlobalPayment();
        }
        return true;
    }

    public void setglobalpermission(String permission, boolean vaule) {
        PermissionINFO.globalpayment = vaule;
    }

    public Boolean getpaymentpermission(UUID uid) {
        return PermissionINFO.getPaymentPermission(uid);
    }

    public void setpaymentpermission(UUID uid, Boolean vaule) {
        PermissionINFO.setPaymentPermission(uid, vaule);
    }

    public Boolean getpaytoggle(UUID uid) {
        return PermissionINFO.getRPaymentPermission(uid);
    }

    public void setpaytoggle(UUID uid, boolean vaule) {
        PermissionINFO.setRPaymentPermission(uid, vaule);
    }

}
