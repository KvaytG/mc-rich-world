package ru.kvaytg.richmobs.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BloodHandler implements Listener {

    private final BlockData bloodData;

    public BloodHandler() {
        bloodData = Material.NETHER_WART_BLOCK.createBlockData();
    }

    private void exudeBlood(Entity entity, boolean isUpper) {
        Location location = isUpper
                ? entity.getLocation().add(0, 1, 0)
                : entity.getLocation().add(0, 0.5, 0);
        entity.getWorld().spawnParticle(
                Particle.BLOCK_DUST,
                location,
                15,
                0.25, 0.25, 0.25,
                bloodData
        );
    }

    /*
     *
     * Добавляет кровь для только что умершего игрока
     *
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        exudeBlood(event.getEntity(), true);
    }

    /*
     *
     * Добавляет кровь для только что умершей сущности
     *
     */
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        exudeBlood(event.getEntity(), false);
    }

}
