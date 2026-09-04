package ru.kvaytg.richdonate.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import java.util.List;

public abstract class AbstractCommand implements SimpleCommand {

    private final ProxyServer proxy;
    private final int minArgs;
    private final Component helpMessage;
    private final Component noAccessMessage;

    public AbstractCommand(ProxyServer proxy, String command, int minArgs, String helpMessage) {
        this.proxy = proxy;
        this.minArgs = minArgs;
        this.helpMessage = Component.text(helpMessage, TextColor.color(0xFF0000));
        this.noAccessMessage = Component.text(
                "Данная команда заблокирована",
                TextColor.color(0xFF0000)
        );
        proxy.getCommandManager().register(
                proxy.getCommandManager().metaBuilder(command).build(), this
        );
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public void sendHelpMessage(CommandSource sender) {
        sender.sendMessage(helpMessage);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();
        if (args.length == 0) return List.of("give", "take");
        if (args.length == 1) return List.of("give", "take");
        return List.of();
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        if (sender instanceof Player) {
            sender.sendMessage(noAccessMessage);
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < minArgs) {
            sendHelpMessage(sender);
            return;
        }

        try {
            onCommand(sender, args);
        } catch (RuntimeException ex) {
            sender.sendMessage(Component.text("Не удалось выполнить команду."));
        }
    }

    public abstract void onCommand(CommandSource sender, String[] args);

}
