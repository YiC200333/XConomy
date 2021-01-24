//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package me.yic.libs.bstats.sponge;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.apache.commons.lang3.Validate;
import org.bstats.sponge.Metrics;
import org.slf4j.Logger;
import org.spongepowered.api.Platform.Component;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.scheduler.Task.Builder;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.zip.GZIPOutputStream;

public class Metrics2 implements Metrics {
    private final ScheduledExecutorService scheduler;
    public static final int B_STATS_VERSION = 1;
    public static final int B_STATS_CLASS_REVISION = 2;
    private static final String URL = "https://bStats.org/submitData/sponge";
    private Logger logger;
    private final PluginContainer plugin;
    private final int pluginId;
    private String serverUUID;
    private boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private final List<Metrics> knownMetricsInstances;
    private final List<CustomChart> charts;
    private Path configDir;
    private List<Object> oldInstances;

    private Metrics2(PluginContainer plugin, Logger logger, Path configDir, int pluginId) {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.logFailedRequests = false;
        this.knownMetricsInstances = new CopyOnWriteArrayList();
        this.charts = new ArrayList();
        this.oldInstances = new ArrayList();
        this.plugin = plugin;
        this.logger = logger;
        this.configDir = configDir;
        this.pluginId = pluginId;
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    @Listener
    public void startup(GamePreInitializationEvent event) {
        try {
            this.loadConfig();
        } catch (IOException var3) {
            this.logger.warn("Failed to load bStats config!", var3);
            return;
        }

        if (Sponge.getServiceManager().isRegistered(Metrics.class)) {
            Metrics provider = (Metrics)Sponge.getServiceManager().provideUnchecked(Metrics.class);
            provider.linkMetrics(this);
        } else {
            Sponge.getServiceManager().setProvider(this.plugin.getInstance().get(), Metrics.class, this);
            this.linkMetrics(this);
            this.startSubmitting();
        }

    }

    public void cancel() {
        this.scheduler.shutdown();
    }

    public List<Metrics> getKnownMetricsInstances() {
        return this.knownMetricsInstances;
    }

    public PluginContainer getPluginContainer() {
        return this.plugin;
    }

    public int getRevision() {
        return 2;
    }

    private void linkOldMetrics(Object metrics) {
        try {
            Field field = metrics.getClass().getDeclaredField("plugin");
            field.setAccessible(true);
            PluginContainer plugin = (PluginContainer)field.get(metrics);
            Method method = metrics.getClass().getMethod("getPluginData");
            this.linkMetrics(new OutdatedInstance(metrics, method, plugin));
        } catch (IllegalAccessException | NoSuchMethodException | NoSuchFieldException var5) {
        }

    }

    public void linkMetrics(Metrics metrics) {
        this.knownMetricsInstances.add(metrics);
    }

    public void addCustomChart(CustomChart chart) {
        Validate.notNull(chart, "Chart cannot be null", new Object[0]);
        this.charts.add(chart);
    }

    public JsonObject getPluginData() {
        JsonObject data = new JsonObject();
        String pluginName = this.plugin.getName();
        String pluginVersion = (String)this.plugin.getVersion().orElse("unknown");
        data.addProperty("pluginName", pluginName);
        data.addProperty("id", this.pluginId);
        data.addProperty("pluginVersion", pluginVersion);
        data.addProperty("metricsRevision", 2);
        JsonArray customCharts = new JsonArray();
        Iterator var5 = this.charts.iterator();

        while(var5.hasNext()) {
            CustomChart customChart = (CustomChart)var5.next();
            JsonObject chart = customChart.getRequestJsonObject(this.logger, this.logFailedRequests);
            if (chart != null) {
                customCharts.add(chart);
            }
        }

        data.add("customCharts", customCharts);
        return data;
    }

    private void startSubmitting() {
        try {
            File configPath = this.configDir.resolve("bStats").toFile();
            configPath.mkdirs();
            String className = this.readFile(new File(configPath, "temp.txt"));
            if (className != null) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Field instancesField = clazz.getDeclaredField("knownMetricsInstances");
                    instancesField.setAccessible(true);
                    this.oldInstances = (List)instancesField.get((Object)null);
                    Iterator var5 = this.oldInstances.iterator();

                    while(var5.hasNext()) {
                        Object instance = var5.next();
                        this.linkOldMetrics(instance);
                    }

                    this.oldInstances.clear();
                    Map<Thread, StackTraceElement[]> threadSet = Thread.getAllStackTraces();
                    Iterator var23 = threadSet.entrySet().iterator();

                    while(var23.hasNext()) {
                        Entry entry = (Entry)var23.next();

                        try {
                            if (((Thread)entry.getKey()).getName().startsWith("Timer")) {
                                Field timerThreadField = ((Thread)entry.getKey()).getClass().getDeclaredField("queue");
                                timerThreadField.setAccessible(true);
                                Object taskQueue = timerThreadField.get(entry.getKey());
                                Field taskQueueField = taskQueue.getClass().getDeclaredField("queue");
                                taskQueueField.setAccessible(true);
                                Object[] tasks = (Object[])((Object[])taskQueueField.get(taskQueue));
                                Object[] var12 = tasks;
                                int var13 = tasks.length;

                                for(int var14 = 0; var14 < var13; ++var14) {
                                    Object task = var12[var14];
                                    if (task != null && task.getClass().getName().startsWith(clazz.getName())) {
                                        ((TimerTask)task).cancel();
                                    }
                                }
                            }
                        } catch (Exception var16) {
                        }
                    }
                } catch (ReflectiveOperationException var17) {
                }
            }
        } catch (IOException var18) {
        }

