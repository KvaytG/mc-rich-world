package ru.kvaytg.simplehub;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import ru.kvaytg.simplehub.command.HubCommand;
import javax.inject.Inject;

@SuppressWarnings("unused")
@Plugin(
        id = "simplehub",
        name = "SimpleHub",
        authors = {"KvaytG"},
        version = "1.0.0",
        description = "Команда /hub проекта RichWorld"
)
public class SimpleHub {

    private final ProxyServer proxy;

    @Inject
    public SimpleHub(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        new HubCommand(proxy);
    }

}