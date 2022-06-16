package me.yic.xconomy.comp;


import me.yic.xconomy.XConomy;
import org.spongepowered.api.Sponge;

@SuppressWarnings("unused")
public class CScheduler {
    public static void runTaskAsynchronously(Runnable runnable){
        Sponge.getScheduler().createAsyncExecutor(XConomy.getInstance()).execute(runnable);
    }
}
