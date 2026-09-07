package ru.kvaytg.coremc.console;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AutoConfigurableHandler;
import ru.kvaytg.coremc.config.constants.ConfigSection;

public class ConnectionHandler extends AutoConfigurableHandler {

    private String joinMessage;
    private String quitMessage;

    public ConnectionHandler(CoreMc plugin) {
        super(plugin, ConfigSection.CONSOLE.getDotPath(), "logJoinAndQuit");
    }

    @Override
    public void onInit() {
        joinMessage = "%s joined the server";
        quitMessage = "%s left the server";
    }

    private void log(String message, PlayerEvent event) {
        Bukkit.getLogger().info(String.format(message, event.getPlayer().getName()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        log(joinMessage, event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        log(quitMessage, event);
    }

}