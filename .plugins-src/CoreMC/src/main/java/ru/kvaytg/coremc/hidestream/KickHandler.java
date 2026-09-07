package ru.kvaytg.coremc.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerKickEvent;
import ru.kvaytg.coremc.CoreMc;

public class KickHandler extends HideStreamHandler{

    public KickHandler(CoreMc plugin) {
        super(plugin, "kick");
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        event.leaveMessage(Component.empty());
    }

}