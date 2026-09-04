package ru.kvaytg.richhub;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richhub.handler.JoinHandler;
import ru.kvaytg.richhub.handler.NightVisionHandler;

public class RichHub extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinHandler(this), this);
        pluginManager.registerEvents(new NightVisionHandler(this), this);
    }

}