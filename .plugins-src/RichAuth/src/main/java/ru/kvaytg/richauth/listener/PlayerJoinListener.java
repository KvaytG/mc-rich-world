package ru.kvaytg.richauth.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import ru.kvaytg.richauth.RichAuth;

public class PlayerJoinListener implements Listener {

    private final RichAuth plugin;

    public PlayerJoinListener(RichAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        String normalized = plugin.normalizeName(name);
        if (plugin.getLinkedAccounts().containsKey(normalized)) {
            Long chatId = plugin.getLinkedAccounts().get(normalized);
            plugin.getConfirmedPlayers().remove(normalized);
            plugin.bot.sendLoginRequest(name, chatId);
            startAuthReminder(player);
        } else {
            startLinkReminder(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String normalized = plugin.normalizeName(event.getPlayer().getName());
        BukkitTask task = plugin.getReminderTasks().remove(normalized);
        if (task != null) task.cancel();
    }

    private void startAuthReminder(Player player) {
        String name = player.getName();
        String normalized = plugin.normalizeName(name);
        BukkitTask task = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                if (plugin.getConfirmedPlayers().contains(normalized)) {
                    plugin.getReminderTasks().remove(normalized);
                    cancel();
                    return;
                }
                if (count > 12) {
                    player.kickPlayer(RichAuth.TIMEOUT);
                    plugin.getReminderTasks().remove(normalized);
                    cancel();
                    return;
                }
                player.sendMessage(RichAuth.REMINDER);
            }
        }.runTaskTimer(plugin, 0, 100);
        plugin.getReminderTasks().put(normalized, task);
    }

    private void startLinkReminder(Player player) {
        String normalized = plugin.normalizeName(player.getName());
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }
                if (plugin.getLinkedAccounts().containsKey(normalized)) {
                    cancel();
                    return;
                }
                String link = "https://t.me/" + plugin.bot.getBotUsername();
                player.sendMessage(RichAuth.REMINDER_PREFIX + link);
                player.sendMessage(RichAuth.REMINDER_SUFFIX + player.getName());
            }
        }.runTaskTimer(plugin, 0, 100);
        plugin.getReminderTasks().put(normalized, task);
    }

}