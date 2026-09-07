package ru.kvaytg.richchat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;

public class ChatHandler implements Listener {

    private final RichDonate donateApi;

    private final int messageCost;

    private final String messageNotEnoughCoins;

    public ChatHandler(RichChat plugin, RichDonate donateApi) {
        this.donateApi = donateApi;
        messageCost = plugin.getConfig().getInt("cost", 1);
        messageNotEnoughCoins = ColorAPI.colorize(
                plugin.getConfig().getString("messages.notEnoughCoins", "")
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncChatEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (!donateApi.takeCoins(player, messageCost)) {
            if (!messageNotEnoughCoins.isBlank()) {
                player.sendMessage(messageNotEnoughCoins);
            }
            event.setCancelled(true);
        }
    }

}