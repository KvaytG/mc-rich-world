package ru.kvaytg.richdonate.paper.update;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.kvaytg.richdonate.ChannelCommand;
import ru.kvaytg.richdonate.paper.RichDonate;
import ru.kvaytg.richdonate.paper.donate.status.StatusManager;

/*
 *
 * Обновитель всех значений на стороне Paper.
 *
 */
public enum ValuesUpdater {

    INSTANCE;

    private BukkitTask task = null;

    public void start(RichDonate plugin) {
        if (task != null && !task.isCancelled()) return;
        task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
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
        }
    }

    private void updateBalance(RichDonate plugin, Player player) {
        plugin.sendPluginMessage(player, ChannelCommand.REQUEST_BALANCE.getText(player.getName()));
    }

    private void updateStatus(RichDonate plugin, Player player) {
        plugin.sendPluginMessage(player, ChannelCommand.REQUEST_STATUS.getText(player.getName()));
    }

    private void updatePermissions(RichDonate plugin, Player player) {
        String status = StatusManager.INSTANCE.getStatus(player);
        plugin.getPermissionManager().updatePermissions(player, status);
    }

}