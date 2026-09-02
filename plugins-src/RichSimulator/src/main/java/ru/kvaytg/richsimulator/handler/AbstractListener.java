package ru.kvaytg.richsimulator.handler;

import org.bukkit.event.Listener;
import ru.kvaytg.richsimulator.RichSimulator;

public abstract class AbstractListener implements Listener {

    private final RichSimulator plugin;

    public AbstractListener(RichSimulator plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public RichSimulator getPlugin() {
        return plugin;
    }

}