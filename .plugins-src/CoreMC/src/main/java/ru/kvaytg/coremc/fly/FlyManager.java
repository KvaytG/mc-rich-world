package ru.kvaytg.coremc.fly;

import org.bukkit.entity.Player;

public enum FlyManager {

    INSTANCE;

    private void fly(Player player, boolean value) {
        player.setAllowFlight(value);
        player.setFlying(value);
    }

    public void on(Player player) {
        fly(player, true);
    }

    public void off(Player player) {
        fly(player, false);
    }

    public FlyStatus switchFly(Player player) {
        if (player.getAllowFlight()) {
            off(player);
            return FlyStatus.DISABLED;
        } else {
            on(player);
            return FlyStatus.ENABLED;
        }
    }

}