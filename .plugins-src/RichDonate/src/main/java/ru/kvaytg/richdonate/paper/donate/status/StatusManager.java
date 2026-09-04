package ru.kvaytg.richdonate.paper.donate.status;

import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public enum StatusManager {

    INSTANCE;

    private static final String DEFAULT_STATUS = "default";

    private final Map<UUID, String> statuses = new ConcurrentHashMap<>();

    public void init() {
        statuses.clear();
    }

    public void setStatus(Player player, String status) {
        statuses.put(player.getUniqueId(), normalize(status));
    }

    public String getStatus(Player player) {
        return statuses.getOrDefault(player.getUniqueId(), DEFAULT_STATUS);
    }

    public void remove(Player player) {
        statuses.remove(player.getUniqueId());
    }

    private String normalize(String status) {
        if (status == null || status.isBlank()) return DEFAULT_STATUS;
        return status.toLowerCase(java.util.Locale.ROOT);
    }

}