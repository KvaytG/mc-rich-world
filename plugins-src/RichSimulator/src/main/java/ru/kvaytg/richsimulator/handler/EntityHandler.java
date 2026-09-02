package ru.kvaytg.richsimulator.handler;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import ru.kvaytg.richsimulator.RichSimulator;

public class EntityHandler extends AbstractListener {

    public EntityHandler(RichSimulator plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntityType() == EntityType.SLIME) {
            Slime slime = (Slime) event.getEntity();
            if (slime.getSize() != 3) {
                slime.setSize(3);
            }
        }
    }

}