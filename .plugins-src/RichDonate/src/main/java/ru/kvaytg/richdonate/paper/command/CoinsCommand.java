package ru.kvaytg.richdonate.paper.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richdonate.paper.RichDonate;

public class CoinsCommand extends AbstractCommand {

    private final String messageNoPlayer;
    private final String messageBalanceMe;
    private final String messageBalanceOther;

    public CoinsCommand(RichDonate plugin) {
        super(plugin, "balance");
        messageNoPlayer = ColorAPI.colorize("&#FF0000Нет такого игрока");
        messageBalanceMe = ColorAPI.colorize("&#FFFF31На вашем счету &#FFAA01%d ⛂");
        messageBalanceOther = ColorAPI.colorize("&#FFFF31На счету игрока %s &#FFAA01%d ⛂");
    }

    @Override
    public void onExecute(Player player, String alias, String[] args) {
        if (args.length > 0 && player.isOp()) {
            String name = args[0];
            Player target = Bukkit.getPlayerExact(name);
            if (target == null) {
                player.sendMessage(messageNoPlayer);
                return;
            }
            String message = String.format(messageBalanceOther, name, getPlugin().getCoinsLong(target));
            player.sendMessage(message);
            return;
        }
        String message = String.format(messageBalanceMe, getPlugin().getCoinsLong(player));
        player.sendMessage(message);
    }

}