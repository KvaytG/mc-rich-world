package ru.kvaytg.richdonate.paper.donate.coins;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

/*
*
* Менеджер монеток на стороне Paper
*
*/
public enum CoinsManager {

    INSTANCE;

    private static final int DEFAULT_BALANCE = 0;

    private Map<Player, Integer> playersAndMoneys;

    public void init() {
        playersAndMoneys = new HashMap<>();
    }

    public void setBalance(Player player, int balance) {
        playersAndMoneys.put(player, balance);
    }

    public int getBalance(Player player) {
        return playersAndMoneys.getOrDefault(player, DEFAULT_BALANCE);
    }

}