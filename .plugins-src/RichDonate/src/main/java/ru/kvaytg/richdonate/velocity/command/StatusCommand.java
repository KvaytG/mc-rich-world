package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.kvaytg.richdonate.velocity.donate.status.StatusManager;

/*
*
* Команда изменения VIP-статуса на стороне Velocity
*
*/
public class StatusCommand extends AbstractCommand {

    public StatusCommand(ProxyServer proxy) {
        super(proxy,
                "status",
                3,
                "Usage: /status <give|take> <name> <status|reason>"
        );
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        String player =  args[1];
        String subCommand = args[0].toLowerCase();
        if (subCommand.equals("give")) {
            String status = args[2].toLowerCase();
            StatusManager.INSTANCE.giveStatus(player, status);
            sender.sendMessage(Component.text(
                    String.format("Статус %s выдан игроку %s", status, player)
            ));
        } else if (subCommand.equals("take")) {
            String reason = args[2];
            StatusManager.INSTANCE.takeStatus(player);
            sender.sendMessage(Component.text(
                    String.format("Статус отозван у игрока %s по причине: %s", player, reason)
            ));
        } else {
            sendHelpMessage(sender);
        }
    }

}