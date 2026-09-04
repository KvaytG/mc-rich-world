package ru.kvaytg.richworld.hidestream;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kvaytg.richworld.RichWorld;

public class QuitHandler extends HideStreamHandler{

    public QuitHandler(RichWorld plugin) {
        super(plugin, "exit");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.empty());
    }

}