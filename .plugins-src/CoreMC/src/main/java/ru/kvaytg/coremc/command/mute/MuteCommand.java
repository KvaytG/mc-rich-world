package ru.kvaytg.coremc.command.mute;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AbstractCommand;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.MuteConfig;
import ru.kvaytg.coremc.config.constants.ConfigParameter;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.config.constants.Placeholder;
import ru.kvaytg.coremc.message.Messages;
import ru.kvaytg.coremc.permission.Permissions;
import ru.kvaytg.coremc.utils.BukkitUtils;

public class MuteCommand extends AbstractCommand {

    private final MuteConfig config;
    private final boolean enabled;

    public MuteCommand(CoreMc plugin) {
        super(plugin, "mute");
        this.config = ConfigManager.INSTANCE.getMuteConfig();
        this.enabled = ConfigManager.INSTANCE.getMainConfig().getBoolean(
                ConfigSection.CHAT.getDotPath().add(ConfigParameter.ENABLED.getName())
        );
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.MUTE_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (!enabled) return;
        if (args.length < 2) {
            Messages.MUTE_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
            return;
        }
        String targetName = args[0];
        Player target = BukkitUtils.getPlayer(targetName);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
            return;
        }
        try {
            long minutes = Long.parseLong(args[1]);
            if (minutes <= 0) {
                Messages.NUMBER_MUST_BE_MORE_THAN_ZERO.send(sender);
                return;
            }
            config.setMute(target.getUniqueId(), minutes * 60 * 1000);
            Messages.MUTE_OTHER.send(
                    sender,
                    Placeholder.PLAYER.get(), targetName,
                    Placeholder.MINUTES.get(), minutes
            );
            Messages.MUTE_OTHERS.send(
                    target,
                    Placeholder.PLAYER.get(), sender.getName(),
                    Placeholder.MINUTES.get(), minutes
            );
        } catch (NumberFormatException notUsed) {
            Messages.NOT_NUMBER.send(sender, Placeholder.VALUE.get(), args[1]);
        }
    }

}