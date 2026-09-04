package ru.kvaytg.richauth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class LinkCommand implements CommandExecutor {

    private final RichAuth plugin;

    public LinkCommand(RichAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String alias,
                             @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        String name = player.getName();
        String normalized = plugin.normalizeName(name);
        if (args.length != 1) {
            player.sendMessage(RichAuth.USAGE);
            return true;
        }
        PendingLink pending = plugin.getPendingLinks().get(normalized);
        if (pending == null) {
            player.sendMessage(RichAuth.CODE_NOT_FOUND);
            return true;
        }
        if (System.currentTimeMillis() - pending.createdAt > 300_000) {
            player.sendMessage(RichAuth.CODE_EXPIRED);
            plugin.getPendingLinks().remove(normalized);
            return true;
        }
        if (!pending.code.equals(args[0])) {
            player.sendMessage(RichAuth.CODE_INVALID);
            return true;
        }
        plugin.getLinkedAccounts().put(normalized, pending.chatId);
        plugin.getPendingLinks().remove(normalized);
        plugin.saveLinkedAccounts();
        player.sendMessage(RichAuth.LINK_SUCCESS);
        plugin.bot.sendMessage(pending.chatId, "Аккаунт " + name + " успешно привязан!");
        if (player.isOnline()) {
            plugin.bot.sendLoginRequest(name, pending.chatId);
        }
        return true;
    }

}