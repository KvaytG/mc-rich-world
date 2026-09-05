package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.kvaytg.richdonate.velocity.donate.coins.CoinsManager;
import java.util.Optional;
import java.util.UUID;

public class CoinsCommand extends AbstractCommand {

    public CoinsCommand(ProxyServer proxy) {
        super(proxy, "coins", 3, "Usage: /coins <give|take> <name> <amount>");
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        String subCommand = args[0].toLowerCase(java.util.Locale.ROOT);
        if (!subCommand.equals("give") && !subCommand.equals("take")) {
            sendHelpMessage(sender);
            return;
        }

        long amount;
        try {
            amount = Long.parseLong(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(Component.text("Количество должно быть целым числом."));
            return;
        }

        if (amount <= 0) {
            sender.sendMessage(Component.text("Количество должно быть больше нуля."));
            return;
        }

        Optional<UUID> target = getPlayerResolver().resolve(args[1]);
        if (target.isEmpty()) {
            sender.sendMessage(Component.text("Игрок не найден или не удалось определить его UUID."));
            return;
        }

        boolean changed;
        if (subCommand.equals("give")) {
            changed = CoinsManager.INSTANCE.giveCoins(
                    target.get(), amount,
                    UUID.randomUUID().toString()
            );
        } else {
            changed = CoinsManager.INSTANCE.takeCoins(
                    target.get(), amount,
                    UUID.randomUUID().toString()
            );
        }

        sender.sendMessage(Component.text(
                changed
                        ? String.format(
                            subCommand.equals("give")
                                    ? "Коины выданы игроку %s в количестве %d шт."
                                    : "Коины отобраны у игрока %s в количестве %d шт.",
                            args[1], amount
                        )
                        : "Операция не выполнена."
        ));
    }

}