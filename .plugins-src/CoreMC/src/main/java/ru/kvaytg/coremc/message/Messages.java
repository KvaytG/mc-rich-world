package ru.kvaytg.coremc.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.coremc.config.MessageConfig;
import ru.kvaytg.coremc.utils.ConfigUtils;

import java.util.function.Function;

public enum Messages {

    NO_ACCESS(config -> config.getMessage("noAccess", "&cУ вас нет прав доступа", "&cSorry, but this command is ONLY for players")),
    NUMBER_MUST_BE_MORE_THAN_ZERO(config -> config.getMessage("numberMustBeMoreThanZero", "&cЧисло должно быть больше 0", "&cThe number must be greater than 0")),
    NOT_NUMBER(config -> config.getMessage("notNumber", "&c'{VALUE}' не является числом", "&c'{VALUE}' is not a number")),
    PLAYER_NOT_FOUND(config -> config.getMessage("playerNotFound", "&cИгрок {PLAYER} не найден", "&cPlayer {PLAYER} was not found")),
    VANISH_USAGE(config -> config.getMessage(null, null, "&cPlease use /{ALIAS} <player>")),
    VANISH_HIDE_ME(config -> config.getMessage("vanishHideMe", "&eВы &aвключили &eсебе режим невидимости")),
    VANISH_SHOW_ME(config -> config.getMessage("vanishShowMe", "&eВы &cвыключили &eсебе режим невидимости")),
    VANISH_HIDE_OTHER(config -> config.getMessage("vanishHideOther", "&eВы &aвключили &eрежим невидимости для игрока {PLAYER}", "&eVanish &aenabled &efor player {PLAYER}")),
    VANISH_SHOW_OTHER(config -> config.getMessage("vanishShowOther", "&eВы &cвыключили &eрежим невидимости для игрока {PLAYER}", "&eVanish &cdisabled &efor player {PLAYER}")),
    VANISH_OTHERS_HIDE(config -> config.getMessage("vanishOthersHide", "&eИгрок {PLAYER} &aвключил &eвам режим невидимости")),
    VANISH_OTHERS_SHOW(config -> config.getMessage("vanishOthersShow", "&eИгрок {PLAYER} &cвыключил &eвам режим невидимости")),
    WARP_USAGE(config -> config.getMessage("warpUsage", "&cИспользование: /{ALIAS} <название> [игрок]", "&cUsage: /{ALIAS} <warp> <player>")),
    WARP_NOT_FOUND(config -> config.getMessage("warpNotFound", "&cВарп {WARP} не найден", "&cWarp {WARP} was not found")),
    WARP_OTHER(config -> config.getMessage("warpOther", "&eВы телепортировали игрока {PLAYER} на {WARP}", "&ePlayer {PLAYER} teleported to warp {WARP}")),
    KICK_USAGE(config -> config.getMessage("kickUsage", "&cИспользование: /{ALIAS} <игрок|@all> <причина>", "&cUsage: /{ALIAS} <player|@all> <reason>")),
    KICK_OTHER(config -> config.getMessage("kickOther", "&eВы кикнули игрока {PLAYER} по причине {REASON}", "&eYou kicked player {PLAYER} for reason: {REASON}")),
    KICK_ALL(config -> config.getMessage(null, null, "All players were kicked for reason: {REASON}")),
    KILL_USAGE(config -> config.getMessage("killUsage", "&cИспользование: /{ALIAS} <игрок|@all>", "&cUsage: /{ALIAS} <player|@all>")),
    KILL_OTHER(config -> config.getMessage("killOther", "&eВы убили игрока {PLAYER}", "&eYou killed player {PLAYER}")),
    KILL_ALL(config -> config.getMessage(null, null, "All players were killed")),
    FLY_USAGE(config -> config.getMessage(null, null, "&cPlease use /{ALIAS} <player>")),
    FLY_ON_ME(config -> config.getMessage("flyOnMe", "&eВы &aвключили &eсебе режим полёта")),
    FLY_OFF_ME(config -> config.getMessage("flyOffMe", "&eВы &cвыключили &eсебе режим полёта")),
    FLY_ON_OTHER(config -> config.getMessage("flyOnOther", "&eВы &aвключили &eрежим полёта для игрока {PLAYER}", "&eFly &aenabled &efor player {PLAYER}")),
    FLY_OFF_OTHER(config -> config.getMessage("flyOffOther", "&eВы &cвыключили &eрежим полёта для игрока {PLAYER}", "&eFly &cdisabled &efor player {PLAYER}")),
    FLY_OTHERS_ON(config -> config.getMessage("flyOthersOn", "&eИгрок {PLAYER} &aвключил &eвам режим полёта")),
    FLY_OTHERS_OFF(config -> config.getMessage("flyOthersOff", "&eИгрок {PLAYER} &cвыключил &eвам режим полёта")),
    MUTE_USAGE(config -> config.getMessage("muteUsage", "&cИспользование: /{ALIAS} <игрок> <минуты>", "&cUsage: /{ALIAS} <player> <minutes>")),
    MUTE_OTHER(config -> config.getMessage("muteOther", "&eВы замутили игрока {PLAYER} на {MINUTES} мин.", "&eYou muted player {PLAYER} for {MINUTES} min.")),
    MUTE_OTHERS(config -> config.getMessage("muteOthers", "§cВы были замучены пользователем {PLAYER} на {MINUTES} мин.")),
    UNMUTE_USAGE(config -> config.getMessage("unmuteUsage", "&cИспользование: /{ALIAS} <игрок>", "&cUsage: /{ALIAS} <player>")),
    UNMUTE_OTHER(config -> config.getMessage("unmuteOther", "&eВы размутили игрока {PLAYER}", "&eYou unmuted player {PLAYER}"));

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