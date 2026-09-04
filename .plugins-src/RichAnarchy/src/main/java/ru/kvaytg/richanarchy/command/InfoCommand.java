package ru.kvaytg.richanarchy.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richanarchy.RichAnarchy;
import java.util.Arrays;

public class InfoCommand extends AbstractCommand {

    private final String messageNoAccess;
    private final String[] infoMessages;

    public InfoCommand(RichAnarchy plugin) {
        super(plugin, "info");
        messageNoAccess = ColorAPI.colorize(
                "&aSorry. but this command is ONLY for players"
        );
        infoMessages = Arrays.stream(new String[] {
                "",
                "&#FFFF31 Доступные команды:",
                "&#FFAA01 /info &#D0D0D0— &#FFFF31Вывести это сообщение",
                "&#FFAA01 /sell &#D0D0D0— &#FFFF31Продать алмазы/незерит",
                "&#FFAA01 /buy &#D0D0D0— &#FFFF31Купить зелья",
                ""
        }).map(ColorAPI::colorize).toArray(String[]::new);

    }

    @Override
    public void execute(CommandSender sender, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messageNoAccess);
            return;
        }
        for (String message : infoMessages) {
            player.sendMessage(message);
        }
    }

}