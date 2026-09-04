package ru.kvaytg.richdonate.velocity.donate.status;

import ru.kvaytg.richdonate.velocity.file.Config;
import ru.kvaytg.richdonate.velocity.file.ResourceManager;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public enum StatusManager {

    INSTANCE;

    private static final String DEFAULT_STATUS = "default";
    private static final int MAX_TRANSACTIONS = 50_000;

    private final Config config = new Config(
            ResourceManager.DATA_FOLDER.resolve("donate.ini").toString()
    );

    private final Map<UUID, String> statuses = new HashMap<>();
    private final Map<String, String> legacyStatuses = new HashMap<>();
    private final Set<String> processedTransactions = new LinkedHashSet<>();
    private final Object lock = new Object();

    private ExecutorService ioExecutor;

    public void init() {
        synchronized (lock) {
            statuses.clear();
            legacyStatuses.clear();
            processedTransactions.clear();

            ResourceManager.INSTANCE.saveResource("donate.ini", false);

            for (Map.Entry<String, String> entry : config.load("DONATE").entrySet()) {
                try {
                    statuses.put(UUID.fromString(entry.getKey()), normalize(entry.getValue()));
                } catch (IllegalArgumentException ignored) {
                    legacyStatuses.put(entry.getKey(), normalize(entry.getValue()));
                }
            }
            processedTransactions.addAll(config.load("TRANSACTIONS").keySet());
        }

        ioExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "RichDonate-Status-IO");
            thread.setDaemon(true);
            return thread;
        });
    }

    public String getStatus(UUID playerId) {
        synchronized (lock) {
            return statuses.getOrDefault(playerId, DEFAULT_STATUS);
        }
    }

    public void migrateLegacyName(String name, UUID playerId) {
        if (name == null) return;
        synchronized (lock) {
            if (statuses.containsKey(playerId)) return;

            String value = legacyStatuses.remove(name);
            if (value == null) return;

            statuses.put(playerId, value);
            scheduleSave();
        }
    }

    public boolean giveStatus(UUID playerId, String status, String transactionId) {
        status = normalize(status);
        synchronized (lock) {
            if (isProcessed(transactionId)) return false;

            statuses.put(playerId, status);
            markProcessed(transactionId);
            scheduleSave();
            return true;
        }
    }

    public boolean takeStatus(UUID playerId, String transactionId) {
        synchronized (lock) {
            if (isProcessed(transactionId)) return false;

            statuses.put(playerId, DEFAULT_STATUS);
            markProcessed(transactionId);
            scheduleSave();
            return true;
        }
    }

    private String normalize(String status) {
        if (status == null || status.isBlank()) return DEFAULT_STATUS;
        return status.toLowerCase(java.util.Locale.ROOT);
    }

    private boolean isProcessed(String transactionId) {
        return transactionId != null
                && !transactionId.isBlank()
                && processedTransactions.contains(transactionId);
    }

    private void markProcessed(String transactionId) {
        if (transactionId == null || transactionId.isBlank()) return;
        processedTransactions.add(transactionId);
        while (processedTransactions.size() > MAX_TRANSACTIONS) {
            processedTransactions.remove(processedTransactions.iterator().next());
        }
    }

    private void scheduleSave() {
        if (ioExecutor != null && !ioExecutor.isShutdown()) {
            ioExecutor.execute(this::saveNow);
        }
    }

    private void saveNow() {
        Map<String, String> donate = new HashMap<>();
        Map<String, String> transactions = new HashMap<>();

        synchronized (lock) {
            for (Map.Entry<UUID, String> entry : statuses.entrySet()) {
                donate.put(entry.getKey().toString(), entry.getValue());
            }
            donate.putAll(legacyStatuses);
            for (String tx : processedTransactions) {
                transactions.put(tx, "1");
            }
        }

        Map<String, Map<String, String>> sections = new HashMap<>();
        sections.put("DONATE", donate);
        sections.put("TRANSACTIONS", transactions);
        config.save(sections);
    }

    public void flush() {
        if (ioExecutor == null) return;
        ioExecutor.shutdown();
        try {
            ioExecutor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        saveNow();
    }

}