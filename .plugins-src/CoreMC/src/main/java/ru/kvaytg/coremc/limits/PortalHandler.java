package ru.kvaytg.coremc.limits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.PortalCreateEvent;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.BukkitUtils;

public class PortalHandler extends LimitHandler {

    public PortalHandler(CoreMc plugin) {
        super(plugin, "portals");
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (getWorlds().contains(event.getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        if (clickedBlock.getType() == Material.END_PORTAL_FRAME) {
            Player player = event.getPlayer();
            if (!getWorlds().contains(player.getWorld())) return;
            if (BukkitUtils.hasMaterialInHand(player.getInventory(), Material.ENDER_EYE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerEnterPortal(PlayerPortalEvent event) {
        if (getWorlds().contains(event.getPlayer().getWorld())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEvent event) {
        if (getWorlds().contains(event.getEntity().getWorld())) {
            event.setCancelled(true);
        }
    }

}