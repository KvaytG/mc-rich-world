package ru.kvaytg.richworld.limits;

import org.bukkit.World;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AutoConfigurableHandler;
import ru.kvaytg.richworld.config.ConfigManager;
import ru.kvaytg.richworld.config.constants.ConfigParameter;
import ru.kvaytg.richworld.config.constants.ConfigSection;
import ru.kvaytg.richworld.utils.ConfigUtils;
import ru.kvaytg.richworld.utils.other.DotPath;
import java.util.Collections;
import java.util.Set;

public abstract class LimitHandler extends AutoConfigurableHandler {

    private final Set<World> worlds;

    private static DotPath getUpdatedPath(String configSection) {
        return ConfigSection.LIMITS.getDotPath().add(configSection);
    }

    public LimitHandler(RichWorld plugin, String configSection) {
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