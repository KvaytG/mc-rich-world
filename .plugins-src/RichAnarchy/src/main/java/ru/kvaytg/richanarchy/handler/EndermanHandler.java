package ru.kvaytg.richanarchy.handler;

import org.bukkit.World;
import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import ru.kvaytg.richanarchy.RichAnarchy;

public class EndermanHandler extends AbstractListener{

    public EndermanHandler(RichAnarchy plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof Enderman)) return;
        World world = event.getBlock().getWorld();
        if (world.getEnvironment() == World.Environment.NORMAL) {
            event.setCancelled(true);
        }
    }

}