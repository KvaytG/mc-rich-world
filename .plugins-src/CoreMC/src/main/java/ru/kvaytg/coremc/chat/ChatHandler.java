package ru.kvaytg.coremc.chat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.api.nametag.NameTagManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AutoConfigurableHandler;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.MuteConfig;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.utils.StringUtils;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatHandler extends AutoConfigurableHandler {

    private static final Pattern LINK_IP_PATTERN = Pattern.compile(
            "(?i)\\b(?:https?://)?(?:(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}|(?:\\d{1,3}\\.){3}\\d{1,3})(?::\\d{1,5})?(?:/\\S*)?\\b"
    );

    private static final String MESSAGE_MUTED = ColorAPI.colorize("&#FF0000Вы замучены! Осталось: %d сек.");
    private static final String MESSAGE_LINK_IP_DENIED = ColorAPI.colorize("&#FF0000Ссылки и IP-адреса запрещены в чате!");
    private static final String MESSAGE_REPEAT_MUTE = ColorAPI.colorize("&#FF0000Вы получили мут на 5 минут за повторение сообщений.");
    private static final String MESSAGE_REPEAT_WARN = ColorAPI.colorize("&#FF0000Сообщение слишком похоже на предыдущее! Не повторяйтесь, иначе получите мут.");

    private final MuteConfig config;
    private final TabAPI tabAPI;
    private final NameTagManager nameTagManager;

    private final Map<UUID, String> lastMessages = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> warnedPlayers = new ConcurrentHashMap<>();

    public ChatHandler(CoreMc plugin) {
        super(plugin, ConfigSection.CHAT.getDotPath());
        this.config = ConfigManager.INSTANCE.getMuteConfig();
        this.tabAPI = TabAPI.getInstance();
        this.nameTagManager = tabAPI.getNameTagManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (config.isMuted(uuid)) {
            long remaining = config.getRemainingTime(uuid) / 1000;
            player.sendMessage(String.format(MESSAGE_MUTED, remaining));
            event.setCancelled(true);
            return;
        }
        String currentMessage = PlainComponentSerializer.plain().serialize(event.message());
        Matcher matcher = LINK_IP_PATTERN.matcher(currentMessage);
        if (matcher.find()) {
            currentMessage = matcher.replaceAll("<***>");
            event.message(Component.text(currentMessage));
            player.sendMessage(MESSAGE_LINK_IP_DENIED);
        }
        if (lastMessages.containsKey(uuid)) {
            double similarity = getSimilarity(lastMessages.get(uuid), currentMessage);
            if (similarity >= 0.9) {
                if (warnedPlayers.getOrDefault(uuid, false)) {
                    event.setCancelled(true);
                    config.setMute(uuid, 5 * 60 * 1000);
                    player.sendMessage(MESSAGE_REPEAT_MUTE);
                    warnedPlayers.remove(uuid);
                    return;
                } else {
                    event.setCancelled(true);
                    player.sendMessage(MESSAGE_REPEAT_WARN);
                    warnedPlayers.put(uuid, true);
                    return;
                }
            }
        }
        lastMessages.put(uuid, currentMessage);
        warnedPlayers.put(uuid, false);
        TabPlayer tabPlayer = tabAPI.getPlayer(uuid);
        String prefix = "";
        String suffix = "";
        if (tabPlayer != null) {
            prefix = nameTagManager.getOriginalPrefix(tabPlayer);
            suffix = nameTagManager.getOriginalSuffix(tabPlayer);
            prefix = !StringUtils.isNullOrBlank(prefix) ? ColorAPI.colorize(prefix) : "";
            suffix = !StringUtils.isNullOrBlank(suffix) ? ColorAPI.colorize(suffix) : "";
        }
        String finalPrefix = prefix;
        String finalSuffix = suffix;
        String finalMessage = currentMessage;
        event.renderer((source, sourceDisplayName, message, viewer) ->
                LegacyComponentSerializer.legacySection().deserialize(
                        finalPrefix + player.getName() + finalSuffix + " §7-> §r" + finalMessage
                )
        );
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        lastMessages.remove(uuid);
        warnedPlayers.remove(uuid);
    }

    private double getSimilarity(String s1, String s2) {
        if (s1.equalsIgnoreCase(s2)) return 1.0;
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == 0 || len2 == 0) return 0.0;
        int[] prev = new int[len2 + 1];
        for (int j = 0; j <= len2; j++) prev[j] = j;
        for (int i = 1; i <= len1; i++) {
            int[] curr = new int[len2 + 1];
            curr[0] = i;
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, curr[0] + 1), prev[j - 1] + cost);
                curr[j] = Math.min(curr[j], prev[j] + 1);
            }
            prev = curr;
        }
        int distance = prev[len2];
        return 1.0 - ((double) distance / Math.max(len1, len2));
    }

}