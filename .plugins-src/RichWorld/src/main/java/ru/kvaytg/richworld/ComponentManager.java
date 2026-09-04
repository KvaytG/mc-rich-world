package ru.kvaytg.richworld;

import ru.kvaytg.richworld.brand.BrandHandler;
import ru.kvaytg.richworld.command.*;
import ru.kvaytg.richworld.component.AutoRegistered;
import ru.kvaytg.richworld.joinitems.JoinItemsHandler;
import ru.kvaytg.richworld.console.ConnectionHandler;
import ru.kvaytg.richworld.hidestream.DeathHandler;
import ru.kvaytg.richworld.hidestream.JoinHandler;
import ru.kvaytg.richworld.hidestream.KickHandler;
import ru.kvaytg.richworld.hidestream.QuitHandler;
import ru.kvaytg.richworld.limits.*;
import ru.kvaytg.richworld.perks.*;
import ru.kvaytg.richworld.spawn.SpawnHandler;

public class ComponentManager {

    private final RichWorld plugin;

    public ComponentManager(RichWorld plugin) {
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
        registerComponent(new HelpCommand(plugin));
        // Регистрация обработчика бренда
        registerComponent(new BrandHandler(plugin));
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
    }

}