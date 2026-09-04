package ru.kvaytg.richanarchy.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.richanarchy.RichAnarchy;

public abstract class AbstractCommand implements CommandExecutor {

    public AbstractCommand(RichAnarchy plugin, String name) {
        PluginCommand command = plugin.getCommand(name);
        if (command != null) command.setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String alias,
                             @NotNull String[] args) {
        execute(sender, alias, args);
        return true;
    }

    public abstract void execute(CommandSender sender, String alias, String[] args);

}