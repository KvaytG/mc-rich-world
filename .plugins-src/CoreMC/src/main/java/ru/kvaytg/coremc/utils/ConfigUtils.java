package ru.kvaytg.coremc.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import ru.kvaytg.coremc.config.AbstractConfig;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ConfigUtils {

    private ConfigUtils() {}

    public static <T, R> R applyWithConfigCheck(T config, Function<T, R> provider, String errorMessage) {
        if (config == null) {
            throw new IllegalStateException(errorMessage);
        }
        return provider.apply(config);
    }

    public static Set<World> getWorlds(AbstractConfig config, DotPath configPath) {
        List<String> worldNames = config.getConfig().getStringList(configPath.getPath());
        if (worldNames.isEmpty()) return Collections.emptySet();
        Set<World> worlds = new HashSet<>();
        for (String worldName : worldNames) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                worlds.add(world);
            }
        }
        return worlds;
    }

}