package ru.kvaytg.coremc.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;

public class HungerHandler extends PerkHandler {

    public static int MAX_SATIETY = 20;
    private boolean infinite;

    public HungerHandler(CoreMc plugin) {
        super(plugin, "foodLevel");
    }

    @Override
    public void onInit() {
        MAX_SATIETY = Math.max(0, Math.min(getConfigInt("value"), 20));
        infinite = getConfigBoolean("infinite");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setFoodLevel(MAX_SATIETY);
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (!infinite) return;
        if (event.getFoodLevel() != MAX_SATIETY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHungerDamage(EntityDamageEvent event) {
        if (!infinite) return;
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.STARVATION) {
            event.setCancelled(true);
        }
    }

}