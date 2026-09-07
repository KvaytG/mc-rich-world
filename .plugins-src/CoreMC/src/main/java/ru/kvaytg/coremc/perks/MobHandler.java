package ru.kvaytg.coremc.perks;

import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import ru.kvaytg.coremc.CoreMc;
import java.util.List;

public class MobHandler extends PerkHandler {

    private List<World.Environment> skippedEnvironments;
    private List<EntityType> blockedEntities;

    private EntityType singleType;
    private boolean fakeDeath;
    private boolean exudeBlood;

    private BlockData bloodData;

    public MobHandler(CoreMc plugin) {
        super(plugin, "onlyMobs");
    }

    @Override
    public void onInit() {
        skippedEnvironments = List.of(
                World.Environment.NETHER, World.Environment.THE_END
        );
        blockedEntities = List.of(
                EntityType.BEE, EntityType.PHANTOM, EntityType.BAT, EntityType.PARROT
        );
        singleType = EntityType.valueOf(getConfigString("type"));
        fakeDeath = getConfigBoolean("fakeDeath");
        exudeBlood = getConfigBoolean("exudeBlood");
        if (exudeBlood) bloodData = Material.NETHER_WART_BLOCK.createBlockData();
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        Location location = event.getLocation();
        World world = location.getWorld();
        if (skippedEnvironments.contains(world.getEnvironment())) return;
        EntityType type = event.getEntityType();
        if (blockedEntities.contains(type)) {
            event.setCancelled(true);
            return;
        }
        if (type != singleType) {
            event.setCancelled(true);
            world.spawnEntity(location, singleType, SpawnReason.CUSTOM);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity damaged)) return;
        if (damaged instanceof Player) return;
        if (event.getFinalDamage() < damaged.getHealth()) return;
        if (exudeBlood) {
            damaged.getWorld().spawnParticle(
                    Particle.BLOCK_DUST,
                    damaged.getLocation().add(0, 0.5, 0),
                    15, // Количество частиц
                    0.25, 0.25, 0.25, // Разброс частиц
                    bloodData
            );
        }
        if (fakeDeath) {
            event.setCancelled(true);
            damaged.remove();
            damaged.getWorld().playSound(
                    damaged.getLocation(),
                    Sound.ENTITY_GENERIC_DEATH,
                    0.9f, // Громкость
                    0.9f  // Тон
            );
        }
    }

}