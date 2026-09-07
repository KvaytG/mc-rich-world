package ru.kvaytg.coremc.joinitems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AbstractHandler;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.JoinItemsConfig;
import java.util.HashSet;
import java.util.Set;

public class JoinItemsHandler extends AbstractHandler {

    private final CoreMc plugin;
    private final JoinItemsConfig config;

    public JoinItemsHandler(CoreMc plugin) {
        super(plugin, true);
        this.plugin = plugin;
        this.config = ConfigManager.INSTANCE.getJoinItemsConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.shouldClearInventoryOnJoin()) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
        }
        giveItems(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Set<JoinItem> joinItems = new HashSet<>(config.getJoinItems());
        event.getDrops().removeIf(drop -> drop != null && joinItems.stream().anyMatch(item -> item.isSimilar(drop)));
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        scheduleTask(player, () -> giveItems(player));
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack drop = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();
        if (config.getJoinItems().stream().anyMatch(item -> item.isSimilar(drop) && !item.isDroppable())) {
            event.setCancelled(true);
            scheduleTask(player, player::updateInventory);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemInHand = event.getItem();
            Player player = event.getPlayer();
            config.getJoinItems().stream()
                    .filter(item -> item.isSimilar(itemInHand))
                    .findFirst().ifPresent(item -> {
                        event.setCancelled(true);
                        item.executeCommand(player);
                        player.updateInventory();
                    });
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        scheduleTask(player, () -> config.getJoinItems().stream()
                .filter(item -> item.isBlockedInWorld(player.getWorld()))
                .forEach(item -> item.removeIfPossible(player)));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && config.getJoinItems().stream().anyMatch(joinItem -> joinItem.isSimilar(item) && joinItem.isMotionless())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack mainHand = event.getMainHandItem();
        ItemStack offHand = event.getOffHandItem();
        if ((mainHand != null && config.getJoinItems().stream().anyMatch(item -> item.isSimilar(mainHand) && item.isMotionless())) ||
                (offHand != null && config.getJoinItems().stream().anyMatch(item -> item.isSimilar(offHand) && item.isMotionless()))) {
            event.setCancelled(true);
        }
    }

    private void giveItems(Player player) {
        config.getJoinItems().forEach(item -> item.giveIfPossible(player));
    }

    private void scheduleTask(Player player, Runnable task) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (player.isOnline()) task.run();
        }, 2L);
    }

}