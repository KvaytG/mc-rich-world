package ru.kvaytg.richdonate.paper.update;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.kvaytg.richdonate.ByteUtils;
import ru.kvaytg.richdonate.ChannelCommand;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richdonate.paper.donate.status.StatusManager;

public enum ValuesUpdater {

    INSTANCE;

    private BukkitTask task;

    public void start(RichDonate plugin) {
        stop();
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                updateBalance(plugin, player);
                updateStatus(plugin, player);
                updatePermissions(plugin, player);
            }
        }, 0L, 10L);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    private void updateBalance(RichDonate plugin, Player player) {
        plugin.sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.REQUEST_BALANCE,
                        player.getUniqueId(),
                        "",
                        0,
                        ""
                )
        );
    }

    private void updateStatus(RichDonate plugin, Player player) {
        plugin.sendPluginMessage(
                player,
                ByteUtils.encode(
                        ChannelCommand.REQUEST_STATUS,
                        player.getUniqueId(),
                        "",
                        0,
                        ""
                )
        );
    }

    private void updatePermissions(RichDonate plugin, Player player) {
        String status = StatusManager.INSTANCE.getStatus(player);
        plugin.getPermissionManager().updatePermissions(player, status);
    }

}