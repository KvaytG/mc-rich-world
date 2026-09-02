package ru.kvaytg.richpit.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.kvaytg.richpit.RichPit;

public class NightVisionHandler extends AbstractListener {

    private final RichPit plugin;

    private final PotionEffect potionEffect;

    public NightVisionHandler(RichPit plugin) {
        super(plugin);
        this.plugin = plugin;
        potionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().addPotionEffect(potionEffect);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            event.getPlayer().addPotionEffect(potionEffect);
        }, 1L);
    }

}