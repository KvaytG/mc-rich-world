package ru.kvaytg.richworld.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import ru.kvaytg.richworld.RichWorld;

public class KickHandler extends HideStreamHandler{

    public KickHandler(RichWorld plugin) {
        super(plugin, "kick");
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.leaveMessage(Component.empty());
    }

}