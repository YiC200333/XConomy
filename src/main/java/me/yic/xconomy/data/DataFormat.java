package me.yic.xconomy.data;

import me.yic.xconomy.XConomy;
import org.bukkit.ChatColor;

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

    @SuppressWarnings("ConstantConditions")
    public static String shown(BigDecimal am) {
        if (am.compareTo(BigDecimal.ONE) > 0) {
            return ChatColor.translateAlternateColorCodes('&', XConomy.config.getString("Currency.display-format")
                    .replace("%balance%", decimalFormat.format(am))
                    .replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNamePlural()));
        }
        return ChatColor.translateAlternateColorCodes('&', XConomy.config.getString("Currency.display-format")
                .replace("%balance%", decimalFormat.format(am))
                .replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNameSingular()));
    }

    @SuppressWarnings("ConstantConditions")
    public static String shownd(Double am) {
        if (am > 1) {
            return ChatColor.translateAlternateColorCodes('&', XConomy.config.getString("Currency.display-format")
                    .replace("%balance%", decimalFormat.format(am))
                    .replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNamePlural()));
        }
        return ChatColor.translateAlternateColorCodes('&', XConomy.config.getString("Currency.display-format")
                .replace("%balance%", decimalFormat.format(am))
                .replace("%currencyname%", XConomy.getInstance().getEconomy().currencyNameSingular()));
    }

    public static boolean isMAX(BigDecimal am) {
        return am.compareTo(maxNumber) > 0;
    }

    public static void load() {
        maxNumber = setmaxnumber();
        isInteger = XConomy.config.getBoolean("Currency.integer-bal");
        String format = "###,##0";
        String gpoint = XConomy.config.getString("Currency.thousands-separator");

        if (isInteger) {
            decimalFormat = new DecimalFormat(format);
            return;
        }

        format = format + ".00";
        decimalFormat = new DecimalFormat(format);
        DecimalFormatSymbols spoint = new DecimalFormatSymbols();
        spoint.setDecimalSeparator('.');
        if (gpoint != null && gpoint.length() == 1) {
            spoint.setGroupingSeparator(gpoint.charAt(0));
        }
        decimalFormat.setDecimalFormatSymbols(spoint);
    }


    private static BigDecimal setmaxnumber() {
        String maxn = XConomy.config.getString("Currency.max-number");
        BigDecimal defaultmaxnumber = new BigDecimal("10000000000000000");
        if (maxn == null) {
            return defaultmaxnumber;
        }
        if (maxn.length() > 17) {
            return defaultmaxnumber;
        }
        BigDecimal mnumber = new BigDecimal(maxn);
        if (mnumber.compareTo(defaultmaxnumber) >= 0) {
            return defaultmaxnumber;
        } else {
            return mnumber;
        }
    }
}
