/*
 *  This file (CompletableFutureManager.java) is a part of project XConomy
 *  Copyright (C) YiC and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.yic.xconomy.utils;

import me.yic.xconomy.XConomy;
import me.yic.xconomy.XConomyLoad;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class CompletableFutureManager {
    private static final ScheduledThreadPoolExecutor delayer;
    static {
        (delayer = new ScheduledThreadPoolExecutor(
                1, new CompletableFutureManager.DaemonThreadFactory())).
                setRemoveOnCancelPolicy(true);
    }

    static final class DaemonThreadFactory implements ThreadFactory {
        public Thread newThread(@NotNull Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("CompletableFutureScheduler");
            return t;
        }
    }

    public static <U> U supplyAsync(Supplier<U> supplier){
        try {
            return CompletableFuture.supplyAsync(supplier)
                    .applyToEither(timeoutAfter(),
                            data -> data).get();
        } catch (InterruptedException | ExecutionException e) {
            if (e.getMessage().contains("TimeoutException")) {
                XConomy.getInstance().logger(null, 1, "Future Timeout");
                e.printStackTrace();
                return null;
            }else {
                throw new RuntimeException(e);
            }
        }
    }


    private static <T> CompletableFuture<T> timeoutAfter() {
        CompletableFuture<T> result = new CompletableFuture<>();
        delayer.schedule(() -> result.completeExceptionally(new TimeoutException()), XConomyLoad.Config.FUTURE_TIMEOUT, TimeUnit.SECONDS);
        return result;
    }

    @SuppressWarnings("unused")
    private static ScheduledFuture<?> delay(Runnable command, long delay,
                                            TimeUnit unit) {
        return delayer.schedule(command, delay, unit);
    }


}
