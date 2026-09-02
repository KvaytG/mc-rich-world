package ru.kvaytg.richworld.config;

import org.bukkit.configuration.file.YamlConfiguration;
import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.utils.other.DotPath;
import java.io.File;
import java.util.List;

public abstract class AbstractConfig {

    private final YamlConfiguration config;

    public YamlConfiguration getConfig() {
        return config;
    }

    public AbstractConfig(RichWorld plugin, String fileName) {
        this.config = saveConfigFile(plugin, fileName);
    }

    private YamlConfiguration saveConfigFile(RichWorld plugin, String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
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

}