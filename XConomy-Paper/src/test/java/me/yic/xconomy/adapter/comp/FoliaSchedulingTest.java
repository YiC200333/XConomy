package me.yic.xconomy.adapter.comp;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.api.event.PlayerAccountEvent;
import me.yic.xconomy.info.RecordInfo;
import me.yic.xconomy.info.DefaultConfig;
import me.yic.xconomy.task.RunBaltop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FoliaSchedulingTest {
    private MockedStatic<Bukkit> bukkit;
    private MockedStatic<XConomy> xconomy;
    private XConomy plugin;

    @Before
    public void setup() {
        XConomyLoad.Config = mock(DefaultConfig.class);
        bukkit = mockStatic(Bukkit.class);
        xconomy = mockStatic(XConomy.class);
        plugin = mock(XConomy.class);
        xconomy.when(XConomy::getInstance).thenReturn(plugin);
    }

    @After
    public void close() {
        if (xconomy != null) xconomy.close();
        if (bukkit != null) bukkit.close();
    }

    @Test
    public void oneSecondDelayIsNotTwentyMilliseconds() {
        AsyncScheduler scheduler = mock(AsyncScheduler.class);
        bukkit.when(Bukkit::getAsyncScheduler).thenReturn(scheduler);
        new CPlugin().runTaskLaterAsynchronously(() -> {}, 20L);
        verify(scheduler).runDelayed(eq(plugin), any(), eq(1000L), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void refreshUsesConfiguredSecondsAndCancelsPreviousTask() {
        AsyncScheduler scheduler = mock(AsyncScheduler.class);
        ScheduledTask task = mock(ScheduledTask.class);
        bukkit.when(Bukkit::getAsyncScheduler).thenReturn(scheduler);
        when(scheduler.runAtFixedRate(eq(plugin), any(), anyLong(), anyLong(), any())).thenReturn(task);
        int previous = XConomyLoad.Config.REFRESH_TIME;
        try {
            XConomyLoad.Config.REFRESH_TIME = 60;
            RunBaltop.runstart();
            RunBaltop.runstart();
            verify(scheduler, times(2)).runAtFixedRate(eq(plugin), any(), eq(60L), eq(60L), eq(TimeUnit.SECONDS));
            verify(task).cancel();
        } finally {
            RunBaltop.stop();
            XConomyLoad.Config.REFRESH_TIME = previous;
        }
    }

    @Test
    public void kickWaitsForPlayersOwningRegion() {
        Player player = mock(Player.class);
        EntityScheduler scheduler = mock(EntityScheduler.class);
        when(player.getScheduler()).thenReturn(scheduler);
        new CPlayer(player).kickPlayer("removed");
        verify(player, never()).kickPlayer(anyString());
        ArgumentCaptor<Consumer<ScheduledTask>> callback = ArgumentCaptor.forClass(Consumer.class);
        verify(scheduler).run(eq(plugin), callback.capture(), isNull());
        callback.getValue().accept(mock(ScheduledTask.class));
        verify(player).kickPlayer("removed");
    }

    @Test
    public void pluginMessageWithNoPlayersIsSafe() {
        bukkit.when(Bukkit::getOnlinePlayers).thenReturn(Collections.emptyList());
        new CPlugin().sendPluginMessage("xconomy:acb", new ByteArrayOutputStream());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void onlineAccountEventRunsOnPlayerRegion() {
        UUID id = UUID.randomUUID();
        Player player = mock(Player.class);
        EntityScheduler scheduler = mock(EntityScheduler.class);
        PluginManager manager = mock(PluginManager.class);
        bukkit.when(() -> Bukkit.getPlayer(id)).thenReturn(player);
        bukkit.when(Bukkit::getPluginManager).thenReturn(manager);
        when(player.getScheduler()).thenReturn(scheduler);
        when(scheduler.run(eq(plugin), any(), any())).thenAnswer(invocation -> {
            Consumer<ScheduledTask> action = invocation.getArgument(1);
            action.accept(mock(ScheduledTask.class));
            return mock(ScheduledTask.class);
        });
        CallAPI.CallPlayerAccountEvent(id, "player", BigDecimal.TEN, BigDecimal.ONE, true,
                new RecordInfo("PLUGIN", "test", null));
        verify(manager).callEvent(isA(PlayerAccountEvent.class));
        bukkit.verify(Bukkit::getRegionScheduler, never());
    }

    @Test
    public void offlineAccountEventUsesGlobalSchedulerWithoutWorldLookup() {
        GlobalRegionScheduler scheduler = mock(GlobalRegionScheduler.class);
        bukkit.when(Bukkit::getGlobalRegionScheduler).thenReturn(scheduler);
        CallAPI.CallPlayerAccountEvent(UUID.randomUUID(), "offline", BigDecimal.TEN, BigDecimal.ONE, true,
                new RecordInfo("PLUGIN", "test", null));
        verify(scheduler).execute(eq(plugin), any());
        bukkit.verify(() -> Bukkit.getWorld(anyString()), never());
    }
}
