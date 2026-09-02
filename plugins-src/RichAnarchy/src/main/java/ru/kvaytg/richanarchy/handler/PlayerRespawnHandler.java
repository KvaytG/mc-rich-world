package ru.kvaytg.richanarchy.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import ru.kvaytg.richanarchy.RichAnarchy;

public class PlayerRespawnHandler extends AbstractListener {

    public PlayerRespawnHandler(RichAnarchy plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        event.getPlayer().setNoDamageTicks(300);
    }

}