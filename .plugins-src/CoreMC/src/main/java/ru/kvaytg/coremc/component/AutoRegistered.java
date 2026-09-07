package ru.kvaytg.coremc.component;

import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.StringUtils;

public abstract class AutoRegistered extends Enabling {

    private final CoreMc plugin;
    private final String name;

    public CoreMc getPlugin() {
        return plugin;
    }

    public String getName() {
        return StringUtils.isNullOrBlank(name) ? null : name;
    }

    public AutoRegistered(CoreMc plugin, boolean enabled, String name) {
        super(enabled);
        this.plugin = plugin;
        this.name = name;
        if (isEnabled()) {
            init();
        }
    }

    public void init() {}

    public abstract void register(CoreMc plugin);

}