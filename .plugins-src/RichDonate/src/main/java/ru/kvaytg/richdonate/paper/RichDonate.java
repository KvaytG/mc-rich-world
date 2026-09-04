package ru.kvaytg.richdonate.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.richdonate.ByteUtils;
import ru.kvaytg.richdonate.Channel;
import ru.kvaytg.richdonate.ChannelCommand;
import ru.kvaytg.richdonate.paper.command.CoinsCommand;
import ru.kvaytg.richdonate.paper.command.DonateCommand;
import ru.kvaytg.richdonate.paper.command.VipCommand;
import ru.kvaytg.richdonate.paper.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.paper.donate.status.StatusManager;
import ru.kvaytg.richdonate.paper.expansion.RichDonateExpansion;
import ru.kvaytg.richdonate.paper.permission.PermissionManager;
import ru.kvaytg.richdonate.paper.permission.PermissionUtil;
import ru.kvaytg.richdonate.paper.update.ValuesUpdater;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class RichDonate extends JavaPlugin {

    private static RichDonate instance;
    private PermissionManager permissionManager;
    private final Map<String, PendingPurchase> pendingPurchases = new ConcurrentHashMap<>();
    private final Map<UUID, String> pendingPurchasesByPlayer = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        permissionManager = new PermissionManager(this);

        CoinsManager.INSTANCE.init();
        StatusManager.INSTANCE.init();

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, Channel.NAME);
        messenger.registerIncomingPluginChannel(this, Channel.NAME, new ChannelHandler());

        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                PermissionUtil.clear(event.getPlayer());
                CoinsManager.INSTANCE.remove(event.getPlayer());
                StatusManager.INSTANCE.remove(event.getPlayer());
                pendingPurchasesByPlayer.remove(event.getPlayer().getUniqueId());
            }
        }, this);

        ValuesUpdater.INSTANCE.start(this);
        new RichDonateExpansion(this).register();
        new CoinsCommand(this);
        new VipCommand(this);
        new DonateCommand(this);
    }

    @Override
    public void onDisable() {
        pendingPurchases.clear();
        pendingPurchasesByPlayer.clear();
        ValuesUpdater.INSTANCE.stop();

        for (Player player : getServer().getOnlinePlayers()) {
            PermissionUtil.clear(player);
            player.closeInventory();
        }

        instance = null;
    }

    public static RichDonate getInstance() {
        return instance;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void sendPluginMessage(Player player, byte[] data) {
        if (player == null || !player.isOnline()) return;
        player.sendPluginMessage(this, Channel.NAME, data);
    }

    /**
     * Legacy ABI method. Keep this exact signature because existing plugins
     * compiled against RichDonate 1.x may still invoke it.
     */
    @SuppressWarnings("unused")
    public void giveCoins(@NotNull Player player, int amount) {
        giveCoinsLong(player, amount);
    }

    public void giveCoinsLong(@NotNull Player player, long amount) {
        if (amount <= 0) return;

        sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.BALANCE_GIVE,
                        player.getUniqueId(),
                        UUID.randomUUID().toString(),
                        amount,
                        ""
                )
        );
        player.sendActionBar(Component.text("+ " + amount + " ⛂", NamedTextColor.GREEN));
    }

    @SuppressWarnings("unused")
    public boolean takeCoins(@NotNull Player player, int amount) {
        return takeCoinsLong(player, amount);
    }

    public boolean takeCoinsLong(@NotNull Player player, long amount) {
        if (amount <= 0) return false;

        long balance = CoinsManager.INSTANCE.getBalance(player);
        if (balance < amount) return false;

        sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.BALANCE_TAKE,
                        player.getUniqueId(),
                        UUID.randomUUID().toString(),
                        amount,
                        ""
                )
        );
        player.sendActionBar(Component.text("- " + amount + " ⛂", NamedTextColor.RED));
        return true;
    }

    /**
     * Legacy ABI method. The original API returned int, so retain it for
     * already-compiled plugins such as RichMobs. New code should use
     * {@link #getCoinsLong(Player)}.
     */
    @SuppressWarnings("unused")
    public int getCoins(@NotNull Player player) {
        long balance = getCoinsLong(player);
        if (balance > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        if (balance < Integer.MIN_VALUE) return Integer.MIN_VALUE;
        return (int) balance;
    }

    public long getCoinsLong(@NotNull Player player) {
        return CoinsManager.INSTANCE.getBalance(player);
    }

    public boolean isPurchasePending(@NotNull Player player) {
        return pendingPurchasesByPlayer.containsKey(player.getUniqueId());
    }

    public boolean purchaseVip(@NotNull Player player, long amount, @NotNull Consumer<Boolean> callback) {
        if (amount <= 0 || !player.isOnline()) return false;

        UUID playerId = player.getUniqueId();
        if (pendingPurchasesByPlayer.putIfAbsent(playerId, "pending") != null) {
            return false;
        }

        String transactionId = UUID.randomUUID().toString();
        pendingPurchases.put(transactionId, new PendingPurchase(playerId, callback));
        pendingPurchasesByPlayer.put(playerId, transactionId);

        sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.PURCHASE_VIP,
                        player.getUniqueId(),
                        transactionId,
                        amount,
                        "vip"
                )
        );
        return true;
    }

    public void resolvePurchase(String transactionId, UUID playerId, boolean success, long newBalance) {
        PendingPurchase pending = pendingPurchases.remove(transactionId);
        if (pending == null || !pending.playerId().equals(playerId)) return;
        pendingPurchasesByPlayer.remove(playerId, transactionId);

        Player player = getServer().getPlayer(playerId);
        if (player == null || !player.isOnline()) return;

        CoinsManager.INSTANCE.setBalance(player, Math.max(0L, newBalance));
        if (success) {
            StatusManager.INSTANCE.setStatus(player, "vip");
        }
        pending.callback().accept(success);
    }

    private record PendingPurchase(UUID playerId, Consumer<Boolean> callback) {
    }

    @SuppressWarnings("unused")
    public void giveStatus(@NotNull Player player, @NotNull String status) {
        if (status.isBlank()) return;
        sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.STATUS_GIVE,
                        player.getUniqueId(),
                        UUID.randomUUID().toString(),
                        0,
                        status
                )
        );
    }

    @SuppressWarnings("unused")
    public void takeStatus(@NotNull Player player, @NotNull String reason) {
        sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.STATUS_TAKE,
                        player.getUniqueId(),
                        UUID.randomUUID().toString(),
                        0,
                        reason
                )
        );
    }

    public String getStatus(@NotNull Player player) {
        return StatusManager.INSTANCE.getStatus(player);
    }

}