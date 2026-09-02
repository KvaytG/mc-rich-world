package ru.kvaytg.richworld.component;

import org.bukkit.event.Listener;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.utils.BukkitUtils;

public abstract class AbstractHandler extends AutoRegistered implements Listener {

    public AbstractHandler(RichWorld plugin, boolean enabled) {
        super(plugin, enabled, null);
    }

    @Override
    public void register(RichWorld plugin) {
        BukkitUtils.registerHandler(this, plugin);
    }

}