package ru.kvaytg.richchat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.neznamy.tab.api.nametag.NameTagManager;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;

public class ChatHandler implements Listener {

    private final RichDonate donateAPI;

    private final TabAPI tabAPI;
    private final NameTagManager nameTagManager;

    private final String messageNotEnough;

    public ChatHandler() {
        donateAPI = RichDonate.getInstance();
        tabAPI = TabAPI.getInstance();
        nameTagManager = tabAPI.getNameTagManager();
        messageNotEnough = ColorAPI.colorize(
                """
                &#FFFF31Недостаточно монет для отправки сообщения.
                1 сообщение = 1 монета.
                Пополните баланс для продолжения использования чата
                """
        );
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (!donateAPI.takeCoins(player, 1)) {
            player.sendMessage(messageNotEnough);
            event.setCancelled(true);
            return;
        }
        TabPlayer tabPlayer = tabAPI.getPlayer(player.getUniqueId());
        String prefix = nameTagManager.getOriginalPrefix(tabPlayer);
        String suffix = nameTagManager.getOriginalSuffix(tabPlayer);
        if (prefix != null && !prefix.isEmpty()) {
            prefix = ColorAPI.colorize(prefix);
        } else {
            prefix = "";
        }
        if (suffix != null && !suffix.isEmpty()) {
            suffix = ColorAPI.colorize(suffix);
        } else {
            suffix = "";
        }
        Component finalMessage = Component.text("")
                .append(Component.text(prefix))
                .append(Component.text(player.getName()))
                .append(Component.text(suffix))
                .append(Component.text(": ", NamedTextColor.WHITE))
                .append(event.message());
        event.renderer((src, srcDisplayName, msg, viewer) -> finalMessage);
    }

}