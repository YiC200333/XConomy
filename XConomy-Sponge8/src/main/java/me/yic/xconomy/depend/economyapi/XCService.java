/*
 *  This file (XCService.java) is a part of project XConomy
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
package me.yic.xconomy.depend.economyapi;

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.DataCon;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.AccountDeletionResultType;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.account.VirtualAccount;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

public class XCService implements EconomyService {

    //private final Set<Currency> currencies = new HashSet<>();

    @Override
    public Currency defaultCurrency() {
        return XConomy.xc;
    }


    @Override
    public boolean hasAccount(UUID uuid) {
        return DataCon.getPlayerData(uuid) != null;
    }

    @Override
    public boolean hasAccount(String identifier) {
        if (XCEconomyCommon.isNonPlayerAccount(identifier)) {
            return true;
        }
        return DataCon.getPlayerData(identifier) != null;
    }

    @Override
    public Stream<UniqueAccount> streamUniqueAccounts() {
        return null;
    }

    @Override
    public Collection<UniqueAccount> uniqueAccounts() {
        return null;
    }

    @Override
    public Stream<VirtualAccount> streamVirtualAccounts() {
        return null;
    }

    @Override
    public Collection<VirtualAccount> virtualAccounts() {
        return null;
    }

    @Override
    public AccountDeletionResultType deleteAccount(UUID uuid) {
        return null;
    }

    @Override
    public AccountDeletionResultType deleteAccount(String identifier) {
        return null;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Optional<UniqueAccount> findOrCreateAccount(UUID uuid) {
        if (!hasAccount(uuid)) {
            if (!AdapterManager.DATALINK.newPlayer(uuid, Sponge.server().player(uuid).get().name())){
                return Optional.empty();
            }
        }
        try {
            return Optional.of(new XCUniqueAccount(
                    Sponge.server().userManager().load(uuid).get().get()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findOrCreateAccount(String identifier) {
        if (hasAccount(identifier)) {
            return Optional.of(new XCVirtualAccount(identifier));
        }
        return Optional.empty();
    }

}
