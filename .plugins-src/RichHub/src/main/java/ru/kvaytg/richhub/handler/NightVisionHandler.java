package ru.kvaytg.richhub.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.kvaytg.richhub.RichHub;

public class NightVisionHandler implements Listener {

    private final RichHub plugin;

    private final PotionEffect potionEffect;

    public NightVisionHandler(RichHub plugin) {
        this.plugin = plugin;
        potionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
    }

    private void addEffect(Player player) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> player.addPotionEffect(potionEffect), 1L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        addEffect(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        addEffect(event.getPlayer());
    }

}