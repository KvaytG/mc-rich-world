package ru.kvaytg.coremc.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.coremc.CoreMc;

public class JoinHandler extends HideStreamHandler {

    public JoinHandler(CoreMc plugin) {
        super(plugin, "join");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.empty());
    }

}