package me.yic.xconomy;

import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.data.caches.NonPlayerCache;
import me.yic.xconomy.data.DataFormat;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Vault extends AbstractEconomy {

	@Override
	public EconomyResponse bankBalance(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayerAccount(String name) {
		return hasAccount(name);
	}

	@Override
	public boolean createPlayerAccount(String name, String arg1) {
		return createPlayerAccount(name);
	}

	@Override
	public String currencyNamePlural() {
		return XConomy.config.getString("Currency.plural-name");
	}

	@Override
	public String currencyNameSingular() {
		return XConomy.config.getString("Currency.singular-name");
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Deposit an amount to a player's balance
	 *
	 * @param name the player name to deposit oo
	 * @param amount     the amount to deposit
	 * @return {@code EconomyResponse}
	 */
	@Override
	public EconomyResponse depositPlayer(String name, double amount) {
		if (XConomy.isBungeecord() & Bukkit.getOnlinePlayers().isEmpty()) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"[BungeeCord] No player in server");
		}

		double bal = getBalance(name);
		Player player = Bukkit.getPlayerExact(name);
		BigDecimal amountFormatted = DataFormat.formatDouble(amount);

		if (DataFormat.isMAX(DataFormat.formatDouble(bal).add(amountFormatted))) {
			return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Max balance!");
		}

		if (isNonPlayerAccount(name)) {
			NonPlayerCache.change(name, amountFormatted, true,"PLUGIN");
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
		}

		UUID playerUUID = Cache.translateUUID(name);
		if (playerUUID == null) {
			return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "No Account!");
		}
		Cache.change(playerUUID, amountFormatted, true,"PLUGIN", name, "N/A");
		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String name, String arg1, double amount) {
		return depositPlayer(name, amount);
	}

	@Override
	public String format(double sum) {
		return DataFormat.shownd(sum);
	}

	@Override
	public int fractionalDigits() {
		if (DataFormat.isInteger) {
			return 0;
		}
		return 2;
		}

	@Override
	public double getBalance(String name) {
		if (isNonPlayerAccount(name)) {
			return NonPlayerCache.getBalanceFromCacheOrDB(name).doubleValue();
		}

		if (Bukkit.getPlayerExact(name) != null) {
			return Cache.getBalanceFromCacheOrDB(Bukkit.getPlayerExact(name).getUniqueId()).doubleValue();
		}

		UUID uuid = Cache.translateUUID(name);
		if (uuid != null) {
			if (Cache.getBalanceFromCacheOrDB(uuid) != null) {
				return Cache.getBalanceFromCacheOrDB(uuid).doubleValue();
			} else {
				return 0.0;
			}
		}

		return XConomy.config.getDouble("Settings.initial-bal");
	}

	@Override
	public double getBalance(String name, String arg1) {
		return getBalance(name);
	}

	@Override
	public List<String> getBanks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "XConomy";
	}

	@Override
	public boolean has(String name, double amount) {
		return getBalance(name) >= amount;
	}

	@Override
	public boolean has(String name, String arg1, double amount) {
		return has(name, amount);
	}

	@Override
	public boolean hasAccount(String name) {
		if (isNonPlayerAccount(name)) {
			return true;
		}
		return Cache.translateUUID(name) != null;
	}

	@Override
	public boolean hasAccount(String name, String arg1) {
		return hasAccount(name);
	}

	@Override
	public boolean hasBankSupport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, double amount) {
		if (XConomy.isBungeecord() & Bukkit.getOnlinePlayers().isEmpty()) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"[BungeeCord] No player in server");
		}

		double bal = getBalance(name);
		Player player = Bukkit.getPlayerExact(name);
		BigDecimal amountFormatted = DataFormat.formatDouble(amount);

		if (bal < amount) {
			return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Insufficient balance!");
		}

		if (isNonPlayerAccount(name)) {
			NonPlayerCache.change(name, amountFormatted, false,"PLUGIN");
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
		}

		UUID playeruuid = Cache.translateUUID(name);
		if (playeruuid == null) {
			return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "No Account!");
		}
		Cache.change(playeruuid, amountFormatted, false,"PLUGIN", name, "N/A");
		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String arg1, double amount) {
		return withdrawPlayer(name, amount);
	}

	private boolean isNonPlayerAccount(String name) {
		if (!XConomy.config.getBoolean("Settings.non-player-account")) {
			return false;
		}

		if (name.length() >= 17) {
			return true;
		}

		if (NonPlayerCache.bal.containsKey(name)) {
			return true;
		}

		return Cache.translateUUID(name) == null;
	}

}
