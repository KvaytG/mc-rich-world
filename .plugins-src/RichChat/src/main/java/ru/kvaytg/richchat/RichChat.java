package ru.kvaytg.richchat;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richdonate.paper.RichDonate;

@SuppressWarnings("unused")
public class RichChat extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(
                new ChatHandler(this, RichDonate.getInstance()),
                this
        );
    }

}