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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.PermissionINFO;
import me.yic.xconomy.info.ServerINFO;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@SuppressWarnings("unused")
public class XConomyAPI {

    public String getversion() {
        return XConomy.getInstance().getDescription().getVersion();
    }

    public boolean isbungeecordmode() {
        return ServerINFO.IsBungeeCordMode;
    }

    public UUID translateUUID(String playername) {
        return Cache.translateUUID(playername, null);
    }

    public BigDecimal formatdouble(String amount) {
        return DataFormat.formatString(amount);
    }

    public String getdisplay(BigDecimal balance) {
        return DataFormat.shown(balance);
    }

    public BigDecimal getbalance(UUID uid) {
        return Cache.getBalanceFromCacheOrDB(uid);
    }

    public boolean ismaxnumber(BigDecimal amount) {
        return DataFormat.isMAX(amount);
    }

    public int changebalance(UUID u, String playername, BigDecimal amount, boolean isadd) {
        if (ServerINFO.IsBungeeCordMode & Bukkit.getOnlinePlayers().isEmpty()) {
            return 1;
        }
        BigDecimal bal = getbalance(u);
        if (isadd) {
            if (ismaxnumber(bal.add(amount))) {
                return 3;
            }
        } else {
            if (bal.compareTo(amount) < 0) {
                return 2;
            }
        }
        Cache.change("PLUGIN_API", u, playername, amount, isadd, "N/A");
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
}
