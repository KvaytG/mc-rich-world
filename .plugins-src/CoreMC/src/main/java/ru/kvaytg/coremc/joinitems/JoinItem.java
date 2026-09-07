package ru.kvaytg.coremc.joinitems;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kvaytg.colorapi.ColorAPI;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class JoinItem {

    private final ItemStack item;
    private final int slot;
    private final String command;
    private final boolean isDroppable;
    private final boolean isMobile;
    private final Set<World> allowedWorlds;

    public JoinItem(Material material,
                    String name,
                    int slot,
                    String command,
                    boolean blockDrop,
                    boolean blockMovement,
                    Set<World> allowedWorlds) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(ColorAPI.colorize(name)));
        item.setItemMeta(meta);
        this.item = item;
        this.slot = slot;
        this.command = command;
        this.isDroppable = !blockDrop;
        this.isMobile = !blockMovement;
        this.allowedWorlds = allowedWorlds;
    }

    public boolean isSimilar(ItemStack item) {
        return item != null && this.item.isSimilar(item);
    }

    public void executeCommand(Player player) {
        Bukkit.dispatchCommand(player, command);
    }

    public boolean isDroppable() {
        return isDroppable;
    }

    public boolean isMotionless() {
        return !isMobile;
    }

    public boolean isBlockedInWorld(World world) {
        return !allowedWorlds.contains(world);
    }

    public void giveIfPossible(Player player) {
        if (isBlockedInWorld(player.getWorld())) return;
        PlayerInventory inventory = player.getInventory();
        if (Arrays.stream(inventory.getContents()).filter(Objects::nonNull).anyMatch(this::isSimilar)) {
            return;
        }
        ItemStack previous = inventory.getItem(slot);
        if (previous != null && previous.getType() != Material.AIR) {
            int emptySlot = inventory.firstEmpty();
            if (emptySlot == -1) {
                logInventoryFull(player);
                return;
            }
            inventory.setItem(emptySlot, previous);
        }
        inventory.setItem(slot, item.clone());
    }

    public void removeIfPossible(Player player) {
        PlayerInventory inv = player.getInventory();
        Arrays.stream(inv.getContents())
                .filter(this::isSimilar).filter(Objects::nonNull)
                .forEach(item -> item.setType(Material.AIR));
        if (isSimilar(inv.getHelmet())) inv.setHelmet(null);
        if (isSimilar(inv.getChestplate())) inv.setChestplate(null);
        if (isSimilar(inv.getLeggings())) inv.setLeggings(null);
        if (isSimilar(inv.getBoots())) inv.setBoots(null);
    }

    private void logInventoryFull(Player player) {
        Bukkit.getLogger().warning(String.format(
                "§c%s's inventory is full. Could not give item: %s",
                player.getName(), item.getType()
        ));
    }

}