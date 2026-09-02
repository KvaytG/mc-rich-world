package ru.kvaytg.commandlimiter.handler;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ru.kvaytg.commandlimiter.CommandLimiter;
import ru.kvaytg.commandlimiter.util.ColorUtils;
import ru.kvaytg.commandlimiter.util.CommandUtils;
import ru.kvaytg.commandlimiter.util.StringUtils;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandHandler implements Listener {

    private final Set<String> commandWhitelist;

    private final String notFoundMessage;
    private final String noPermsMessage;

    public CommandHandler(CommandLimiter plugin) {
        FileConfiguration config = plugin.getConfig();
        this.commandWhitelist = config
                .getStringList("whitelist")
                .stream()
                .filter(s -> !StringUtils.isNullOrBlank(s))
                .map(CommandUtils::prepareCommand)
                .collect(Collectors.toSet());
        notFoundMessage = ColorUtils.colorize(config.getString(
                "messages.notFound", "&#FF0000Данной команды не существует, либо она заблокирована")
        );
        noPermsMessage = ColorUtils.colorize(config.getString(
                "messages.noPerms", "&#FF0000У вас недостаточно прав для этой команды"
        ));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (isAdmin(player)) return;
        String message = CommandUtils.prepareCommand(event.getMessage());
        String command = message.split(" ")[0].substring(1);
        if (!commandWhitelist.contains(command)) {
            event.setCancelled(true);
            player.sendMessage(notFoundMessage);
            return;
        }
        if (!player.hasPermission("command." + command)) {
            event.setCancelled(true);
            player.sendMessage(noPermsMessage);
        }
    }

    private boolean isAdmin(Player player) {
        return player.isOp();
    }

}