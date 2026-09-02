package ru.kvaytg.richdonate.paper.donate.status;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

/*
*
* Менеджер статусов на стороне Paper
*
*/
public enum StatusManager {

    INSTANCE;

    private static final String DEFAULT_STATUS = "default";

    private Map<Player, String> playerAndStatus;

    public void init() {
        playerAndStatus = new HashMap<>();
    }

    public void setStatus(Player player, String status) {
        playerAndStatus.put(player, status);
    }

    public String getStatus(Player player) {
        return playerAndStatus.getOrDefault(player, DEFAULT_STATUS);
    }

}