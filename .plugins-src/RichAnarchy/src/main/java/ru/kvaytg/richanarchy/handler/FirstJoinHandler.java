package ru.kvaytg.richanarchy.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richanarchy.RichAnarchy;

public class FirstJoinHandler extends AbstractListener {

    private final String message;

    public FirstJoinHandler(RichAnarchy plugin) {
        super(plugin);
        message = ColorAPI.colorize(
                "&#FFFF31Введите &#FFAA01/info &#FFFF31для справки"
        );
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            player.sendMessage(message);
        }
    }

}
