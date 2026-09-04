package ru.kvaytg.richpit.handler;

import ru.kvaytg.richpit.RichPit;

public enum HandlerManager {

    INSTANCE;

    public void registerAll(RichPit plugin) {
        new JoinHandler(plugin);
        new NightVisionHandler(plugin);
        new ItemHandler(plugin);
        new DeathHandler(plugin);
    }

}