        Runnable submitTask = () -> {
            Iterator var1 = this.oldInstances.iterator();

            while(var1.hasNext()) {
                Object instance = var1.next();
                this.linkOldMetrics(instance);
            }

            this.oldInstances.clear();
            Scheduler scheduler = Sponge.getScheduler();
            Builder taskBuilder = scheduler.createTaskBuilder();
            taskBuilder.execute(this::submitData).submit(this.plugin);
        };
        long initialDelay = (long)(60000.0D * (3.0D + Math.random() * 3.0D));
        long secondDelay = (long)(60000.0D * Math.random() * 30.0D);
        this.scheduler.schedule(submitTask, initialDelay, TimeUnit.MILLISECONDS);
        this.scheduler.scheduleAtFixedRate(submitTask, initialDelay + secondDelay, 1800000L, TimeUnit.MILLISECONDS);
        List<String> enabled = new ArrayList();
        List<String> disabled = new ArrayList();
        Iterator var26 = this.knownMetricsInstances.iterator();

        while(var26.hasNext()) {
            Metrics metrics = (Metrics)var26.next();
            if (Sponge.getMetricsConfigManager().areMetricsEnabled(metrics.getPluginContainer())) {
                enabled.add(metrics.getPluginContainer().getName());
            } else {
                disabled.add(metrics.getPluginContainer().getName());
            }
        }

        StringBuilder builder = (new StringBuilder()).append(System.lineSeparator());
        builder.append("bStats metrics is present in ").append(enabled.size() + disabled.size()).append(" plugins on this server.");
        builder.append(System.lineSeparator());
        if (enabled.isEmpty()) {
            builder.append("Presently, none of them are allowed to send data.").append(System.lineSeparator());
        } else {
            builder.append("Presently, the following ").append(enabled.size()).append(" plugins are allowed to send data:").append(System.lineSeparator());
            builder.append(enabled).append(System.lineSeparator());
        }

        if (disabled.isEmpty()) {
            builder.append("None of them have data sending disabled.");
            builder.append(System.lineSeparator());
        } else {
            builder.append("Presently, the following ").append(disabled.size()).append(" plugins are not allowed to send data:").append(System.lineSeparator());
            builder.append(disabled).append(System.lineSeparator());
        }

