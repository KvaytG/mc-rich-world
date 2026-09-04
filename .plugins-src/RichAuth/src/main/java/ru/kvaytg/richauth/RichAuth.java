package ru.kvaytg.richauth;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.richauth.listener.PlayerChatListener;
import ru.kvaytg.richauth.listener.PlayerJoinListener;
import ru.kvaytg.richauth.listener.PlayerMoveListener;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class RichAuth extends JavaPlugin {

    public static String USAGE;
    public static String CODE_NOT_FOUND;
    public static String CODE_EXPIRED;
    public static String CODE_INVALID;
    public static String LINK_SUCCESS;
    public static String REMINDER;
    public static String REMINDER_PREFIX;
    public static String REMINDER_SUFFIX;
    public static String BLOCKED;
    public static String TIMEOUT;

    private String botToken;
    private String botUsername;
    private final Map<String, Long> linkedAccounts = new ConcurrentHashMap<>();
    private final Map<String, PendingLink> pendingLinks = new ConcurrentHashMap<>();
    private final Map<String, BukkitTask> reminderTasks = new ConcurrentHashMap<>();
    private final Set<String> confirmedPlayers = ConcurrentHashMap.newKeySet();
    public TelegramBot bot;

    @Override
    public void onEnable() {
        USAGE = ColorAPI.colorize("&#FFFF31Использование: /link <ваш код из Telegram>");
        CODE_NOT_FOUND = ColorAPI.colorize("&#FFFF31Код не найден. Запросите новый в боте.");
        CODE_EXPIRED = ColorAPI.colorize("&#FF0000Код устарел! Запросите новый.");
        CODE_INVALID = ColorAPI.colorize("&#FF0000Неверный код подтверждения!");
        LINK_SUCCESS = ColorAPI.colorize("&#00FF00Аккаунт успешно привязан!");
        REMINDER = ColorAPI.colorize("&#FFFF31Подтвердите вход в Telegram!");
        REMINDER_PREFIX = ColorAPI.colorize("&#FFFF31Для регистрации привяжите Telegram: &#FFAA01");
        REMINDER_SUFFIX = ColorAPI.colorize("&#FFFF31В боте введите ваш ник: &#FFAA01");
        BLOCKED = ColorAPI.colorize("&#FF0000Вход заблокирован владельцем аккаунта");
        TIMEOUT = ColorAPI.colorize("&#FF0000Время подтверждения истекло!");

        saveDefaultConfig();
        loadConfig();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Objects.requireNonNull(getCommand("link")).setExecutor(new LinkCommand(this));

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            DefaultBotOptions botOptions = createBotOptions();
            bot = new TelegramBot(botOptions);

            botsApi.registerBot(bot);
            getLogger().info("Telegram бот запущен!");
        } catch (Exception e) {
            getLogger().severe("Ошибка запуска бота: " + e.getMessage());
        }
    }

    private DefaultBotOptions createBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();
        FileConfiguration config = getConfig();

        if (config.getBoolean("proxy.enabled", false)) {
            String proxyType = config.getString("proxy.type", "HTTP").toUpperCase();
            String host = config.getString("proxy.host", "127.0.0.1");
            int port = config.getInt("proxy.port", 8080);

            if ("SOCKS5".equals(proxyType) || "SOCKS".equals(proxyType)) {
                options.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            } else if ("SOCKS4".equals(proxyType)) {
                options.setProxyType(DefaultBotOptions.ProxyType.SOCKS4);
            } else {
                options.setProxyType(DefaultBotOptions.ProxyType.HTTP);
            }

            options.setProxyHost(host);
            options.setProxyPort(port);

            String username = config.getString("proxy.username", "");
            String password = config.getString("proxy.password", "");

            if (!username.isEmpty() && !password.isEmpty()) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                });
            }
            getLogger().info("Используется прокси " + proxyType + ": " + host + ":" + port);
        }

        return options;
    }

    @Override
    public void onDisable() {
        saveLinkedAccounts();
        reminderTasks.values().forEach(BukkitTask::cancel);
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        botToken = config.getString("bot-token", "");
        botUsername = config.getString("bot-username", "");
        if (config.contains("linked-accounts")) {
            for (String key : Objects.requireNonNull(config.getConfigurationSection("linked-accounts")).getKeys(false)) {
                linkedAccounts.put(key.toLowerCase(), config.getLong("linked-accounts." + key));
            }
        }
    }

    void saveLinkedAccounts() {
        FileConfiguration config = getConfig();
        config.set("linked-accounts", null);
        for (Map.Entry<String, Long> entry : linkedAccounts.entrySet()) {
            config.set("linked-accounts." + entry.getKey(), entry.getValue());
        }
        saveConfig();
    }

    public String normalizeName(String name) {
        return Normalizer.normalize(name, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ENGLISH);
    }

    public Map<String, Long> getLinkedAccounts() {
        return linkedAccounts;
    }

    public Map<String, PendingLink> getPendingLinks() {
        return pendingLinks;
    }

    public Set<String> getConfirmedPlayers() {
        return confirmedPlayers;
    }

    public Map<String, BukkitTask> getReminderTasks() {
        return reminderTasks;
    }

    @SuppressWarnings("UnstableApiUsage")
    public void sendToHub(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("hub");
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public class TelegramBot extends TelegramLongPollingBot {

        public TelegramBot(DefaultBotOptions options) {
            super(options);
        }

        @Override
        public void onUpdateReceived(Update update) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessage(update);
            } else if (update.hasCallbackQuery()) {
                handleCallback(update);
            }
        }

        private void handleMessage(Update update) {
            String message = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();
            if (message.startsWith("/start")) {
                sendWelcomeMessage(chatId);
            } else {
                handleMinecraftName(chatId, message);
            }
        }

        private void sendWelcomeMessage(Long chatId) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText("Для привязки аккаунта введите ваш ник Minecraft:");
            executeSilently(msg);
        }

        private void handleMinecraftName(Long chatId, String name) {
            if (!name.matches("^[a-zA-Z0-9_]{3,16}$")) {
                sendMessage(chatId, "Некорректный ник Minecraft. Используйте 3-16 символов (a-z, 0-9, _).");
                return;
            }
            Player player = Bukkit.getPlayerExact(name);
            if (player == null) {
                sendMessage(chatId, "Игрок не найден онлайн. Введите ник снова:");
                return;
            }
            String normalized = normalizeName(name);
            if (linkedAccounts.containsValue(chatId)) {
                sendMessage(chatId, "Этот Telegram уже привязан к другому аккаунту!");
                return;
            }
            if (linkedAccounts.containsKey(normalized)) {
                sendMessage(chatId, "Этот игрок уже привязан к другому аккаунту!");
                return;
            }
            String code = String.format("%06d", ThreadLocalRandom.current().nextInt(999999));
            pendingLinks.put(normalized, new PendingLink(chatId, code, System.currentTimeMillis()));
            sendMessage(chatId, "Введите в игре: /link " + code);
        }

        @SuppressWarnings("deprecation")
        private void handleCallback(Update update) {
            String data = update.getCallbackQuery().getData();
            String[] parts = data.split(":");
            if (parts.length < 2) return;
            String action = parts[0];
            String playerName = parts[1];
            Long senderChatId = update.getCallbackQuery().getFrom().getId();
            String normalized = normalizeName(playerName);
            Long ownerChatId = linkedAccounts.get(normalized);
            if (ownerChatId == null || !ownerChatId.equals(senderChatId)) {
                sendMessage(senderChatId, "Вы не владелец аккаунта!");
                return;
            }
            Player player = Bukkit.getPlayerExact(playerName);
            if ("allow".equals(action)) {
                confirmedPlayers.add(normalized);
                Bukkit.getScheduler().runTask(RichAuth.this, () -> {
                    if (player != null && player.isOnline()) {
                        sendToHub(player);
                    }
                });
                sendMessage(senderChatId, "Вход разрешён");
            } else if ("deny".equals(action)) {
                confirmedPlayers.remove(normalized);
                Bukkit.getScheduler().runTask(RichAuth.this, () -> {
                    if (player != null && player.isOnline()) {
                        player.kickPlayer(BLOCKED);
                    }
                });
                sendMessage(senderChatId, "Вход заблокирован");
            }
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            deleteMessage(senderChatId, messageId);
        }

        void deleteMessage(Long chatId, int messageId) {
            DeleteMessage deleteMsg = new DeleteMessage();
            deleteMsg.setChatId(chatId.toString());
            deleteMsg.setMessageId(messageId);
            executeSilently(deleteMsg);
        }

        void sendMessage(Long chatId, String text) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText(text);
            executeSilently(msg);
        }

        public void sendLoginRequest(String playerName, Long chatId) {
            SendMessage msg = new SendMessage();
            msg.setChatId(chatId.toString());
            msg.setText("Попытка входа на сервер: " + playerName);

            InlineKeyboardButton allowButton = new InlineKeyboardButton("Подтвердить");
            allowButton.setCallbackData("allow:" + playerName);

            InlineKeyboardButton denyButton = new InlineKeyboardButton("Заблокировать");
            denyButton.setCallbackData("deny:" + playerName);

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(allowButton);
            row.add(denyButton);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(List.of(row));
            msg.setReplyMarkup(markup);

            executeSilently(msg);
        }

        private void executeSilently(Object method) {
            try {
                if (method instanceof SendMessage) {
                    execute((SendMessage) method);
                } else if (method instanceof DeleteMessage) {
                    execute((DeleteMessage) method);
                }
            } catch (TelegramApiException e) {
                getLogger().warning("Ошибка Telegram (" + method.getClass().getSimpleName() +
                        "): " + e.getMessage());
            }
        }

        @Override
        public String getBotUsername() {
            return botUsername;
        }

        @Override
        public String getBotToken() {
            return botToken;
        }
    }
}