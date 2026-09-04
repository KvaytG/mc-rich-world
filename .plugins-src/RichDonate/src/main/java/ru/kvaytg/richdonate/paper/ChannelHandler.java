package ru.kvaytg.richdonate.paper;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.richdonate.Channel;
import ru.kvaytg.richdonate.ByteUtils;
import ru.kvaytg.richdonate.ChannelCommand;
import ru.kvaytg.richdonate.paper.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.paper.donate.status.StatusManager;

/*
 *
 * Обработчик канала на стороне Paper
 *
 * Устанавливает:
 * 1. Балансы игроков в Менеджере монеток,
 * 2. Статусы в Менеджере статусов
 * путём получение ответов со стороны Velocity
 *
 */
public class ChannelHandler implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel,
                                        @NotNull Player player,
                                        byte @NotNull [] data) {
        if (!channel.equals(Channel.NAME)) return;
        String message = ByteUtils.bytesToString(data);
        String[] messageArray = message.split(" ");
        if (!player.getName().equals(messageArray[1])) return;
        String messageName = messageArray[0];
        if (messageName.equals(ChannelCommand.RESPONSE_BALANCE.getName())) {
            CoinsManager.INSTANCE.setBalance(player, Integer.parseInt(messageArray[2]));
        } else if (messageName.equals(ChannelCommand.RESPONSE_STATUS.getName())) {
            StatusManager.INSTANCE.setStatus(player, messageArray[2]);
        }
    }

}