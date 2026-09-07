package ru.kvaytg.coremc.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.kvaytg.coremc.CoreMc;

public class DeathHandler extends HideStreamHandler {

    public DeathHandler(CoreMc plugin) {
        super(plugin, "death");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(Component.empty());
    }

}