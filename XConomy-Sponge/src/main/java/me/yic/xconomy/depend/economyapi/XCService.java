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

import me.yic.xconomy.XConomy;
import me.yic.xconomy.data.caches.Cache;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.*;

@SuppressWarnings("NullableProblems")
public class XCService implements EconomyService {

    private final Set<Currency> currencies = new HashSet<>();

    @Override
    public Currency getDefaultCurrency() {
        return XConomy.xc;
    }

    @Override
    public Set<Currency> getCurrencies() {
        return currencies;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public boolean hasAccount(UUID uuid) {
        return !Cache.getBalanceFromCacheOrDB(uuid).getName().equalsIgnoreCase("*");
    }

    @Override
    public boolean hasAccount(String identifier) {
        if (XCEconomyCommon.isNonPlayerAccount(identifier)) {
            return true;
        }
        return Cache.translateUUID(identifier, null) != null;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) {
        if (hasAccount(uuid)) {
            return Optional.of(new XCUniqueAccount(
                    Sponge.getServiceManager().provide(UserStorageService.class).flatMap(provide -> provide.get(uuid)).get()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> getOrCreateAccount(String identifier) {
        if (hasAccount(identifier)) {
            return Optional.of(new XCVirtualAccount(identifier));
        }
        return Optional.empty();
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Account> calculator) {

    }

}
