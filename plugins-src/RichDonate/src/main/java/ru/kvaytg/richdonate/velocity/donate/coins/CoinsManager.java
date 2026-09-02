package ru.kvaytg.richdonate.velocity.donate.coins;

import ru.kvaytg.richdonate.velocity.file.Config;
import ru.kvaytg.richdonate.velocity.file.ResourceManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
*
* Менеджер монеток на стороне Velocity
*
* Может изменять и сохранять балансы игроков в файл
*
*/
public enum CoinsManager {

    INSTANCE;

    private static final int DEFAULT_BALANCE = 0;

    private final String configName = "coins.ini";
    private final String sectionName = "COINS";
    private final Config config = new Config(ResourceManager.DATA_FOLDER.resolve(configName).toString());

    private Map<String, Integer> playersAndCoins = new HashMap<>();
    private Map<String, Object> playerLocks = new ConcurrentHashMap<>();

    public void init() {
        playersAndCoins = new HashMap<>();
        playerLocks = new ConcurrentHashMap<>();
        ResourceManager.INSTANCE.saveResource(configName, false);
        Map<String, String> loaded = config.load(sectionName);
        for (Map.Entry<String, String> entry : loaded.entrySet()) {
            playersAndCoins.put(entry.getKey(), Integer.parseInt(entry.getValue()));
        }
    }

    private void save() {
        Map<String, String> stringMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : playersAndCoins.entrySet()) {
            stringMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        config.save(sectionName, stringMap);
    }

    public int getCoins(String player) {
        return playersAndCoins.getOrDefault(player, DEFAULT_BALANCE);
    }

    public void giveCoins(String player, int amount) {
        if (amount <= 0) return;
        Object lock = playerLocks.computeIfAbsent(player, k -> new Object());
        synchronized (lock) {
            playersAndCoins.put(player, getCoins(player) + amount);
            save();
        }
    }

    public void takeCoins(String player, int amount) {
        if (amount <= 0) return;
        Object lock = playerLocks.computeIfAbsent(player, k -> new Object());
        synchronized (lock) {
            playersAndCoins.put(player, getCoins(player) - amount);
            save();
        }
    }

}