package ru.kvaytg.coremc.config;

import ru.kvaytg.coremc.CoreMc;

public enum ConfigManager {

    INSTANCE;

    private MainConfig mainConfig;
    private WarpConfig warpConfig;
    private JoinItemsConfig joinItemsConfig;
    private MuteConfig muteConfig;

    public void init(CoreMc plugin) {
        new MessageConfig(plugin);
        new PermissionConfig(plugin);
        mainConfig = new MainConfig(plugin);
        warpConfig = new WarpConfig(plugin);
        joinItemsConfig = new JoinItemsConfig(plugin);
        muteConfig = new MuteConfig(plugin);
    }

    public void saveAll() {
        muteConfig.saveMutes();
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

    public MuteConfig getMuteConfig() {
        return muteConfig;
    }

}