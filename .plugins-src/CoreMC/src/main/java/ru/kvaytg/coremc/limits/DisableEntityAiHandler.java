package ru.kvaytg.coremc.limits;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.BukkitUtils;

public class DisableEntityAiHandler extends LimitHandler {

    public DisableEntityAiHandler(CoreMc plugin) {
        super(plugin, "mobAi");
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (getWorlds().contains(entity.getWorld())) {
            BukkitUtils.disableAI(entity);
        }
    }

}