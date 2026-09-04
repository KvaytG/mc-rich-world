package ru.kvaytg.richdonate.paper.donate.coins;

import org.bukkit.entity.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public enum CoinsManager {

    INSTANCE;

    private static final long DEFAULT_BALANCE = 0L;

    private final Map<UUID, Long> balances = new ConcurrentHashMap<>();

    public void init() {
        balances.clear();
    }

    public void setBalance(Player player, long balance) {
        if (balance < 0) {
            balance = 0;
        }
        balances.put(player.getUniqueId(), balance);
    }

    public long getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), DEFAULT_BALANCE);
    }

    public void remove(Player player) {
        balances.remove(player.getUniqueId());
    }

}