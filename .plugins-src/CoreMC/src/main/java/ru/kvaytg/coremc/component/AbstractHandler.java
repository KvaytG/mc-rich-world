package ru.kvaytg.coremc.component;

import org.bukkit.event.Listener;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.BukkitUtils;

public abstract class AbstractHandler extends AutoRegistered implements Listener {

    public AbstractHandler(CoreMc plugin, boolean enabled) {
        super(plugin, enabled, null);
    }

    @Override
    public void register(CoreMc plugin) {
        BukkitUtils.registerHandler(this, plugin);
    }

}