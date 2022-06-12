package me.yic.xconomy.comp;


import me.yic.xconomy.XConomy;
import org.bukkit.Bukkit;

@SuppressWarnings("unused")
public class CScheduler {

    public static void runTaskAsynchronously(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(XConomy.getInstance(), runnable);
    }
}
