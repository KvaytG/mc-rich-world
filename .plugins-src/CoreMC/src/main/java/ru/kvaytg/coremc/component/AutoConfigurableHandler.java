package ru.kvaytg.coremc.component;

import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.MainConfig;
import ru.kvaytg.coremc.config.constants.ConfigParameter;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.utils.other.DotPath;

import java.util.List;

public abstract class AutoConfigurableHandler extends AbstractHandler {

    private final DotPath baseConfigPath;

    public AutoConfigurableHandler(CoreMc plugin,
                                   DotPath configPath,
                                   String enablingParameter) {
        super(plugin, ConfigSection.isEnabled(
                ConfigManager.INSTANCE.getMainConfig(),
                configPath,
                enablingParameter)
        );
        this.baseConfigPath = configPath;
        onInit();
    }

    public AutoConfigurableHandler(CoreMc plugin,
                                   DotPath configPath) {
        this(plugin, configPath, ConfigParameter.ENABLED.getName());
    }

    @Override
    public final void init() {}

    public void onInit() {}

    private MainConfig getMainConfig() {
        return ConfigManager.INSTANCE.getMainConfig();
    }

    public DotPath pathTo(String... pathSegments) {
        return baseConfigPath.add(pathSegments);
    }

    public boolean getConfigBoolean(String... pathSegments) {
        return getMainConfig().getBoolean(pathTo(pathSegments));
    }

    public String getConfigString(String... pathSegments) {
        return getMainConfig().getString(pathTo(pathSegments));
    }

    @SuppressWarnings("unused")
    public List<String> getConfigList(String... pathSegments) {
        return getMainConfig().getList(pathTo(pathSegments));
    }

    public int getConfigInt(String... pathSegments) {
        return getMainConfig().getInt(pathTo(pathSegments));
    }

    @SuppressWarnings("unused")
    public double getConfigDouble(String... pathSegments) {
        return getMainConfig().getDouble(pathTo(pathSegments));
    }

    public float getConfigFloat(String... pathSegments) {
        return getMainConfig().getFloat(pathTo(pathSegments));
    }

}