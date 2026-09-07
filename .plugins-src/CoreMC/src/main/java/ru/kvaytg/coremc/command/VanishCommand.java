package ru.kvaytg.coremc.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AbstractCommand;
import ru.kvaytg.coremc.config.constants.Placeholder;
import ru.kvaytg.coremc.message.Messages;
import ru.kvaytg.coremc.permission.Permissions;
import ru.kvaytg.coremc.utils.BukkitUtils;
import ru.kvaytg.coremc.vanish.VanishManager;
import ru.kvaytg.coremc.vanish.VanishStatus;
import java.util.HashMap;
import java.util.Map;

public class VanishCommand extends AbstractCommand {

    private final VanishManager vanishManager;

    private final Map<VanishStatus, Messages> meMessages = new HashMap<>();
    private final Map<VanishStatus, Messages> meOtherMessages = new HashMap<>();
    private final Map<VanishStatus, Messages> othersMessages = new HashMap<>();

    public VanishCommand(CoreMc plugin) {
        super(plugin, "vanish");
        vanishManager = VanishManager.INSTANCE;
        meMessages.put(VanishStatus.ENABLED, Messages.VANISH_HIDE_ME);
        meMessages.put(VanishStatus.DISABLED, Messages.VANISH_SHOW_ME);
        meOtherMessages.put(VanishStatus.ENABLED, Messages.VANISH_HIDE_OTHER);
        meOtherMessages.put(VanishStatus.DISABLED, Messages.VANISH_SHOW_OTHER);
        othersMessages.put(VanishStatus.ENABLED, Messages.VANISH_OTHERS_HIDE);
        othersMessages.put(VanishStatus.DISABLED, Messages.VANISH_OTHERS_SHOW);
    }

    private void toggleVanish(Player target, CommandSender sender, boolean isSelf) {
        VanishStatus status = vanishManager.switchVanish(target);
        if (isSelf) {
            meMessages.get(status).send(target);
        } else {
            meOtherMessages.get(status).send(sender, Placeholder.PLAYER.get(), target.getName());
            othersMessages.get(status).send(target, Placeholder.PLAYER.get(), sender.getName());
        }
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.VANISH_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                Messages.VANISH_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
                return;
            }
            if (Permissions.VANISH_ME.hasNo(player)) {
                Messages.NO_ACCESS.send(sender);
                return;
            }
            toggleVanish(player, sender, true);
            return;
        }
        if (Permissions.VANISH_OTHERS.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        String targetName = args[0];
        Player target = BukkitUtils.getPlayer(targetName);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
            return;
        }
        toggleVanish(target, sender, sender.equals(target));
    }

}