package ru.kvaytg.richdonate.paper.command;

import org.bukkit.entity.Player;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;
import java.util.Objects;

public class VipCommand extends AbstractCommand {

    public static final int COST = 5000;

    private final String messageEnough;
    private final String messageNotEnough;

    private final String messageAlreadyPurchased;
    private final String messageSuccess;

    public VipCommand(RichDonate plugin) {
        super(plugin, "vip");
        String messageInfo = "&#FFFF31Для получения статуса &aVIP &#FFFF31вам необходимо &#FFAA01" + COST + " ⛂\n";
        messageEnough = ColorAPI.colorize(
                messageInfo +
                "&#FFFF31Вам &aхватает &#FFFF31монеток для покупки\n" +
                "Введите &#FFAA01/vip confirm &#FFFF31для подтверждения"
        );
        messageNotEnough = ColorAPI.colorize(
                messageInfo +
                "&#FFFF31У вас &#FF0000недостаточно &#FFFF31монеток для покупки"
        );
        messageAlreadyPurchased = ColorAPI.colorize(
                "&#FFFF31У вас уже есть &aVIP&#FFFF31-статус"
        );
        messageSuccess = ColorAPI.colorize(
                "&#FFFF31Поздравляем с приобретением &aVIP&#FFFF31-статуса!"
        );
    }

    @Override
    public void onExecute(Player player, String alias, String[] args) {
        if (!Objects.equals(getPlugin().getStatus(player), "default")) {
            player.sendMessage(messageAlreadyPurchased);
            return;
        }
        boolean isEnough = getPlugin().getCoins(player) >= COST;
        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            if (isEnough && getPlugin().takeCoins(player, COST)) {
                getPlugin().giveStatus(player, "vip");
                player.sendMessage(messageSuccess);
            } else {
                player.sendMessage(messageNotEnough);
            }
            return;
        }
        player.sendMessage(isEnough ? messageEnough : messageNotEnough);
    }

}