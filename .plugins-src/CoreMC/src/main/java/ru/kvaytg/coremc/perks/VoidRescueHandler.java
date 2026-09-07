package ru.kvaytg.coremc.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.warp.Warp;
import ru.kvaytg.coremc.config.WarpConfig;

public class VoidRescueHandler extends PerkHandler {

    private Warp warp;

    public VoidRescueHandler(CoreMc plugin) {
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