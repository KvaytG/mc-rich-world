package ru.kvaytg.coremc.config.constants;

import ru.kvaytg.coremc.config.MainConfig;
import ru.kvaytg.coremc.utils.other.DotPath;

public enum ConfigSection {

    SPAWN("spawn"),
    HIDE_STREAM("hideStream"),
    PERKS("perks"),
    LIMITS("limits"),
    CONSOLE("console"),
    CHAT("chat");

    private final DotPath configPath;

    ConfigSection(String section) {
        configPath = new DotPath(section);
    }

    public DotPath getDotPath() {
        return configPath;
    }

    public static boolean isEnabled(MainConfig mainConfig,
                                    DotPath configPath,
                                    String enablingParameter) {
        return mainConfig.getBoolean(configPath.add(enablingParameter));
    }

    public static boolean isEnabled(MainConfig mainConfig,
                                    DotPath configPath) {
        return isEnabled(mainConfig, configPath, ConfigParameter.ENABLED.getName());
    }

}