package ru.kvaytg.richworld.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.kvaytg.richworld.RichWorld;

public class DeathHandler extends HideStreamHandler {

    public DeathHandler(RichWorld plugin) {
        super(plugin, "death");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.deathMessage(Component.empty());
    }

}