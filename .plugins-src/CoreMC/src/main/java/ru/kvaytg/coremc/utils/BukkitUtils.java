package ru.kvaytg.coremc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.ProjectInfo;
import ru.kvaytg.coremc.component.AbstractHandler;

public class BukkitUtils {

    private BukkitUtils() {}

    public static void registerHandler(AbstractHandler handler, CoreMc plugin) {
        if (!handler.isEnabled()) return;
        Bukkit.getPluginManager().registerEvents(handler, plugin);
    }

    public static Player getPlayer(String name) {
        return Bukkit.getPlayerExact(name);
    }

    public static boolean hasMaterialInHand(PlayerInventory inventory, Material material) {
        Material mainHand = inventory.getItemInMainHand().getType();
        Material offHand = inventory.getItemInOffHand().getType();
        return mainHand == material || offHand == material;
    }

    public static void disableAI(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasAI()) {
                livingEntity.setAI(false);
            }
        }
    }

    public static boolean isAdmin(CommandSender sender) {
        return sender.isOp() || sender.getName().equals(ProjectInfo.AUTHOR);
    }

}