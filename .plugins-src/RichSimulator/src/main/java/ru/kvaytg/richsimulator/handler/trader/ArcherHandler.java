package ru.kvaytg.richsimulator.handler.trader;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import ru.kvaytg.richsimulator.util.PlayerUtils;

public class ArcherHandler extends TraderHandler {

    private static final int ARROWS_PER_COIN = 4;

    private final RichDonate donateApi;

    private final ItemStack arrow;

    private final String successMessage;
    private final String errorMessage;

    public ArcherHandler(RichSimulator plugin) {
        super(plugin);
        donateApi = RichDonate.getInstance();
        ItemStack item = new ItemStack(Material.ARROW, 4);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Стрела")
                .color(TextColor.fromHexString("#FFFF31"))
                .decoration(TextDecoration.ITALIC, false)
        );
        item.setItemMeta(meta);
        arrow = item;
        successMessage = ColorAPI.colorize(
                "&#FFFF31Вы купили &#FFAA01" + ARROWS_PER_COIN + " &#FFFF31стрелы"
        );
        errorMessage = ColorAPI.colorize(
                "&#FFFF31У вас &#FF0000недостаточно &#FFFF31монеток для покупки"
        );
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (isTraderWithName(entity, TraderNames.ARCHER)) {
            event.setCancelled(true);
            if (checkAndApplyCooldown(player)) return;
            if (donateApi.takeCoins(player, 1)) {
                PlayerUtils.addToInventory(player, arrow, player.getLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                player.sendMessage(successMessage);
                Villager villager = (Villager) entity;
                villager.getWorld().spawnParticle(org.bukkit.Particle.VILLAGER_HAPPY,
                        villager.getLocation().add(0, 2, 0), 15);
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                player.sendMessage(errorMessage);
            }
        }
    }

}