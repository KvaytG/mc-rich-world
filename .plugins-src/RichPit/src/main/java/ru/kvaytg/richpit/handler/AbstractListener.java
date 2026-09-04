package ru.kvaytg.richpit.handler;

import org.bukkit.event.Listener;
import ru.kvaytg.richpit.RichPit;

public abstract class AbstractListener implements Listener {

    public AbstractListener(RichPit plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

}