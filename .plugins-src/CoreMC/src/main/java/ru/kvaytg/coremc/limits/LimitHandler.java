package ru.kvaytg.coremc.limits;

import org.bukkit.World;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.component.AutoConfigurableHandler;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.constants.ConfigParameter;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.utils.ConfigUtils;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.util.Collections;
import java.util.Set;

public abstract class LimitHandler extends AutoConfigurableHandler {

    private final Set<World> worlds;

    private static DotPath getUpdatedPath(String configSection) {
        return ConfigSection.LIMITS.getDotPath().add(configSection);
    }

    public LimitHandler(CoreMc plugin, String configSection) {
        super(plugin, getUpdatedPath(configSection));
        worlds = ConfigUtils.getWorlds(
                ConfigManager.INSTANCE.getMainConfig(),
                getUpdatedPath(configSection).add(ConfigParameter.WORLDS.getName())
        );
    }

    public Set<World> getWorlds() {
        return Collections.unmodifiableSet(worlds);
    }

}