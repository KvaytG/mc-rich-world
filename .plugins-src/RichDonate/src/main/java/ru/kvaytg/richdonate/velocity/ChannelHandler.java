package ru.kvaytg.richdonate.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import ru.kvaytg.richdonate.ByteUtils;
import ru.kvaytg.richdonate.ChannelCommand;
import ru.kvaytg.richdonate.velocity.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.velocity.donate.status.StatusManager;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public record ChannelHandler(ProxyServer proxy) {

    private static final Logger LOGGER = Logger.getLogger(ChannelHandler.class.getName());

    public void sendMessage(UUID playerId, ChannelCommand command,
                            String transactionId, long amount, String text) {
        proxy.getPlayer(playerId).ifPresent(player -> {
            Optional<ServerConnection> connection = player.getCurrentServer();
            connection.ifPresent(server -> server.sendPluginMessage(
                    Identifier.get(),
                    ByteUtils.encode(command, playerId, transactionId, amount, text)
            ));
        });
    }

    @Subscribe
    public void onPluginMessageFromPlugin(PluginMessageEvent event) {
        if (!(event.getSource() instanceof ServerConnection source)) return;
        if (!event.getIdentifier().equals(Identifier.get())) return;
        byte[] data = event.getData();
        try {
            ByteUtils.Packet packet = ByteUtils.decode(data);
            Optional<Player> player = proxy.getPlayer(packet.playerId());
            if (player.isEmpty()
                    || player.get().getCurrentServer().isEmpty()
                    || !player.get().getCurrentServer().get().getServerInfo().getName()
                    .equals(source.getServerInfo().getName())) {
                LOGGER.warning("Rejected RichDonate message from non-current backend for "
                        + packet.playerId());
                return;
            }
            handle(packet);
        } catch (IOException | RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Rejected malformed RichDonate plugin message", ex);
        }
    }

    private void handle(ByteUtils.Packet packet) {
        UUID playerId = packet.playerId();
        proxy.getPlayer(playerId).ifPresent(player -> {
            CoinsManager.INSTANCE.migrateLegacyName(player.getUsername(), playerId);
            StatusManager.INSTANCE.migrateLegacyName(player.getUsername(), playerId);
        });
        switch (packet.command()) {
            case BALANCE_GIVE:
                if (validAmount(packet.amount())) {
                    CoinsManager.INSTANCE.giveCoins(
                            playerId, packet.amount(), packet.transactionId()
                    );
                }
                break;
            case BALANCE_TAKE:
                if (validAmount(packet.amount())) {
                    CoinsManager.INSTANCE.takeCoins(
                            playerId, packet.amount(), packet.transactionId()
                    );
                }
                break;
            case PURCHASE_VIP:
                if (!validAmount(packet.amount())) {
                    sendMessage(playerId, ChannelCommand.RESPONSE_PURCHASE,
                            packet.transactionId(), CoinsManager.INSTANCE.getCoins(playerId), "FAIL");
                    break;
                }
                if (!"vip".equals(packet.text())
                        || !"default".equals(StatusManager.INSTANCE.getStatus(playerId))) {
                    sendMessage(playerId, ChannelCommand.RESPONSE_PURCHASE,
                            packet.transactionId(), CoinsManager.INSTANCE.getCoins(playerId), "FAIL");
                    break;
                }
                boolean charged = CoinsManager.INSTANCE.takeCoinsIfEnough(
                        playerId, packet.amount(), packet.transactionId()
                );
                if (charged) {
                    StatusManager.INSTANCE.giveStatus(
                            playerId, "vip", packet.transactionId() + ":status"
                    );
                }
                sendMessage(playerId, ChannelCommand.RESPONSE_PURCHASE,
                        packet.transactionId(),
                        CoinsManager.INSTANCE.getCoins(playerId),
                        charged ? "OK" : "FAIL");
                break;
            case REQUEST_BALANCE:
                sendMessage(
                        playerId,
                        ChannelCommand.RESPONSE_BALANCE,
                        "",
                        CoinsManager.INSTANCE.getCoins(playerId),
                        ""
                );
                break;
            case STATUS_GIVE:
                StatusManager.INSTANCE.giveStatus(
                        playerId, packet.text(), packet.transactionId()
                );
                break;
            case STATUS_TAKE:
                StatusManager.INSTANCE.takeStatus(
                        playerId, packet.transactionId()
                );
                break;
            case REQUEST_STATUS:
                sendMessage(
                        playerId,
                        ChannelCommand.RESPONSE_STATUS,
                        "",
                        0,
                        StatusManager.INSTANCE.getStatus(playerId)
                );
                break;
            default:
                break;
        }
    }

    private boolean validAmount(long amount) {
        return amount > 0;
    }

}