package ru.kvaytg.richauth.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.setCancelled(true);
    }

}