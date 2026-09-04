package ru.kvaytg.richdonate.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import ru.kvaytg.richdonate.ChannelCommand;
import ru.kvaytg.richdonate.ByteUtils;
import ru.kvaytg.richdonate.velocity.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.velocity.donate.status.StatusManager;
import java.util.Optional;

/*
*
* Обработчик канала на стороне Velocity
*
* Отслеживает запросы на получение баланса и статуса,
* после чего отсылает ответы на них на сторону Paper
*
*/
public class ChannelHandler {

    private final ProxyServer proxy;

    public ChannelHandler(ProxyServer proxy) {
        this.proxy = proxy;
    }

    public void sendMessage(String playerName, String message) {
        proxy.getPlayer(playerName).ifPresent((player) -> {
            byte[] bytes = ByteUtils.stringToBytes(message);
            Optional<ServerConnection> connection = player.getCurrentServer();
            connection.ifPresent(serverConnection -> serverConnection.sendPluginMessage(Identifier.get(), bytes));
        });
    }

    @Subscribe
    public void onPluginMessageFromPlugin(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection)) return;
        if (event.getIdentifier() != Identifier.get()) return;
        String message = ByteUtils.bytesToString(event.getData());
        String[] messageArray = message.split(" ");
        String messageName = messageArray[0];
        String playerName = messageArray[1];
        if (messageName.equals(ChannelCommand.BALANCE_GIVE.getName())) {
            int amount = Integer.parseInt(messageArray[2]);
            CoinsManager.INSTANCE.giveCoins(playerName, amount);
        } else if (messageName.equals(ChannelCommand.BALANCE_TAKE.getName())) {
            int amount = Integer.parseInt(messageArray[2]);
            CoinsManager.INSTANCE.takeCoins(playerName, amount);
        } else if (messageName.equals(ChannelCommand.REQUEST_BALANCE.getName())) {
            int balance = CoinsManager.INSTANCE.getCoins(playerName);
            sendMessage(playerName, ChannelCommand.RESPONSE_BALANCE.getText(playerName, balance));
        } else if (messageName.equals(ChannelCommand.STATUS_GIVE.getName())) {
            String status = messageArray[2];
            StatusManager.INSTANCE.giveStatus(playerName, status);
        } else if (messageName.equals(ChannelCommand.STATUS_TAKE.getName())) {
            StatusManager.INSTANCE.takeStatus(playerName);
        } else if (messageName.equals(ChannelCommand.REQUEST_STATUS.getName())) {
            String status = StatusManager.INSTANCE.getStatus(playerName);
            sendMessage(playerName, ChannelCommand.RESPONSE_STATUS.getText(playerName, status));
        }
    }

}