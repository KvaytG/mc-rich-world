package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.kvaytg.richdonate.velocity.donate.status.StatusManager;
import java.util.Optional;
import java.util.UUID;

public class StatusCommand extends AbstractCommand {

    public StatusCommand(ProxyServer proxy) {
        super(proxy, "status", 3, "Usage: /status <give|take> <name> <status|reason>");
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        String subCommand = args[0].toLowerCase(java.util.Locale.ROOT);
        if (!subCommand.equals("give") && !subCommand.equals("take")) {
            sendHelpMessage(sender);
            return;
        }

        Optional<Player> target = getProxy().getPlayer(args[1]);
        if (target.isEmpty()) {
            sender.sendMessage(Component.text("Игрок должен быть онлайн."));
            return;
        }

        UUID playerId = target.get().getUniqueId();
        boolean changed;

        if (subCommand.equals("give")) {
            changed = StatusManager.INSTANCE.giveStatus(
                    playerId,
                    args[2],
                    UUID.randomUUID().toString()
            );
            sender.sendMessage(Component.text(
                    changed
                            ? String.format("Статус %s выдан игроку %s", args[2], target.get().getUsername())
                            : "Операция не выполнена."
            ));
        } else {
            changed = StatusManager.INSTANCE.takeStatus(
                    playerId,
                    UUID.randomUUID().toString()
            );
            sender.sendMessage(Component.text(
                    changed
                            ? String.format("Статус отозван у игрока %s. Причина: %s",
                                target.get().getUsername(), args[2])
                            : "Операция не выполнена."
            ));
        }
    }

}