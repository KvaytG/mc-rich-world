package ru.kvaytg.richdonate.velocity.donate.status;

import ru.kvaytg.richdonate.velocity.file.Config;
import ru.kvaytg.richdonate.velocity.file.ResourceManager;
import java.util.HashMap;
import java.util.Map;

/*
 *
 * Менеджер статусов на стороне Velocity
 *
 * Может изменять и сохранять статусы игроков в файл
 *
 */
public enum StatusManager {

    INSTANCE;

    private static final String CONFIG_NAME = "donate.ini";
    private static final String SECTION_NAME = "DONATE";
    private static final Config CONFIG = new Config(
            ResourceManager.DATA_FOLDER.resolve(CONFIG_NAME).toString()
    );
    private static final String DEFAULT_STATUS = "default";

    private Map<String, String> playersWithStatus;

    public void init() {
        playersWithStatus = new HashMap<>();
        ResourceManager.INSTANCE.saveResource(CONFIG_NAME, false);
        playersWithStatus.putAll(CONFIG.load(SECTION_NAME));
    }

    private synchronized void save() {
        CONFIG.save(SECTION_NAME, playersWithStatus);
    }

    public synchronized void giveStatus(String player, String status) {
        playersWithStatus.put(player, status);
        save();
    }

    public synchronized void takeStatus(String player) {
        playersWithStatus.put(player, DEFAULT_STATUS);
        save();
    }

    public synchronized String getStatus(String player) {
        return playersWithStatus.getOrDefault(player, DEFAULT_STATUS);
    }

}