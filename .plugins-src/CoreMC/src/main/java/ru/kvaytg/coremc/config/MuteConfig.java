package ru.kvaytg.coremc.config;

import ru.kvaytg.coremc.CoreMc;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteConfig extends AbstractConfig {

    private final Map<UUID, Long> mutes = new ConcurrentHashMap<>();

    public MuteConfig(CoreMc plugin) {
        super(plugin, "mutes.yml");
        loadMutes();
    }

    private void loadMutes() {
        for (String key : getConfig().getKeys(false)) {
            mutes.put(UUID.fromString(key), getConfig().getLong(key));
        }
    }

    public void saveMutes() {
        for (String key : getConfig().getKeys(false)) {
            getConfig().set(key, null);
        }
        mutes.forEach((uuid, time) -> {
            if (time > System.currentTimeMillis()) {
                getConfig().set(uuid.toString(), time);
            }
        });
        try {
            getConfig().save(getConfigFile());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setMute(UUID uuid, long durationMillis) {
        long newExpiry = System.currentTimeMillis() + durationMillis;
        long currentExpiry = mutes.getOrDefault(uuid, 0L);
        if (newExpiry > currentExpiry) {
            mutes.put(uuid, newExpiry);
            getConfig().set(uuid.toString(), newExpiry);
        }
    }

    public void unmute(UUID uuid) {
        mutes.remove(uuid);
        getConfig().set(uuid.toString(), null);
    }

    public boolean isMuted(UUID uuid) {
        Long expiry = mutes.get(uuid);
        if (expiry == null) {
            return false;
        }
        if (System.currentTimeMillis() >= expiry) {
            unmute(uuid);
            return false;
        }
        return true;
    }

    public long getRemainingTime(UUID uuid) {
        return Math.max(0, mutes.getOrDefault(uuid, 0L) - System.currentTimeMillis());
    }

}