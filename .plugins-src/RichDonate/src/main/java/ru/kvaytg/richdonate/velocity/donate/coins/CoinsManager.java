package ru.kvaytg.richdonate.velocity.donate.coins;

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

public enum CoinsManager {

    INSTANCE;

    private static final long DEFAULT_BALANCE = 0L;
    private static final int MAX_TRANSACTIONS = 50_000;

    private final Config config = new Config(
            ResourceManager.DATA_FOLDER.resolve("coins.ini").toString()
    );

    private final Map<UUID, Long> balances = new HashMap<>();
    private final Map<String, Long> legacyBalances = new HashMap<>();
    private final Set<String> processedTransactions = new LinkedHashSet<>();
    private final Object lock = new Object();

    private ExecutorService ioExecutor;

    public void init() {
        synchronized (lock) {
            balances.clear();
            legacyBalances.clear();
            processedTransactions.clear();

            ResourceManager.INSTANCE.saveResource("coins.ini", false);

            for (Map.Entry<String, String> entry : config.load("COINS").entrySet()) {
                try {
                    balances.put(UUID.fromString(entry.getKey()), Long.parseLong(entry.getValue()));
                } catch (IllegalArgumentException ignored) {
                    try {
                        legacyBalances.put(entry.getKey(), Long.parseLong(entry.getValue()));
                    } catch (NumberFormatException ignoredValue) {
                        // Повреждённая старая запись игнорируется.
                    }
                }
            }
            processedTransactions.addAll(config.load("TRANSACTIONS").keySet());
        }

        ioExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "RichDonate-IO");
            thread.setDaemon(true);
            return thread;
        });
    }

    public long getCoins(UUID playerId) {
        synchronized (lock) {
            return balances.getOrDefault(playerId, DEFAULT_BALANCE);
        }
    }

    public void migrateLegacyName(String name, UUID playerId) {
        if (name == null) return;
        synchronized (lock) {
            if (balances.containsKey(playerId)) return;
            Long value = legacyBalances.remove(name);
            if (value == null) return;
            balances.put(playerId, value);
            scheduleSave();
        }
    }

    public boolean giveCoins(UUID playerId, long amount, String transactionId) {
        if (amount <= 0) return false;
        synchronized (lock) {
            if (isProcessed(transactionId)) return false;

            long current = balances.getOrDefault(playerId, DEFAULT_BALANCE);
            final long updated;
            try {
                updated = Math.addExact(current, amount);
            } catch (ArithmeticException ex) {
                return false;
            }

            balances.put(playerId, updated);
            markProcessed(transactionId);
            scheduleSave();
            return true;
        }
    }

    public boolean takeCoins(UUID playerId, long amount, String transactionId) {
        if (amount <= 0) return false;
        synchronized (lock) {
            if (isProcessed(transactionId)) return false;

            long current = balances.getOrDefault(playerId, DEFAULT_BALANCE);
            if (current < amount) return false;

            final long updated;
            try {
                updated = Math.subtractExact(current, amount);
            } catch (ArithmeticException ex) {
                return false;
            }

            if (updated < 0) return false;

            balances.put(playerId, updated);
            markProcessed(transactionId);
            scheduleSave();
            return true;
        }
    }

    public boolean takeCoinsIfEnough(UUID playerId, long amount, String transactionId) {
        return takeCoins(playerId, amount, transactionId);
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

    private Map<String, String> toStringMap(Map<String, Long> values) {
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Long> entry : values.entrySet()) {
            result.put(entry.getKey(), Long.toString(entry.getValue()));
        }
        return result;
    }

    private void scheduleSave() {
        if (ioExecutor != null && !ioExecutor.isShutdown()) {
            ioExecutor.execute(this::saveNow);
        }
    }

    private void saveNow() {
        Map<String, String> coins = new HashMap<>();
        Map<String, String> transactions = new HashMap<>();

        synchronized (lock) {
            for (Map.Entry<UUID, Long> entry : balances.entrySet()) {
                coins.put(entry.getKey().toString(), Long.toString(entry.getValue()));
            }
            coins.putAll(toStringMap(legacyBalances));
            for (String tx : processedTransactions) {
                transactions.put(tx, "1");
            }
        }

        Map<String, Map<String, String>> sections = new HashMap<>();
        sections.put("COINS", coins);
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