package ru.kvaytg.richmobs;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richmobs.handler.BloodHandler;
import ru.kvaytg.richmobs.handler.DeathHandler;

@SuppressWarnings("unused")
public class RichMobs extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BloodHandler(), this);
        pluginManager.registerEvents(new DeathHandler(), this);
    }

}