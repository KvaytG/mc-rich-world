package ru.kvaytg.coremc.warp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public record Warp(String name, Location location) {

    public void teleport(Player player) {
        player.teleport(location);
    }

}