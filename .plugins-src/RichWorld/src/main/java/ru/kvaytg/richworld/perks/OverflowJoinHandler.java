package ru.kvaytg.richworld.perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import ru.kvaytg.richworld.RichWorld;

public class OverflowJoinHandler extends PerkHandler {

    private int slots;

    public OverflowJoinHandler(RichWorld plugin) {
        super(plugin, "overflowJoin");
    }

    @Override
    public void onInit() {
        slots = getConfigInt("slots");
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL)) {
            event.allow();
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        event.setMaxPlayers(slots);
    }

}