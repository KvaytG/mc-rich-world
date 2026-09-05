package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Получает UUID игрока даже если он сейчас оффлайн.
 * Порядок поиска:
 * 1. Игрок онлайн в Velocity.
 * 2. UUID есть в кэше.
 * 3. Offline-mode: вычисляется OfflinePlayer UUID.
 * 4. Online-mode: запрос к Mojang API.
 */
public final class PlayerResolver {

    private static final Pattern ID_PATTERN = Pattern.compile(
            "\"id\"\\s*:\\s*\"([0-9a-fA-F]{32})\""
    );

    private final ProxyServer proxy;

    private final Map<String, UUID> cache = new ConcurrentHashMap<>();

    private final HttpClient httpClient;

    public PlayerResolver(ProxyServer proxy) {
        this.proxy = proxy;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    public Optional<UUID> resolve(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }

        String requestedName = name.trim();
        String cacheKey = requestedName.toLowerCase(Locale.ROOT);

        /*
         * 1. Сначала проверяем игрока онлайн.
         */
        Optional<Player> online = proxy.getPlayer(requestedName);

        if (online.isPresent()) {
            Player player = online.get();
            UUID uuid = player.getUniqueId();

            cache.put(
                    cacheKey,
                    uuid
            );

            cache.put(
                    player.getUsername().toLowerCase(Locale.ROOT),
                    uuid
            );

            return Optional.of(uuid);
        }

        /*
         * 2. Проверяем локальный кэш.
         */
        UUID cached = cache.get(cacheKey);

        if (cached != null) {
            return Optional.of(cached);
        }

        /*
         * 3. Если Velocity работает в offline-mode,
         *    UUID можно получить локально.
         */
        if (!proxy.getConfiguration().isOnlineMode()) {
            UUID uuid = offlineUuid(requestedName);

            cache.put(cacheKey, uuid);

            return Optional.of(uuid);
        }

        /*
         * 4. Online-mode — пытаемся узнать UUID через Mojang.
         */
        Optional<UUID> mojangUuid = resolveFromMojang(requestedName);

        mojangUuid.ifPresent(uuid ->
                cache.put(cacheKey, uuid)
        );

        return mojangUuid;
    }

    private Optional<UUID> resolveFromMojang(String name) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(
                            URI.create(
                                    "https://api.mojang.com/users/profiles/minecraft/"
                                            + name
                            )
                    )
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(
                            StandardCharsets.UTF_8
                    )
            );

            if (response.statusCode() != 200) {
                return Optional.empty();
            }

            Matcher matcher = ID_PATTERN.matcher(
                    response.body()
            );

            if (!matcher.find()) {
                return Optional.empty();
            }

            return Optional.of(
                    UUID.fromString(
                            formatUuid(matcher.group(1))
                    )
            );

        } catch (IOException | RuntimeException ex) {
            return Optional.empty();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    /**
     * Стандартный UUID для OfflinePlayer.
     */
    private static UUID offlineUuid(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Mojang API отдаёт UUID без дефисов.
     */
    private static String formatUuid(String raw) {
        return raw.substring(0, 8)
                + "-"
                + raw.substring(8, 12)
                + "-"
                + raw.substring(12, 16)
                + "-"
                + raw.substring(16, 20)
                + "-"
                + raw.substring(20);
    }

}