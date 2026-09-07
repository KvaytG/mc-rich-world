package ru.kvaytg.coremc.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import ru.kvaytg.coremc.CoreMc;

public class FallRescueHandler extends PerkHandler {

    public FallRescueHandler(CoreMc plugin) {
        super(plugin, "fallRescue");
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

}
