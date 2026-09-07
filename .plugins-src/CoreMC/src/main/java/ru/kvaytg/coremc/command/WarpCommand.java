package ru.kvaytg.coremc.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AbstractCommand;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.constants.Placeholder;
import ru.kvaytg.coremc.message.Messages;
import ru.kvaytg.coremc.permission.Permissions;
import ru.kvaytg.coremc.warp.Warp;
import ru.kvaytg.coremc.config.WarpConfig;
import ru.kvaytg.coremc.utils.BukkitUtils;

public class WarpCommand extends AbstractCommand {

    private final WarpConfig warpConfig;

    public WarpCommand(CoreMc plugin) {
        super(plugin, "warp");
        warpConfig = ConfigManager.INSTANCE.getWarpConfig();
    }

    private boolean teleport(Player player, String warpName) {
        Warp warp = warpConfig.getWarp(warpName);
        if (warp != null) {
            warp.teleport(player);
            return true;
        }
        return false;
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.WARP_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (args.length < 1) {
            Messages.WARP_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
            return;
        }
        String warpName = args[0].toLowerCase();
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                Messages.WARP_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
                return;
            }
            if (Permissions.WARP_ME.hasNo(player)) {
                Messages.NO_ACCESS.send(player);
                return;
            }
            if (!teleport(player, warpName)) {
                Messages.WARP_NOT_FOUND.send(player, Placeholder.WARP.get(), warpName);
            }
            return;
        }
        if (args.length == 2) {
            if (Permissions.WARP_OTHERS.hasNo(sender)) {
                Messages.NO_ACCESS.send(sender);
                return;
            }
            String targetName = args[1];
            Player target = BukkitUtils.getPlayer(targetName);
            if (target == null) {
                Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
                return;
            }
            if (!teleport(target, warpName)) {
                Messages.WARP_NOT_FOUND.send(sender, Placeholder.WARP.get(), warpName);
                return;
            }
            if (target.equals(sender)) return;
            Messages.WARP_OTHER.send(sender,
                    Placeholder.PLAYER.get(), targetName,
                    Placeholder.WARP.get(), warpName
            );
        }
    }

}