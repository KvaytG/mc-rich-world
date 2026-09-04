package ru.kvaytg.richhub.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richhub.RichHub;
import java.util.Arrays;

public class JoinHandler implements Listener {

    private static final int MONEY_AMOUNT = 100;
    private static final RichDonate DONATE_API = RichDonate.getInstance();
    private static final String[] WELCOME_MESSAGES = Arrays.stream(new String[] {
            "&#FFAA01&m          &#FFFF31&m          &#FFAA01&m          &#FFFF31&m          &#FFAA01&m          &#FFFF31&m          ",
            "",
            "    &#FFFF31Добро пожаловать на проект &#FFAA01RichWorld",
            "",
            "            &#FFFF31Помощь по командам: &#FFAA01/help",
            "                &#FFFF31Выбор режима: &#FFAA01/menu",
            "",
            "              &#FFFF31Наш сайт: &#FFAA01rich-world.ru",
            "",
            "&#FFAA01&m          &#FFFF31&m          &#FFAA01&m          &#FFFF31&m          &#FFAA01&m          &#FFFF31&m          "
    }).map(ColorAPI::colorize).toArray(String[]::new);
    private static final String BONUS_MESSAGE = String.format(
            ColorAPI.colorize("&#FFFF31Вы получили &#FFAA01%d &#FFFF31бонусных монет!"),
            MONEY_AMOUNT
    );

    private final RichHub plugin;

    public JoinHandler(RichHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            for (String message : WELCOME_MESSAGES) {
                player.sendMessage(message);
            }
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (player.isOnline()) {
                    DONATE_API.giveCoins(player, MONEY_AMOUNT);
                    player.sendMessage(BONUS_MESSAGE);
                }
            }, 40L);
        }
    }

}