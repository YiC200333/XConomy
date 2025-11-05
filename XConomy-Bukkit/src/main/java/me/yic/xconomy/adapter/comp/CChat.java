package me.yic.xconomy.adapter.comp;

import me.yic.xconomy.utils.RGBColor;
import org.bukkit.ChatColor;

@SuppressWarnings("unused")
public class CChat {
    public static String translateAlternateColorCodes(Character cha, String str) {
        return ChatColor.translateAlternateColorCodes(cha, RGBColor.translateHexColorCodes(str));
    }

}
