package me.Yi.XConomy.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import me.Yi.XConomy.XConomy;

public class DataFormat {

	public static boolean isi = false;
	public static DecimalFormat dfs;

	public static BigDecimal formatd(Double am) {
		BigDecimal bd = new BigDecimal(Double.toString(am));
		if (isi) {
			return bd.setScale(0, BigDecimal.ROUND_DOWN);
		} else {
			return bd.setScale(2, BigDecimal.ROUND_DOWN);
		}
	}

	public static Double formats(String am) {
		BigDecimal bd = new BigDecimal(am);
		if (isi) {
			return bd.setScale(0, BigDecimal.ROUND_DOWN).doubleValue();
		} else {
			return bd.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		}
	}

	public static String shown(Double am) {
		if (am > 1) {
			return XConomy.config.getString("Currency.display-format").replace("%balance%", dfs.format(am))
					.replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNamePlural());
		}
		return XConomy.config.getString("Currency.display-format").replace("%balance%", dfs.format(am))
				.replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNameSingular());
	}

	public static void load() {
		isi = XConomy.config.getBoolean("Currency.integer-bal");
		String forms = "###" + XConomy.config.getString("Currency.thousands-separator") + "##0";
		if (isi) {
			dfs = new DecimalFormat(forms);
		} else {
			forms = forms + ".00";
			dfs = new DecimalFormat(forms);
			DecimalFormatSymbols sf = new DecimalFormatSymbols();
			sf.setDecimalSeparator('.');
			dfs.setDecimalFormatSymbols(sf);
		}
	}
}