        builder.append("To change the enabled/disabled state of any bStats use in a plugin, visit the Sponge config!");
        this.logger.info(builder.toString());
    }

    private JsonObject getServerData() {
        int playerAmount = Math.min(Sponge.getServer().getOnlinePlayers().size(), 200);
        int onlineMode = Sponge.getServer().getOnlineMode() ? 1 : 0;
        String minecraftVersion = Sponge.getGame().getPlatform().getMinecraftVersion().getName();
        String spongeImplementation = Sponge.getPlatform().getContainer(Component.IMPLEMENTATION).getName();
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        int coreCount = Runtime.getRuntime().availableProcessors();
        JsonObject data = new JsonObject();
        data.addProperty("serverUUID", this.serverUUID);
        data.addProperty("playerAmount", playerAmount);
        data.addProperty("onlineMode", onlineMode);
        data.addProperty("minecraftVersion", minecraftVersion);
        data.addProperty("spongeImplementation", spongeImplementation);
        data.addProperty("javaVersion", javaVersion);
        data.addProperty("osName", osName);
        data.addProperty("osArch", osArch);
        data.addProperty("osVersion", osVersion);
        data.addProperty("coreCount", coreCount);
        return data;
    }

    private void submitData() {
        JsonObject data = this.getServerData();
        JsonArray pluginData = new JsonArray();
        Iterator var3 = this.knownMetricsInstances.iterator();

        while(var3.hasNext()) {
            Metrics metrics = (Metrics)var3.next();
            if (Sponge.getMetricsConfigManager().areMetricsEnabled(metrics.getPluginContainer())) {
                JsonObject plugin = metrics.getPluginData();
                if (plugin != null) {
                    pluginData.add(plugin);
                }
            }
        }

        if (pluginData.size() != 0) {
            data.add("plugins", pluginData);
            (new Thread(() -> {
                try {
                    sendData(this.logger, data);
                } catch (Exception e) {
                    if (this.logFailedRequests) {
                        this.logger.warn("Could not submit plugin stats!", var3);
                    }
                }

            })).start();
        }
    }

    private void loadConfig() throws IOException {
        File configPath = this.configDir.resolve("bStats").toFile();
        configPath.mkdirs();
        File configFile = new File(configPath, "config.conf");
        HoconConfigurationLoader configurationLoader = ((HoconConfigurationLoader.Builder)HoconConfigurationLoader.builder().setFile(configFile)).build();
        CommentedConfigurationNode node;
        if (!configFile.exists()) {
            configFile.createNewFile();
            node = (CommentedConfigurationNode)configurationLoader.load();
            node.getNode(new Object[]{"enabled"}).setValue(false);
            node.getNode(new Object[]{"serverUuid"}).setValue(UUID.randomUUID().toString());
            node.getNode(new Object[]{"logFailedRequests"}).setValue(false);
            node.getNode(new Object[]{"logSentData"}).setValue(false);
            node.getNode(new Object[]{"logResponseStatusText"}).setValue(false);
            node.getNode(new Object[]{"enabled"}).setComment("Enabling bStats in this file is deprecated. At least one of your plugins now uses the\nSponge config to control bStats. Leave this value as you want it to be for outdated plugins,\nbut look there for further control");
            node.getNode(new Object[]{"serverUuid"}).setComment("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo control whether this is enabled or disabled, see the Sponge configuration file.\nCheck out https://bStats.org/ to learn more :)");
            node.getNode(new Object[]{"configVersion"}).setValue(2);
            configurationLoader.save(node);
        } else {
            node = (CommentedConfigurationNode)configurationLoader.load();
            if (!node.getNode(new Object[]{"configVersion"}).isVirtual()) {
                node.getNode(new Object[]{"configVersion"}).setValue(2);
                node.getNode(new Object[]{"enabled"}).setComment("Enabling bStats in this file is deprecated. At least one of your plugins now uses the\nSponge config to control bStats. Leave this value as you want it to be for outdated plugins,\nbut look there for further control");
                node.getNode(new Object[]{"serverUuid"}).setComment("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo control whether this is enabled or disabled, see the Sponge configuration file.\nCheck out https://bStats.org/ to learn more :)");
                configurationLoader.save(node);
            }
        }

        this.serverUUID = node.getNode(new Object[]{"serverUuid"}).getString();
        this.logFailedRequests = node.getNode(new Object[]{"logFailedRequests"}).getBoolean(false);
        logSentData = node.getNode(new Object[]{"logSentData"}).getBoolean(false);
        logResponseStatusText = node.getNode(new Object[]{"logResponseStatusText"}).getBoolean(false);
    }

    private String readFile(File file) throws IOException {
        if (!file.exists()) {
            return null;
        } else {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            Throwable var3 = null;

            String var4;
            try {
                var4 = bufferedReader.readLine();
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (bufferedReader != null) {
                    if (var3 != null) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        bufferedReader.close();
                    }
                }

            }

            return var4;
        }
    }

    private static void sendData(Logger logger, JsonObject data) throws Exception {
        Validate.notNull(data, "Data cannot be null", new Object[0]);
        if (logSentData) {
            logger.info("Sending data to bStats: {}", data);
        }

        HttpsURLConnection connection = (HttpsURLConnection)(new URL("https://bStats.org/submitData/sponge")).openConnection();
        byte[] compressedData = compress(data.toString());
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Content-Encoding", "gzip");
        connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "MC-Server/1");
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        Throwable var5 = null;

        try {
            outputStream.write(compressedData);
        } catch (Throwable var28) {
            var5 = var28;
            throw var28;
        } finally {
            if (outputStream != null) {
                if (var5 != null) {
                    try {
                        outputStream.close();
                    } catch (Throwable var26) {
                        var5.addSuppressed(var26);
                    }
                } else {
                    outputStream.close();
                }
            }

        }

        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        Throwable var6 = null;

        try {
            String line;
            try {
                while((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (Throwable var30) {
                var6 = var30;
                throw var30;
            }
        } finally {
            if (bufferedReader != null) {
                if (var6 != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable var27) {
                        var6.addSuppressed(var27);
                    }
                } else {
                    bufferedReader.close();
                }
            }

        }

        if (logResponseStatusText) {
            logger.info("Sent data to bStats and received response: {}", builder);
        }

    }

    private static byte[] compress(String str) throws IOException {
        if (str == null) {
            return null;
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
            Throwable var3 = null;

            try {
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            } catch (Throwable var12) {
                var3 = var12;
                throw var12;
            } finally {
                if (gzip != null) {
                    if (var3 != null) {
                        try {
                            gzip.close();
                        } catch (Throwable var11) {
                            var3.addSuppressed(var11);
                        }
                    } else {
                        gzip.close();
                    }
                }

            }

            return outputStream.toByteArray();
        }
    }

    static {
        String defaultName = "me:yic:libs:bstats:sponge:Metrics".replace(":", ".");
        if (!Metrics.class.getName().equals(defaultName)) {
            throw new IllegalStateException("bStats Metrics interface has been relocated or renamed and will not be run!");
        } else if (!Metrics2.class.getName().equals(defaultName + "2")) {
            throw new IllegalStateException("bStats Metrics2 class has been relocated or renamed and will not be run!");
        }
    }

    public static class AdvancedBarChart extends CustomChart {
        private final Callable<Map<String, int[]>> callable;

        public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, int[]> map = (Map)this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean allSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while(true) {
                    Entry entry;
                    do {
                        if (!var5.hasNext()) {
                            if (allSkipped) {
                                return null;
                            }

                            data.add("values", values);
                            return data;
                        }

                        entry = (Entry)var5.next();
                    } while(((int[])entry.getValue()).length == 0);

                    allSkipped = false;
                    JsonArray categoryValues = new JsonArray();
                    int[] var8 = (int[])entry.getValue();
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        int categoryValue = var8[var10];
                        categoryValues.add(new JsonPrimitive(categoryValue));
                    }

                    values.add((String)entry.getKey(), categoryValues);
                }
            } else {
                return null;
            }
        }
    }

    public static class SimpleBarChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = (Map)this.callable.call();
            if (map != null && !map.isEmpty()) {
                Iterator var4 = map.entrySet().iterator();

                while(var4.hasNext()) {
                    Entry<String, Integer> entry = (Entry)var4.next();
                    JsonArray categoryValues = new JsonArray();
                    categoryValues.add(new JsonPrimitive((Number)entry.getValue()));
                    values.add((String)entry.getKey(), categoryValues);
                }

                data.add("values", values);
                return data;
            } else {
                return null;
            }
        }
    }

    public static class MultiLineChart extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public MultiLineChart(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = (Map)this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean allSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while(var5.hasNext()) {
                    Entry<String, Integer> entry = (Entry)var5.next();
                    if ((Integer)entry.getValue() != 0) {
                        allSkipped = false;
                        values.addProperty((String)entry.getKey(), (Number)entry.getValue());
                    }
                }

                if (allSkipped) {
                    return null;
                } else {
                    data.add("values", values);
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    public static class SingleLineChart extends CustomChart {
        private final Callable<Integer> callable;

        public SingleLineChart(String chartId, Callable<Integer> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            int value = (Integer)this.callable.call();
            if (value == 0) {
                return null;
            } else {
                data.addProperty("value", value);
                return data;
            }
        }
    }

    public static class DrilldownPie extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> callable;

        public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
            super(chartId);
            this.callable = callable;
        }

        public JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Map<String, Integer>> map = (Map)this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean reallyAllSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while(var5.hasNext()) {
                    Entry<String, Map<String, Integer>> entryValues = (Entry)var5.next();
                    JsonObject value = new JsonObject();
                    boolean allSkipped = true;

                    for(Iterator var9 = ((Map)map.get(entryValues.getKey())).entrySet().iterator(); var9.hasNext(); allSkipped = false) {
                        Entry<String, Integer> valueEntry = (Entry)var9.next();
                        value.addProperty((String)valueEntry.getKey(), (Number)valueEntry.getValue());
                    }

                    if (!allSkipped) {
                        reallyAllSkipped = false;
                        values.add((String)entryValues.getKey(), value);
                    }
                }

                if (reallyAllSkipped) {
                    return null;
                } else {
                    data.add("values", values);
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    public static class AdvancedPie extends CustomChart {
        private final Callable<Map<String, Integer>> callable;

        public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            JsonObject values = new JsonObject();
            Map<String, Integer> map = (Map)this.callable.call();
            if (map != null && !map.isEmpty()) {
                boolean allSkipped = true;
                Iterator var5 = map.entrySet().iterator();

                while(var5.hasNext()) {
                    Entry<String, Integer> entry = (Entry)var5.next();
                    if ((Integer)entry.getValue() != 0) {
                        allSkipped = false;
                        values.addProperty((String)entry.getKey(), (Number)entry.getValue());
                    }
                }

                if (allSkipped) {
                    return null;
                } else {
                    data.add("values", values);
                    return data;
                }
            } else {
                return null;
            }
        }
    }

    public static class SimplePie extends CustomChart {
        private final Callable<String> callable;

        public SimplePie(String chartId, Callable<String> callable) {
            super(chartId);
            this.callable = callable;
        }

        protected JsonObject getChartData() throws Exception {
            JsonObject data = new JsonObject();
            String value = (String)this.callable.call();
            if (value != null && !value.isEmpty()) {
                data.addProperty("value", value);
                return data;
            } else {
                return null;
            }
        }
    }

    public abstract static class CustomChart {
        private final String chartId;

        CustomChart(String chartId) {
            if (chartId != null && !chartId.isEmpty()) {
                this.chartId = chartId;
            } else {
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            }
        }

        private JsonObject getRequestJsonObject(Logger logger, boolean logFailedRequests) {
            JsonObject chart = new JsonObject();
            chart.addProperty("chartId", this.chartId);

            try {
                JsonObject data = this.getChartData();
                if (data == null) {
                    return null;
                } else {
                    chart.add("data", data);
                    return chart;
                }
            } catch (Throwable var5) {
                if (logFailedRequests) {
                    logger.warn("Failed to get data for custom chart with id {}", this.chartId, var5);
                }

                return null;
            }
        }

        protected abstract JsonObject getChartData() throws Exception;
    }

    public static class Factory {
        private final PluginContainer plugin;
        private final Logger logger;
        private final Path configDir;

        @Inject
        private Factory(PluginContainer plugin, Logger logger, @ConfigDir(sharedRoot = true) Path configDir) {
            this.plugin = plugin;
            this.logger = logger;
            this.configDir = configDir;
        }

        public Metrics2 make(int pluginId) {
            return new Metrics2(this.plugin, this.logger, this.configDir, pluginId);
        }
    }

    private static class OutdatedInstance implements Metrics {
        private Object instance;
        private Method method;
        private PluginContainer plugin;

        private OutdatedInstance(Object instance, Method method, PluginContainer plugin) {
            this.instance = instance;
            this.method = method;
            this.plugin = plugin;
        }

        public void cancel() {
        }

        public List<Metrics> getKnownMetricsInstances() {
            return new ArrayList();
        }

        public JsonObject getPluginData() {
            try {
                return (JsonObject)this.method.invoke(this.instance);
            } catch (IllegalAccessException | InvocationTargetException | ClassCastException var2) {
                return null;
            }
        }

        public PluginContainer getPluginContainer() {
            return this.plugin;
        }

        public int getRevision() {
            return 0;
        }

        public void linkMetrics(Metrics metrics) {
        }
    }
}

