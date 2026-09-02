package ru.kvaytg.richsimulator.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richsimulator.RichSimulator;

public class EffectHandler extends AbstractListener {

    private final RichDonate donateApi;

    private final PotionEffect nightVisionEffect;
    private final PotionEffect regenerationEffect;
    private final PotionEffect slownessEffect;

    public EffectHandler(RichSimulator plugin) {
        super(plugin);
        donateApi = RichDonate.getInstance();
        nightVisionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
        regenerationEffect = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false);
        slownessEffect = new PotionEffect(PotionEffectType.SLOW, 1200, 2, false, true);
    }

    private void addEffects(Player player) {
        getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {
            player.addPotionEffect(nightVisionEffect);
        }, 1L);
        getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {
            if (!"default".equals(donateApi.getStatus(player))) {
                player.addPotionEffect(regenerationEffect);
            }
        }, 40L);
    }

    private void addSlownessEffect(Player player) {
        getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {
            player.removePotionEffect(PotionEffectType.SLOW);
            player.addPotionEffect(slownessEffect);
        }, 5L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.removePotionEffect(regenerationEffect.getType());
        addEffects(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        addEffects(player);
        addSlownessEffect(player);
    }

}