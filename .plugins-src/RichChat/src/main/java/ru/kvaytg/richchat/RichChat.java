package ru.kvaytg.richchat;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class RichChat extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChatHandler(), this);
    }

}