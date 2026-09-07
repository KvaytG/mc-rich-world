package ru.kvaytg.coremc.limits;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;

public class ExperienceHandler extends LimitHandler {

    public ExperienceHandler(CoreMc plugin) {
        super(plugin, "experience");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!getWorlds().contains(player.getWorld())) return;
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (!getWorlds().contains(event.getPlayer().getWorld())) return;
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerPickupExperience(PlayerPickupExperienceEvent event) {
        if (!getWorlds().contains(event.getPlayer().getWorld())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExp(BlockExpEvent event) {
        if (!getWorlds().contains(event.getBlock().getWorld())) return;
        event.setExpToDrop(0);
    }

}