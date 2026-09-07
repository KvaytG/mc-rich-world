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

public class UnmuteCommand extends AbstractCommand {

    private final MuteConfig config;
    private final boolean enabled;

    public UnmuteCommand(CoreMc plugin) {
        super(plugin, "unmute");
        this.config = ConfigManager.INSTANCE.getMuteConfig();
        this.enabled = ConfigManager.INSTANCE.getMainConfig().getBoolean(
                ConfigSection.CHAT.getDotPath().add(ConfigParameter.ENABLED.getName())
        );
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.UNMUTE_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (!enabled) return;
        if (args.length < 1) {
            Messages.UNMUTE_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
            return;
        }
        String targetName = args[0];
        Player target = BukkitUtils.getPlayer(targetName);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
            return;
        }
        config.unmute(target.getUniqueId());
        Messages.UNMUTE_OTHER.send(sender, Placeholder.PLAYER.get(), targetName);
    }

}