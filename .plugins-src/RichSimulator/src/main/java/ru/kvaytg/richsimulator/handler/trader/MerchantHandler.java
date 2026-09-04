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

public class MerchantHandler extends TraderHandler {

    private static final int APPLES_PER_COIN = 8;

    private final RichDonate donateApi;

    private final ItemStack apple;

    private final String successMessage;
    private final String errorMessage;

    public MerchantHandler(RichSimulator plugin) {
        super(plugin);
        donateApi = RichDonate.getInstance();
        ItemStack item = new ItemStack(Material.APPLE, 8);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Яблоко")
                .color(TextColor.fromHexString("#FFFF31"))
                .decoration(TextDecoration.ITALIC, false)
        );
        item.setItemMeta(meta);
        apple = item;
        successMessage = ColorAPI.colorize(
                "&#FFFF31Вы купили &#FFAA01" + APPLES_PER_COIN + " &#FFFF31яблок"
        );
        errorMessage = ColorAPI.colorize(
                "&#FFFF31У вас &#FF0000недостаточно &#FFFF31монеток для покупки"
        );
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (isTraderWithName(entity, TraderNames.MERCHANT)) {
            event.setCancelled(true);
            if (checkAndApplyCooldown(player)) return;
            if (donateApi.takeCoins(player, 1)) {
                PlayerUtils.addToInventory(player, apple, player.getLocation());
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