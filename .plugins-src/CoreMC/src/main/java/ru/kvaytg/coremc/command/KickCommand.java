package ru.kvaytg.coremc.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AbstractCommand;
import ru.kvaytg.coremc.config.constants.Placeholder;
import ru.kvaytg.coremc.message.Messages;
import ru.kvaytg.coremc.permission.Permissions;
import ru.kvaytg.coremc.utils.BukkitUtils;
import java.util.Arrays;

public class KickCommand extends AbstractCommand {

    public KickCommand(CoreMc plugin) {
        super(plugin, "kick");
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.KICK_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (args.length < 2) {
            Messages.KICK_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
            return;
        }
        String targetName = args[0];
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Component reasonComponent = Component.text(reason);
        if (targetName.equalsIgnoreCase("@all")) {
            if (sender instanceof Player) {
                Messages.NO_ACCESS.send(sender);
                return;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kick(reasonComponent);
            }
            Messages.KICK_ALL.send(sender, Placeholder.REASON.get(), reason);
        } else {
            Player target = BukkitUtils.getPlayer(targetName);
            if (target == null) {
                Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
                return;
            }
            target.kick(reasonComponent);
            Messages.KICK_OTHER.send(sender,
                    Placeholder.PLAYER.get(), targetName,
                    Placeholder.REASON.get(), reason
            );
        }
    }

}