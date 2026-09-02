package ru.kvaytg.simplehub.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import java.util.List;

public abstract class AbstractCommand implements SimpleCommand {

    private final ProxyServer proxy;
    private final Component noAccessMessage;

    public AbstractCommand(ProxyServer proxy,
                           String command) {
        this.proxy = proxy;
        this.noAccessMessage = Component.text(
                "Sorry, but this command is ONLY for players",
                TextColor.color(0xFF0000)
        );
        proxy.getCommandManager().register(proxy.getCommandManager().metaBuilder(command).build(), this);
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return List.of();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(noAccessMessage);
            return;
        }
        String[] args = invocation.arguments();
        onCommand(player, args);
    }

    public abstract void onCommand(Player player, String[] args);

}