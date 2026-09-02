package ru.kvaytg.richworld.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AbstractCommand;
import ru.kvaytg.richworld.message.Messages;
import ru.kvaytg.richworld.permission.Permissions;

public class FlyCommand extends AbstractCommand {

    public FlyCommand(RichWorld plugin) {
        super(plugin, "fly");
    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player player) || Permissions.FLY_USE.hasNo(player)) {
            Messages.NO_ACCESS.send(sender);
            return;
        }
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setFlying(false);
            Messages.FLY_OFF.send(player);
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            Messages.FLY_ON.send(player);
        }
    }

}
