package ru.kvaytg.coremc.component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;
import ru.kvaytg.coremc.CoreMc;

public abstract class AbstractCommand extends AutoRegistered implements CommandExecutor {

    public AbstractCommand(CoreMc plugin, String name) {
        super(plugin, true, name.toLowerCase());
    }

    @Override
    public void register(CoreMc plugin) {
        PluginCommand command = plugin.getCommand(getName());
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        execute(sender, alias, args);
        return true;
    }

    public abstract void execute(CommandSender sender, String alias, String[] args);

}