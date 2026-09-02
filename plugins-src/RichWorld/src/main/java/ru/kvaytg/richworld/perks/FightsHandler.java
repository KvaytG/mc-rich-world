package ru.kvaytg.richworld.perks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.kvaytg.richworld.RichWorld;

public class FightsHandler extends PerkHandler {

    public FightsHandler(RichWorld plugin) {
        super(plugin, "preventFights");
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            event.setCancelled(true);
            return;
        }
        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player) {
            event.setCancelled(true);
        }
    }

}