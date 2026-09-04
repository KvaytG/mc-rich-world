package ru.kvaytg.richworld.config;

import ru.kvaytg.richworld.RichWorld;

public enum ConfigManager {

    INSTANCE;

    private MainConfig mainConfig;
    private WarpConfig warpConfig;
    private JoinItemsConfig joinItemsConfig;

    public void init(RichWorld plugin) {
        new MessageConfig(plugin);
        new PermissionConfig(plugin);
        mainConfig = new MainConfig(plugin);
        warpConfig = new WarpConfig(plugin);
        joinItemsConfig = new JoinItemsConfig(plugin);
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public WarpConfig getWarpConfig() {
        return warpConfig;
    }

    public JoinItemsConfig getJoinItemsConfig() {
        return joinItemsConfig;
    }

}