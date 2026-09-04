package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import ru.kvaytg.richdonate.velocity.donate.coins.CoinsManager;

/*
*
* Команда изменения монеток на стороне Velocity
*
*/
public class CoinsCommand extends AbstractCommand {

    public CoinsCommand(ProxyServer proxy) {
        super(proxy,
                "coins",
                3,
                "Usage: /coins <give|take> <name> <amount>"
        );
    }

    @Override
    public void onCommand(CommandSource sender, String[] args) {
        String player =  args[1];
        String subCommand = args[0].toLowerCase();
        int amount = Integer.parseInt(args[2]);
        if (subCommand.equals("give")) {
            CoinsManager.INSTANCE.giveCoins(player, amount);
            sender.sendMessage(Component.text(
                    String.format("Коины выданы игроку %s в количестве %s шт.", player, amount)
            ));
        } else if (subCommand.equals("take")) {
            CoinsManager.INSTANCE.takeCoins(player, amount);
            sender.sendMessage(Component.text(
                    String.format("Коины отобраны у игрока %s в количестве %s шт.", player, amount)
            ));
        } else {
            sendHelpMessage(sender);
        }
    }

}