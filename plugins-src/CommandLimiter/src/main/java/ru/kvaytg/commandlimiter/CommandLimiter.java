package ru.kvaytg.commandlimiter;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kvaytg.commandlimiter.handler.CommandHandler;

public class CommandLimiter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new CommandHandler(this), this);
    }

}