package ru.kvaytg.coremc;

import ru.kvaytg.coremc.chat.ChatHandler;
import ru.kvaytg.coremc.command.*;
import ru.kvaytg.coremc.command.mute.MuteCommand;
import ru.kvaytg.coremc.command.mute.UnmuteCommand;
import ru.kvaytg.coremc.component.AutoRegistered;
import ru.kvaytg.coremc.console.ConnectionHandler;
import ru.kvaytg.coremc.hidestream.DeathHandler;
import ru.kvaytg.coremc.hidestream.JoinHandler;
import ru.kvaytg.coremc.hidestream.KickHandler;
import ru.kvaytg.coremc.hidestream.QuitHandler;
import ru.kvaytg.coremc.joinitems.JoinItemsHandler;
import ru.kvaytg.coremc.limits.*;
import ru.kvaytg.coremc.perks.*;
import ru.kvaytg.coremc.spawn.SpawnHandler;

public class ComponentManager {

    private final CoreMc plugin;

    public ComponentManager(CoreMc plugin) {
        this.plugin = plugin;
    }

    private void registerComponent(AutoRegistered component) {
        component.register(plugin);
    }

    public void registerComponents() {
        // Регистрация команд
        registerComponent(new KickCommand(plugin));
        registerComponent(new KillCommand(plugin));
        registerComponent(new VanishCommand(plugin));
        registerComponent(new WarpCommand(plugin));
        registerComponent(new FlyCommand(plugin));
        // Регистрация консольного обработчика
        registerComponent(new ConnectionHandler(plugin));
        // Регистрация обработчика спавна
        registerComponent(new SpawnHandler(plugin));
        // Регистрация обработчика стартовых предметов
        registerComponent(new JoinItemsHandler(plugin));
        // Регистрация hide-stream обработчиков
        registerComponent(new JoinHandler(plugin));
        registerComponent(new QuitHandler(plugin));
        registerComponent(new KickHandler(plugin));
        registerComponent(new DeathHandler(plugin));
        // Регистрация плюшек
        registerComponent(new HeartsHandler(plugin));
        registerComponent(new HungerHandler(plugin));
        registerComponent(new SpeedHandler(plugin));
        registerComponent(new MobHandler(plugin));
        registerComponent(new FightsHandler(plugin));
        registerComponent(new FallRescueHandler(plugin));
        registerComponent(new VoidRescueHandler(plugin));
        registerComponent(new OverflowJoinHandler(plugin));
        registerComponent(new WelcomeHandler(plugin));
        // Регистрация ограничителей
        registerComponent(new AdvancementHandler(plugin));
        registerComponent(new DisableEntityAiHandler(plugin));
        registerComponent(new PhysicsHandler(plugin));
        registerComponent(new PortalHandler(plugin));
        registerComponent(new BlockHandler(plugin));
        registerComponent(new ExperienceHandler(plugin));
        // Регистрация обработчика чата и сопутствующих команд
        if (plugin.getServer().getPluginManager().isPluginEnabled("TAB")) {
            registerComponent(new MuteCommand(plugin));
            registerComponent(new UnmuteCommand(plugin));
            registerComponent(new ChatHandler(plugin));
        } else {
            plugin.getServer().getLogger().warning(
                    "[" + ProjectInfo.NAME + "] The TAB plugin was not found, so ChatHandler and related commands are disabled"
            );
        }
    }

}