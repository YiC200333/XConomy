package me.yic.xconomy.data;

import me.yic.xconomy.data.caches.Cache;
import me.yic.xconomy.info.PermissionINFO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ConcurrencyTest {
    @Test
    public void publishedRankingCannotBeChangedByWriterOrReader() {
        LinkedHashMap<String, BigDecimal> balances = new LinkedHashMap<>();
        balances.put("first", BigDecimal.TEN);
        balances.put("second", BigDecimal.ONE);
        Cache.Ranking snapshot = new Cache.Ranking(balances);
        balances.clear();
        assertEquals("first", snapshot.names.get(0));
        assertEquals(BigDecimal.TEN, snapshot.balances.get(snapshot.names.get(0)));
        assertThrows(UnsupportedOperationException.class, () -> snapshot.names.clear());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.balances.clear());
    }

    @Test
    public void concurrentReceiveTogglesDoNotLoseUpdates() throws Exception {
        UUID player = UUID.randomUUID();
        PermissionINFO.setRPaymentPermission(player, true);
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Future<?>> tasks = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                tasks.add(pool.submit(() -> {
                    for (int n = 0; n < 1001; n++) PermissionINFO.setRPaymentPermission(player);
                }));
            }
            for (Future<?> task : tasks) task.get();
            assertTrue(PermissionINFO.getRPaymentPermission(player));
        } finally {
            pool.shutdownNow();
            PermissionINFO.setRPaymentPermission(player, true);
        }
    }
}
