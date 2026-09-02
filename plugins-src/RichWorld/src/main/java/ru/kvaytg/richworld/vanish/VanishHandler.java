package ru.kvaytg.richworld.vanish;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AbstractHandler;
import ru.kvaytg.richworld.permission.Permissions;

public class VanishHandler extends AbstractHandler {

    private final VanishManager vanishManager;

    public VanishHandler(RichWorld plugin, VanishManager vanishManager) {
        super(plugin, true);
        this.vanishManager = vanishManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        Player player = ev.getPlayer();
        vanishManager.hideVanishedFor(player);
    }

    @EventHandler
    public void onExit(PlayerQuitEvent ev) {
        Player player = ev.getPlayer();
        if (vanishManager.isVanished(player)) {
            vanishManager.show(player);
        }
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent ev) {
        Player player = ev.getPlayer();
        if (!vanishManager.isVanished(player)
                || Permissions.VANISH_INVENTORY.hasNo(player)) return;
        if (ev.getRightClicked() instanceof Player target) {
            player.openInventory(target.getInventory());
        }
    }

}