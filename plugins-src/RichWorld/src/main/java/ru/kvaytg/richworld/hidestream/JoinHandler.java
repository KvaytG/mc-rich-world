package ru.kvaytg.richworld.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.richworld.RichWorld;

public class JoinHandler extends HideStreamHandler {

    public JoinHandler(RichWorld plugin) {
        super(plugin, "join");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());
    }

}