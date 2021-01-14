package me.yic.xconomy.economyapi;

import org.spongepowered.api.service.context.ContextCalculator;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class XCAccount implements EconomyService {

    public final XCurrency xc = new XCurrency();

    @Override
    public Currency getDefaultCurrency() {
        return xc;
    }

    @Override
    public Set<Currency> getCurrencies() {
        return null;
    }

    @Override
    public boolean hasAccount(UUID uuid) {
        return false;
    }

    @Override
    public boolean hasAccount(String identifier) {
        return false;
    }

    @Override
    public Optional<UniqueAccount> getOrCreateAccount(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> getOrCreateAccount(String identifier) {
        return Optional.empty();
    }

    @Override
    public void registerContextCalculator(ContextCalculator<Account> calculator) {

    }
}
