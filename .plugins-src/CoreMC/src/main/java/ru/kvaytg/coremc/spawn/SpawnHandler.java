package ru.kvaytg.coremc.spawn;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AutoConfigurableHandler;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.perks.HungerHandler;
import ru.kvaytg.coremc.warp.Warp;

import java.util.function.BiConsumer;

public class SpawnHandler extends AutoConfigurableHandler {

    private final Location location;

    public SpawnHandler(CoreMc plugin) {
        super(plugin, ConfigSection.SPAWN.getDotPath());
        location = getWarpLocation(getConfigString("warp"));
    }

    private Location getWarpLocation(String warpName) {
        Warp warp = ConfigManager.INSTANCE.getWarpConfig().getWarp(warpName);
        return (warp != null) ? warp.location() : null;
    }

    private void ifLocationNotNull(Player player, BiConsumer<Player, Location> action) {
        if (location != null) {
            action.accept(player, location);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ifLocationNotNull(event.getPlayer(), Player::teleport);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        player.setHealth((maxHealthAttr != null) ? maxHealthAttr.getValue() : 20.0);
        player.setFoodLevel(HungerHandler.MAX_SATIETY);
        player.setSaturation(5.0f);
        ifLocationNotNull(player, (p, loc) -> event.setRespawnLocation(loc));
    }

}