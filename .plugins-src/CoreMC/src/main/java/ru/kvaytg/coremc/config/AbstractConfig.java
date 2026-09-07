package ru.kvaytg.coremc.config;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.io.File;
import java.util.List;

public abstract class AbstractConfig {

    private final File configFile;
    private final YamlConfiguration config;

    public AbstractConfig(CoreMc plugin, String fileName) {
        this.configFile = new File(plugin.getDataFolder(), fileName);
        this.config = saveConfigFile(plugin);
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    private YamlConfiguration saveConfigFile(CoreMc plugin) {
        if (!configFile.exists()) {
            plugin.saveResource(configFile.getName(), false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    private String getPath(DotPath dotPath) {
        return dotPath.getPath();
    }

    public boolean getBoolean(DotPath configPath) {
        return getConfig().getBoolean(getPath(configPath));
    }

    public String getString(DotPath configPath) {
        return getConfig().getString(getPath(configPath), "");
    }

    public List<String> getList(DotPath configPath) {
        return getConfig().getStringList(getPath(configPath));
    }

    public int getInt(DotPath configPath) {
        return getConfig().getInt(getPath(configPath));
    }

    public double getDouble(DotPath configPath) {
        return getConfig().getDouble(getPath(configPath));
    }

    public float getFloat(DotPath configPath) {
        return (float) getDouble(configPath);
    }

    @SuppressWarnings("unused")
    public long getLong(DotPath configPath) {
        return getConfig().getLong(getPath(configPath));
    }

}