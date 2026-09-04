package ru.kvaytg.simplehub.command;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class HubCommand extends AbstractCommand {

    private static final String SERVER_AUTH_NAME = "auth";
    private static final String SERVER_HUB_NAME = "hub";

    private final Component messageNoAccess;
    private final Component messageServerNotFound;
    private final Component messageAlreadyOnHub;

    public HubCommand(ProxyServer proxy) {
        super(proxy, "hub");
        messageNoAccess = Component.text(
                "Данная команда заблокирована",
                TextColor.color(0xFF0000)
        );
        messageServerNotFound = Component.text(
                "Сервер Хаба не найден. Свяжитесь с Администратором",
                TextColor.color(0xFF0000)
        );
        messageAlreadyOnHub = Component.text(
                "Вы уже находитесь в Хабе",
                TextColor.color(0xFFFF31)
        );
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.getCurrentServer().isEmpty()) return;
        String playerServerName = player.getCurrentServer().get().getServerInfo().getName();
        if (playerServerName.equalsIgnoreCase(SERVER_AUTH_NAME)) {
            player.sendMessage(messageNoAccess);
            return;
        }
        RegisteredServer hubServer = getProxy().getServer(SERVER_HUB_NAME).orElse(null);
        if (hubServer == null) {
            player.sendMessage(messageServerNotFound);
            return;
        }
        if (playerServerName.equalsIgnoreCase(SERVER_HUB_NAME)) {
            player.sendMessage(messageAlreadyOnHub);
            return;
        }
        player.createConnectionRequest(hubServer).connect();
    }

}