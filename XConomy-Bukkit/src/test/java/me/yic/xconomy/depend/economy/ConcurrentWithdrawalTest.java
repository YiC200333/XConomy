package me.yic.xconomy.depend.economy;

import me.yic.xconomy.AdapterManager;
import me.yic.xconomy.XConomyLoad;
import me.yic.xconomy.info.DefaultConfig;
import me.yic.xconomy.data.DataCon;
import me.yic.xconomy.data.DataFormat;
import me.yic.xconomy.data.syncdata.PlayerData;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ConcurrentWithdrawalTest {
    @Test(timeout = 10000)
    public void onlyOneRegionCanWithdrawTheRemainingBalance() throws Exception {
        XConomyLoad.Config = mock(DefaultConfig.class);
        UUID id = UUID.randomUUID();
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getUniqueId()).thenReturn(id);
        AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.TEN);
        AtomicInteger successes = new AtomicInteger();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        Runnable withdrawal = () -> {
            try (MockedStatic<DataCon> data = mockStatic(DataCon.class);
                 MockedStatic<AdapterManager> adapter = mockStatic(AdapterManager.class);
                 MockedStatic<DataFormat> format = mockStatic(DataFormat.class)) {
                data.when(() -> DataCon.getPlayerData(id)).thenAnswer(call -> new PlayerData(id, "player", balance.get()));
                format.when(() -> DataFormat.formatdouble(10.0)).thenReturn(BigDecimal.TEN);
                data.when(() -> DataCon.changeplayerdata(anyString(), eq(id), any(), eq(false), isNull(), isNull()))
                        .thenAnswer(call -> {
                            // Widen the race between validation and committing the withdrawal.
                            Thread.sleep(30);
                            BigDecimal result = balance.get().subtract(BigDecimal.TEN);
                            balance.set(result);
                            return result;
                        });
                ready.countDown();
                start.await();
                EconomyResponse response = new Vault().withdrawPlayer(player, 10.0);
                if (response.transactionSuccess()) successes.incrementAndGet();
            } catch (Throwable error) {
                failure.set(error);
                ready.countDown();
            }
        };
        Thread first = new Thread(withdrawal);
        Thread second = new Thread(withdrawal);
        first.start();
        second.start();
        ready.await();
        start.countDown();
        first.join();
        second.join();
        if (failure.get() != null) throw new AssertionError(failure.get());
        assertEquals(1, successes.get());
        assertEquals(BigDecimal.ZERO, balance.get());
    }
}
