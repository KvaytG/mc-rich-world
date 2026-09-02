package ru.kvaytg.richworld.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.richworld.config.MessageConfig;
import ru.kvaytg.richworld.utils.ConfigUtils;
import java.util.function.Function;

public enum Messages {

    NO_ACCESS(config -> config.getMessage("noAccess", "&cУ вас нет прав доступа", "&cSorry, but this command is ONLY for players")),
    PLAYER_NOT_FOUND(config -> config.getMessage("playerNotFound", "&cИгрок {PLAYER} не найден", "&cPlayer {PLAYER} was not found")),
    VANISH_USAGE(config -> config.getMessage(null, null, "&cPlease use /{ALIAS} <player>")),
    VANISH_HIDE_ME(config -> config.getMessage("vanishHideMe", "&6Вы &aвключили &6себе режим невидимости")),
    VANISH_SHOW_ME(config -> config.getMessage("vanishShowMe", "&6Вы &cвыключили &6себе режим невидимости")),
    VANISH_HIDE_OTHER(config -> config.getMessage("vanishHideOther", "&6Вы &aвключили &6режим невидимости для игрока {PLAYER}", "&6Vanish &aenabled &6for player {PLAYER}")),
    VANISH_SHOW_OTHER(config -> config.getMessage("vanishShowOther", "&6Вы &cвыключили &6режим невидимости для игрока {PLAYER}", "&6Vanish &cdisabled &6for player {PLAYER}")),
    VANISH_OTHERS_HIDE(config -> config.getMessage("vanishOthersHide", "&6Игрок {PLAYER} &aвключил &6вам режим невидимости")),
    VANISH_OTHERS_SHOW(config -> config.getMessage("vanishOthersShow", "&6Игрок {PLAYER} &cвыключил &6вам режим невидимости")),
    WARP_USAGE(config -> config.getMessage("warpUsage", "&cИспользование: /{ALIAS} <название> [игрок]", "&cUsage: /{ALIAS} <warp> <player>")),
    WARP_NOT_FOUND(config -> config.getMessage("warpNotFound", "&cВарп {WARP} не найден", "&cWarp {WARP} was not found")),
    WARP_OTHER(config -> config.getMessage("warpOther", "&6Вы телепортировали игрока {PLAYER} на {WARP}", "&6Player {PLAYER} teleported to warp {WARP}")),
    KICK_USAGE(config -> config.getMessage("kickUsage", "&cИспользование: /{ALIAS} <игрок|@all> <причина>", "&cUsage: /{ALIAS} <player|@all> <reason>")),
    KICK_OTHER(config -> config.getMessage("kickOther", "&6Вы кикнули игрока {PLAYER} по причине {REASON}", "&6You kicked player {PLAYER} for reason: {REASON}")),
    KICK_ALL(config -> config.getMessage(null, null, "All players were kicked for reason: {REASON}")),
    KILL_USAGE(config -> config.getMessage("killUsage", "&cИспользование: /{ALIAS} <игрок|@all>", "&cUsage: /{ALIAS} <player|@all>")),
    KILL_OTHER(config -> config.getMessage("killOther", "&6Вы убили игрока {PLAYER}", "&6You killed player {PLAYER}")),
    KILL_ALL(config -> config.getMessage(null, null, "All players were killed")),
    FLY_ON(config -> config.getMessage("flyOn", "&#FFAA01Вы &aвключили &#FFAA01режим полёта", null)),
    FLY_OFF(config -> config.getMessage("flyOff", "&#FFAA01Вы &#FF0000выключили &#FFAA01режим полёта", null));

    private static MessageConfig config;
    private final Function<MessageConfig, Message> provider;

    Messages(Function<MessageConfig, Message> provider) {
        this.provider = provider;
    }

    public static void init(MessageConfig messageConfig) {
        config = messageConfig;
    }

    public void send(CommandSender sender, Object... replacements) {
        Message configMessage = ConfigUtils.applyWithConfigCheck(
                config,
                provider,
                "Messages class is not initialized. Call init() first."
        );
        String message = selectMessage(sender, configMessage);
        if (message == null) return;
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(replacements[i].toString(), replacements[i + 1].toString());
            }
        }
        sender.sendMessage(message);
    }

    private String selectMessage(CommandSender sender, Message configMessage) {
        if (sender instanceof Player) {
            String mainMessage = configMessage.getMain();
            return mainMessage != null ? mainMessage : configMessage.getAlternative();
        } else {
            return configMessage.getConsoleMessage();
        }
    }

}