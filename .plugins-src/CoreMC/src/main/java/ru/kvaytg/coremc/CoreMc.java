package ru.kvaytg.coremc;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.coremc.antiwdl.AntiWorldDownloader;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.console.ConsoleManager;
import ru.kvaytg.coremc.utils.other.PaperLib;
import ru.kvaytg.coremc.vanish.VanishManager;
import ru.kvaytg.coremc.world.WorldChanger;

public class CoreMc extends JavaPlugin {

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
        ConfigManager.INSTANCE.saveAll();
    }

}