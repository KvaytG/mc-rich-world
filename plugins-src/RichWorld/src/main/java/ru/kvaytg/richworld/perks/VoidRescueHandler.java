package ru.kvaytg.richworld.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.config.ConfigManager;
import ru.kvaytg.richworld.warp.Warp;
import ru.kvaytg.richworld.config.WarpConfig;

public class VoidRescueHandler extends PerkHandler {

    private Warp warp;

    public VoidRescueHandler(RichWorld plugin) {
        super(plugin, "voidRescue");
    }

    @Override
    public void onInit() {
        WarpConfig warpConfig = ConfigManager.INSTANCE.getWarpConfig();
        warp = warpConfig.getWarp(getConfigString("warp"));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamage(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getCause() == DamageCause.VOID) {
            if (warp == null) return;
            event.setCancelled(true);
            player.setFallDistance(0.0f);
            warp.teleport(player);
        }
    }

}