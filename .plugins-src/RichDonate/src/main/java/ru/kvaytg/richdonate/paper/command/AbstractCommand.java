package ru.kvaytg.richdonate.paper.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;
import java.util.Objects;

public abstract class AbstractCommand implements CommandExecutor {

    private final RichDonate plugin;

    private final String messageNoAccess;

    public AbstractCommand(RichDonate plugin, String name) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand(name)).setExecutor(this);
        messageNoAccess = ColorAPI.colorize("&#FF0000Sorry, but this command is ONLY for players");
    }

    public RichDonate getPlugin() {
        return plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String alias,
                             @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageNoAccess);
            return true;
        }
        onExecute(player, alias, args);
        return true;
    }

    public abstract void onExecute(Player player, String alias, String[] args);

}
