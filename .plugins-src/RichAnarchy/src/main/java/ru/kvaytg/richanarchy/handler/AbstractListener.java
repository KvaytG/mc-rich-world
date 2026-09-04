package ru.kvaytg.richanarchy.handler;

import org.bukkit.event.Listener;
import ru.kvaytg.richanarchy.RichAnarchy;

public abstract class AbstractListener implements Listener {

    public AbstractListener(RichAnarchy plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}