package ru.kvaytg.richsimulator.trader;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import java.util.Objects;

public enum TraderController {

    INSTANCE;

    public void renameTraders() {
        World world = Objects.requireNonNull(Bukkit.getWorld("world"));
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Villager villager) {
                if (isComparedLocations(villager.getLocation(), new Location(world, 0.5, 93.0, 7.5))) {
                    villager.setCustomName(TraderNames.BUYER);
                } else if (isComparedLocations(villager.getLocation(), new Location(world, 124.5, 93.0, -5.5))) {
                    villager.setCustomName(TraderNames.MERCHANT);
                } else if (isComparedLocations(villager.getLocation(), new Location(world, 0.5, 98.0, 63.5))) {
                    villager.setCustomName(TraderNames.ARCHER);
                }
            }
        }
    }

    private boolean isComparedLocations(Location location1, Location location2) {
        return location1.getBlockX() == location2.getBlockX() &&
                location1.getBlockY() == location2.getBlockY() &&
                location1.getBlockZ() == location2.getBlockZ();
    }

}