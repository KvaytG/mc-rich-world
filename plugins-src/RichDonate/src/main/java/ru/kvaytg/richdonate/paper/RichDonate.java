package ru.kvaytg.richdonate.paper;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
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

/*
*
* Главный класс на стороне Paper
*
*/
public class RichDonate extends JavaPlugin {

    private static RichDonate instance;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        instance = this;
        permissionManager = new PermissionManager(this);
        CoinsManager.INSTANCE.init();
        StatusManager.INSTANCE.init();
        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, Channel.NAME);
        messenger.registerIncomingPluginChannel(this, Channel.NAME, new ChannelHandler());
        ValuesUpdater.INSTANCE.start(this);
        new RichDonateExpansion(this).register();
        new CoinsCommand(this);
        new VipCommand(this);
        new DonateCommand(this);
    }

    @Override
    public void onDisable() {
        ValuesUpdater.INSTANCE.stop();
        for (Player player : getServer().getOnlinePlayers()) {
            PermissionUtil.clear(player);
            player.closeInventory();
        }
    }

    public static RichDonate getInstance() {
        return instance;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void sendPluginMessage(Player player, String message) {
        player.sendPluginMessage(this, Channel.NAME, ByteUtils.stringToBytes(message));
    }

    @SuppressWarnings("unused")
    public void giveCoins(@NotNull Player player, int amount) {
        sendPluginMessage(
                player,
                ChannelCommand.BALANCE_GIVE.getText(player.getName(), amount)
        );
        player.sendActionBar(Component.text("+ " + amount + " ⛂", NamedTextColor.GREEN));
    }

    @SuppressWarnings("unused")
    public boolean takeCoins(@NotNull Player player, int amount) {
        int balance = CoinsManager.INSTANCE.getBalance(player);
        if (balance < amount) return false;
        sendPluginMessage(
                player,
                ChannelCommand.BALANCE_TAKE.getText(player.getName(), amount)
        );
        player.sendActionBar(Component.text("- " + amount + " ⛂", NamedTextColor.RED));
        return true;
    }

    @SuppressWarnings("unused")
    public int getCoins(@NotNull Player player) {
        return CoinsManager.INSTANCE.getBalance(player);
    }

    @SuppressWarnings("unused")
    public void giveStatus(@NotNull Player player, String status) {
        sendPluginMessage(
                player,
                ChannelCommand.STATUS_GIVE.getText(player.getName(), status.toLowerCase())
        );
    }

    @SuppressWarnings("unused")
    public void takeStatus(@NotNull Player player, String reason) {
        sendPluginMessage(
                player,
                ChannelCommand.STATUS_TAKE.getText(player.getName(), reason)
        );
    }

    @SuppressWarnings("unused")
    public String getStatus(@NotNull Player player) {
        return StatusManager.INSTANCE.getStatus(player);
    }

}