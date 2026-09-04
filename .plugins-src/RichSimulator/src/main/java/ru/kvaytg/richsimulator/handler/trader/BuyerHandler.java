package ru.kvaytg.richsimulator.handler.trader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richsimulator.RichSimulator;
import ru.kvaytg.richsimulator.trader.TraderNames;

public class BuyerHandler extends TraderHandler {

    private static final int GRASS_PER_COIN = 32;

    private final RichDonate donateApi;

    private final String successMessage1;
    private final String successMessage2;
    private final String errorMessage1;
    private final String errorMessage2;

    public BuyerHandler(RichSimulator plugin) {
        super(plugin);
        donateApi = RichDonate.getInstance();
        successMessage1 = ColorAPI.colorize(
                "&#FFFF31Вы сдали &#FFAA01%d &#FFFF31травы"
        );
        successMessage2 = ColorAPI.colorize(
                "&#FFFF31и получили монет: &#FFAA01%d &#FFFF31шт."
        );
        errorMessage1 = ColorAPI.colorize(
                "&#FFFF31Нужно минимум &#FFAA01" + GRASS_PER_COIN + " &#FFFF31травы для обмена!"
        );
        errorMessage2 = ColorAPI.colorize(
                "&#FFFF31У вас сейчас травы: &#FFAA01%d &#FFFF31шт."
        );
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (isTraderWithName(entity, TraderNames.BUYER)) {
            event.setCancelled(true);
            if (checkAndApplyCooldown(player)) return;
            int totalGrass = countGrass(player);
            if (totalGrass >= GRASS_PER_COIN) {
                int stacks = totalGrass / GRASS_PER_COIN;
                int grassToRemove = stacks * GRASS_PER_COIN;
                removeGrass(player, grassToRemove);
                donateApi.giveCoins(player, stacks);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                player.sendMessage(String.format(successMessage1, grassToRemove));
                player.sendMessage(String.format(successMessage2, stacks));
                Villager villager = (Villager) entity;
                villager.getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY,
                        villager.getLocation().add(0, 2, 0), 15);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage(errorMessage1);
                player.sendMessage(String.format(errorMessage2, totalGrass));
            }
        }
    }

    private int countGrass(Player player) {
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            if (isGrassItem(item)) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private void removeGrass(Player player, int amount) {
        int remaining = amount;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            if (isGrassItem(item)) {
                int remove = Math.min(remaining, item.getAmount());
                item.setAmount(item.getAmount() - remove);
                remaining -= remove;
                if (remaining <= 0) break;
            }
        }
        player.updateInventory();
    }

    private boolean isGrassItem(ItemStack item) {
        if (item.getType() != Material.WHEAT) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;
        Component displayName = meta.displayName();
        if (!(displayName instanceof TextComponent)) return false;
        return "Трава".equals(((TextComponent) displayName).content());
    }

}