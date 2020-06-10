package me.Yi.XConomy;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.Yi.XConomy.Data.Cache;
import me.Yi.XConomy.Data.Cache_NonPlayer;
import me.Yi.XConomy.Data.DataFormat;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

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

	@Override
	public EconomyResponse depositPlayer(String name, double amount) {
		if (XConomy.isbc() & Bukkit.getOnlinePlayers().isEmpty()) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"[BungeeCord] No player in server");
		}
		Double bal = getBalance(name);
		Player a = Bukkit.getPlayer(name);
		BigDecimal amountd = DataFormat.formatd(amount);
		if (isnon(name)) {
			Cache_NonPlayer.change(name, amountd, 1);
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
		}
		if (a != null) {
			UUID u = a.getUniqueId();
			Cache.change(u, amountd, 1);
		} else {
			UUID u = Cache.translateuid(name);
			if (u == null) {
				return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "No Account!");
			}
			Cache.change(u, amountd, 1);
			;
		}

		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(String name, String arg1, double amount) {
		return depositPlayer(name, amount);
	}

	@Override
	public String format(double summ) {
		return DataFormat.dfs.format(summ);
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public double getBalance(String name) {
		if (isnon(name)) {
			return Cache_NonPlayer.getbal(name).doubleValue();
		}
		if (Bukkit.getPlayer(name) != null) {
			return Cache.getbal(Bukkit.getPlayer(name).getUniqueId()).doubleValue();
		} else {
			UUID u = Cache.translateuid(name);
			if (u != null) {
				if (Cache.getbal(u) == null) {
					return 0.0;
				} else {
					return Cache.getbal(u).doubleValue();
				}
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
		if (isnon(name)) {
			return true;
		}
		if (Cache.translateuid(name) != null) {
			return true;
		}
		return false;
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
		if (XConomy.isbc() & Bukkit.getOnlinePlayers().isEmpty()) {
			return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.FAILURE,
					"[BungeeCord] No player in server");
		}
		Double bal = getBalance(name);
		Player a = Bukkit.getPlayer(name);
		BigDecimal amountd = DataFormat.formatd(amount);
		if (bal < amount) {
			return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "Insufficient balance!");
		}
		if (isnon(name)) {
			Cache_NonPlayer.change(name, amountd, 2);
			return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
		}
		if (a != null) {
			UUID u = a.getUniqueId();
			Cache.change(u, amountd, 2);
			;
		} else {
			UUID u = Cache.translateuid(name);
			if (u == null) {
				return new EconomyResponse(0.0D, bal, EconomyResponse.ResponseType.FAILURE, "No Account!");
			}
			Cache.change(u, amountd, 2);
			;
		}
		return new EconomyResponse(amount, bal, EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String arg1, double amount) {
		return withdrawPlayer(name, amount);
	}

	private boolean isnon(String name) {
		if (XConomy.config.getBoolean("Settings.non-player-account")) {
			if (name.length() >= 17) {
				return true;
			}
			if (Cache_NonPlayer.bal.containsKey(name)) {
				return true;
			}
			if (Cache.translateuid(name) == null) {
				return true;
			}
		}
		return false;
	}

}
