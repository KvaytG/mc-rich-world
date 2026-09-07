package ru.kvaytg.coremc.world;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.constants.ConfigParameter;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.config.MainConfig;
import ru.kvaytg.coremc.utils.BukkitUtils;
import ru.kvaytg.coremc.utils.ConfigUtils;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.util.Collections;
import java.util.Set;

public class WorldChanger {

    private final MainConfig mainConfig;

    public WorldChanger() {
        mainConfig = ConfigManager.INSTANCE.getMainConfig();
    }

    private Set<World> getWorldsBySetting(WorldSetting setting) {
        DotPath settingPath = ConfigSection.LIMITS.getDotPath().add(setting.getName());
        if (ConfigSection.isEnabled(mainConfig, settingPath)) {
            DotPath configPath = settingPath.add(ConfigParameter.WORLDS.getName());
            return ConfigUtils.getWorlds(mainConfig, configPath);
        }
        return Collections.emptySet();
    }

    private void disableGameRule(World world, DisabledGameRule rule) {
        world.setGameRule(rule.getRule(), rule.getValue());
    }

    private void disableMobAI(World world) {
        for (Entity entity : world.getEntities()) {
            BukkitUtils.disableAI(entity);
        }
    }

    public void disableWorldGameRules() {
        for (WorldSetting setting : WorldSetting.values()) {
            for (World world : getWorldsBySetting(setting)) {
                if (setting.getRule() != null) {
                    disableGameRule(world, setting.getRule());
                } else if (setting == WorldSetting.MOB_AI) {
                    disableMobAI(world);
                }
            }
        }
    }

}