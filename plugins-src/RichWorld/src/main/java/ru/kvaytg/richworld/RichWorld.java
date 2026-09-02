package ru.kvaytg.richworld;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.richworld.antiwdl.AntiWorldDownloader;
import ru.kvaytg.richworld.config.ConfigManager;
import ru.kvaytg.richworld.console.ConsoleManager;
import ru.kvaytg.richworld.utils.other.PaperLib;
import ru.kvaytg.richworld.vanish.VanishManager;
import ru.kvaytg.richworld.world.WorldChanger;

public class RichWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!PaperLib.isPaper()) {
            getLogger().severe("This plugin requires a Paper-based server");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        AntiWorldDownloader.INSTANCE.init(this);
        ConfigManager.INSTANCE.init(this);
        VanishManager.INSTANCE.init(this);
        new WorldChanger().disableWorldGameRules();
        new ComponentManager(this).registerComponents();
        ConsoleManager.INSTANCE.init();
    }

    @Override
    public void onDisable() {
        AntiWorldDownloader.INSTANCE.stop();
        VanishManager.INSTANCE.clear();
    }

}