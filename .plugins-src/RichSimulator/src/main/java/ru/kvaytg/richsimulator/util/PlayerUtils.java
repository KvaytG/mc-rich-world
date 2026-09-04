package ru.kvaytg.richsimulator.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtils {

    public static void addToInventory(Player player, ItemStack item, Location location) {
        if (player.getInventory().addItem(item).isEmpty()) {
            return;
        }
        location.getWorld().playSound(location, Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.0f);
        player.getWorld().dropItemNaturally(location, item);
    }

}