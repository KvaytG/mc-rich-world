package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.kvaytg.richdonate.velocity.PlayerResolver;
import ru.kvaytg.richdonate.velocity.donate.status.StatusManager;
import java.util.Optional;
import java.util.UUID;

public class StatusCommand extends AbstractCommand {

    private final PlayerResolver playerResolver;

    public StatusCommand(ProxyServer proxy, PlayerResolver playerResolver) {
        super(proxy, "status", 3, "Usage: /status <give|take> <name> <status|reason>");
        this.playerResolver = playerResolver;
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        String subCommand = args[0].toLowerCase(java.util.Locale.ROOT);
        if (!subCommand.equals("give") && !subCommand.equals("take")) {
            sendHelpMessage(sender);
            return;
        }
        Optional<UUID> target = playerResolver.resolve(args[1]);
        if (target.isEmpty()) {
            sender.sendMessage(Component.text("Игрок не найден или не удалось определить его UUID."));
            return;
        }
        UUID playerId = target.get();
        boolean changed;
        if (subCommand.equals("give")) {
            changed = StatusManager.INSTANCE.giveStatus(
                    playerId,
                    args[2],
                    UUID.randomUUID().toString()
            );
            sender.sendMessage(Component.text(
                    changed
                            ? String.format("Статус %s выдан игроку %s", args[2], args[1])
                            : "Операция не выполнена."
            ));
        } else {
            changed = StatusManager.INSTANCE.takeStatus(
                    playerId,
                    UUID.randomUUID().toString()
            );
            sender.sendMessage(Component.text(
                    changed
                            ? String.format("Статус отозван у игрока %s. Причина: %s", args[1], args[2])
                            : "Операция не выполнена."
            ));
        }
    }

}