package ru.kvaytg.richdonate.paper;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.richdonate.ByteUtils;
import ru.kvaytg.richdonate.Channel;
import ru.kvaytg.richdonate.paper.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.paper.donate.status.StatusManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ChannelHandler implements PluginMessageListener {

    private static final Logger LOGGER = Logger.getLogger(ChannelHandler.class.getName());

    @Override
    public void onPluginMessageReceived(@NotNull String channel,
                                        @NotNull Player player,
                                        byte @NotNull [] data) {
        if (!Channel.NAME.equals(channel)) return;
        try {
            ByteUtils.Packet packet = ByteUtils.decode(data);
            if (!player.getUniqueId().equals(packet.playerId())) return;
            switch (packet.command()) {
                case RESPONSE_BALANCE:
                    if (packet.amount() < 0 || packet.amount() > Integer.MAX_VALUE) return;
                    CoinsManager.INSTANCE.setBalance(player, packet.amount());
                    break;
                case RESPONSE_STATUS:
                    StatusManager.INSTANCE.setStatus(player, packet.text());
                    break;
                case RESPONSE_PURCHASE:
                    if (packet.amount() < 0) return;
                    if (!"OK".equals(packet.text()) && !"FAIL".equals(packet.text())) return;
                    RichDonate.getInstance().resolvePurchase(
                            packet.transactionId(),
                            player.getUniqueId(),
                            "OK".equals(packet.text()),
                            packet.amount()
                    );
                    break;
                default:
                    break;
            }
        } catch (IOException | RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Rejected malformed RichDonate plugin message", ex);
        }
    }

}