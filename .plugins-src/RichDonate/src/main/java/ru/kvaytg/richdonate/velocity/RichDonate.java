package ru.kvaytg.richdonate.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import ru.kvaytg.richdonate.velocity.command.CoinsCommand;
import ru.kvaytg.richdonate.velocity.command.StatusCommand;
import ru.kvaytg.richdonate.velocity.donate.coins.CoinsManager;
import ru.kvaytg.richdonate.velocity.donate.status.StatusManager;

import javax.inject.Inject;

@Plugin(
        id = "richdonate",
        name = "RichDonate",
        authors = {"KvaytG"},
        version = "2.0.0",
        description = "Донат-система проекта RichWorld"
)
public class RichDonate {

    private final ProxyServer proxy;

    @Inject
    public RichDonate(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CoinsManager.INSTANCE.init();
        StatusManager.INSTANCE.init();

        proxy.getChannelRegistrar().register(Identifier.get());
        proxy.getEventManager().register(this, new ChannelHandler(proxy));

        new CoinsCommand(proxy);
        new StatusCommand(proxy);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        CoinsManager.INSTANCE.flush();
        StatusManager.INSTANCE.flush();
    }

}