package ru.kvaytg.coremc.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AbstractCommand;
import ru.kvaytg.coremc.config.constants.Placeholder;
import ru.kvaytg.coremc.fly.FlyManager;
import ru.kvaytg.coremc.fly.FlyStatus;
import ru.kvaytg.coremc.message.Messages;
import ru.kvaytg.coremc.permission.Permissions;
import ru.kvaytg.coremc.utils.BukkitUtils;
import java.util.HashMap;
import java.util.Map;

public class FlyCommand extends AbstractCommand {

    private final FlyManager flyManager;

    private final Map<FlyStatus, Messages> meMessages = new HashMap<>();
    private final Map<FlyStatus, Messages> meOtherMessages = new HashMap<>();
    private final Map<FlyStatus, Messages> othersMessages = new HashMap<>();

    public FlyCommand(CoreMc plugin) {
        super(plugin, "fly");
        flyManager = FlyManager.INSTANCE;
        meMessages.put(FlyStatus.ENABLED, Messages.FLY_ON_ME);
        meMessages.put(FlyStatus.DISABLED, Messages.FLY_OFF_ME);
        meOtherMessages.put(FlyStatus.ENABLED, Messages.FLY_ON_OTHER);
        meOtherMessages.put(FlyStatus.DISABLED, Messages.FLY_OFF_OTHER);
        othersMessages.put(FlyStatus.ENABLED, Messages.FLY_OTHERS_ON);
        othersMessages.put(FlyStatus.DISABLED, Messages.FLY_OTHERS_OFF);
    }

    private void toggleFly(Player target, CommandSender sender, boolean isSelf) {
        FlyStatus status = flyManager.switchFly(target);
        if (isSelf) {
            meMessages.get(status).send(target);
        } else {
            meOtherMessages.get(status).send(sender, Placeholder.PLAYER.get(), target.getName());
            othersMessages.get(status).send(target, Placeholder.PLAYER.get(), sender.getName());
        }
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (Permissions.FLY_USE.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                Messages.FLY_USAGE.send(sender, Placeholder.ALIAS.get(), alias);
                return;
            }
            if (Permissions.FLY_ME.hasNo(player)) {
                Messages.NO_ACCESS.send(sender);
                return;
            }
            toggleFly(player, sender, true);
            return;
        }
        if (Permissions.FLY_OTHERS.hasNo(sender)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        String targetName = args[0];
        Player target = BukkitUtils.getPlayer(targetName);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(sender, Placeholder.PLAYER.get(), targetName);
            return;
        }
        toggleFly(target, sender, sender.equals(target));
    }

}