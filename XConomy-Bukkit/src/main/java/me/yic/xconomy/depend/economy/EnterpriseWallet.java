package me.yic.xconomy.depend.economy;/*
 *  This file (EnterpriseWallet.java) is a part of project XConomy
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

import com.github.sanctum.economy.construct.EconomyAction;
import com.github.sanctum.economy.construct.account.PlayerWallet;
import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

public class EnterpriseWallet extends PlayerWallet {

    public EnterpriseWallet(OfflinePlayer player) {
        super(player);
    }

    @Override
    public EconomyAction setBalance(BigDecimal amount) {
        if (XConomyLoad.getSyncData_Enable() & AdapterManager.BanModiftyBalance()) {
            return new EconomyAction(getHolder(), false,
                    "[BungeeCord] No player in server");
        }

        if (DataFormat.isMAX(amount)) {
            return new EconomyAction(getHolder(), false,  "Max balance!");
        }

        DataCon.changeplayerdata("PLUGIN", getPlayer().getUniqueId(), amount, null, null ,null);
        return new EconomyAction(getHolder(), true,  "");
    }

    @Override
    public EconomyAction setBalance(BigDecimal amount, String s) {
        return setBalance(amount);
    }

    @Override
    public boolean exists() {
        return DataCon.getPlayerData(getPlayer().getUniqueId()) != null;
    }

    @Override
    public boolean exists(String s) {
        return exists();
    }

    @Override
    public @Nullable BigDecimal getBalance() {
        UUID uuid = getPlayer().getUniqueId();
        if (DataCon.getPlayerData(uuid) != null) {
            return DataCon.getPlayerData(uuid).getBalance();
        }
        return BigDecimal.ZERO;
    }

    @Override
    public @Nullable BigDecimal getBalance(String s) {
        return getBalance();
    }

    @Override
    public boolean has(BigDecimal bigDecimal) {
        BigDecimal bal = getBalance();
        if (bal == null){
            return false;
        }
        return getBalance().compareTo(getBalance()) > 0;
    }

    @Override
    public boolean has(BigDecimal bigDecimal, String s) {
        return has(bigDecimal);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public EconomyAction withdraw(BigDecimal amount) {
        if (XConomyLoad.getSyncData_Enable() & AdapterManager.BanModiftyBalance()) {
            return new EconomyAction(getHolder(), false, "[BungeeCord] No player in server");
        }

        BigDecimal bal = getBalance();

        if (bal.compareTo(amount) < 0) {
            return new EconomyAction(getHolder(), false, "Insufficient balance!");
        }

        UUID playeruuid = getPlayer().getUniqueId();
        DataCon.changeplayerdata("PLUGIN", playeruuid, amount, false, null, null);
        return new EconomyAction(getHolder(), true, "");
    }

    @Override
    public EconomyAction withdraw(BigDecimal amount, String s) {
        return withdraw(amount);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public EconomyAction deposit(BigDecimal amount) {
        if (XConomyLoad.getSyncData_Enable() & AdapterManager.BanModiftyBalance()) {
            return new EconomyAction(getHolder(), false,
                    "[BungeeCord] No player in server");
        }

        BigDecimal bal = getBalance();

        if (DataFormat.isMAX(bal.add(amount))) {
            return new EconomyAction(getHolder(), false,  "Max balance!");
        }

        UUID playerUUID = getPlayer().getUniqueId();
        DataCon.changeplayerdata("PLUGIN", playerUUID, amount, true, null, null);
        return new EconomyAction(getHolder(), true,  "");
    }

    @Override
    public EconomyAction deposit(BigDecimal amount, String s) {
        return deposit(amount);
    }
}
