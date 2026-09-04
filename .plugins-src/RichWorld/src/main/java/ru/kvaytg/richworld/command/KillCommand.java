package ru.kvaytg.richworld.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AbstractCommand;
import ru.kvaytg.richworld.config.constants.Placeholder;
import ru.kvaytg.richworld.message.Messages;
import ru.kvaytg.richworld.permission.Permissions;
import ru.kvaytg.richworld.utils.BukkitUtils;

public class KillCommand extends AbstractCommand {

    public KillCommand(RichWorld plugin) {
        super(plugin, "kill");
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.KILL_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (args.length != 1) {
            Messages.KILL_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
            return;
        }
        String targetName = args[0];
        if (targetName.equalsIgnoreCase("@all")) {
            if (sender instanceof Player) {
                Messages.NO_ACCESS.send(sender);
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setHealth(0.0);
            }
            Messages.KILL_ALL.send(sender);
        } else {
            Player target = BukkitUtils.getPlayer(targetName);
            if (target == null) {
                Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
                return;
            }
            target.setHealth(0.0);
            Messages.KILL_OTHER.send(sender, Placeholder.PLAYER.get(), targetName);
        }
    }

}