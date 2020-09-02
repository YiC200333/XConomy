package me.yi.xconomy.data;

import me.yi.xconomy.XConomy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DataFormat {

	public static boolean isInteger = false;
	public static DecimalFormat decimalFormat;
	public static BigDecimal maxNumber;

	public static BigDecimal formatString(String am) {
		BigDecimal bigDecimal = new BigDecimal(am);
		if (isInteger) {
			return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
		} else {
			return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
		}
	}

	public static BigDecimal formatDouble(Double am) {
		BigDecimal bigDecimal = new BigDecimal(String.valueOf(am));
		if (isInteger) {
			return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
		} else {
			return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
		}
	}

	public static String shown(BigDecimal am) {
		if (am.compareTo(BigDecimal.ONE) == 1) {
			return XConomy.config.getString("Currency.display-format")
					.replace("%balance%", decimalFormat.format(am))
					.replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNamePlural());
		}
		return XConomy.config.getString("Currency.display-format")
				.replace("%balance%", decimalFormat.format(am))
				.replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNameSingular());
	}

	public static boolean isValid(BigDecimal am) {
		return am.compareTo(maxNumber) < 0;
	}

	public static void load() {
		maxNumber = new BigDecimal("10000000000000000");
		isInteger = XConomy.config.getBoolean("Currency.integer-bal");
		String format = "###" + XConomy.config.getString("Currency.thousands-separator") + "##0";

		if (isInteger) {
			decimalFormat = new DecimalFormat(format);
			return;
		}

		format = format + ".00";
		decimalFormat = new DecimalFormat(format);
		DecimalFormatSymbols sf = new DecimalFormatSymbols();
		sf.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(sf);

	}
}
