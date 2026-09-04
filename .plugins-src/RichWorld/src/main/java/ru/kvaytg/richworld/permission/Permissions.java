package ru.kvaytg.richworld.permission;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kvaytg.richworld.config.PermissionConfig;
import ru.kvaytg.richworld.utils.ConfigUtils;
import java.util.function.Function;

public enum Permissions {

    VANISH_USE(config -> config.getPermission("vanishUse", "vanish", "use")),
    VANISH_ME(config -> config.getPermission("vanishMe", "vanish", "me")),
    VANISH_OTHERS(config -> config.getPermission("vanishOthers", "vanish", "others")),
    VANISH_INVENTORY(config -> config.getPermission("vanishInventory", "vanish", "inventory")),
    WARP_USE(config -> config.getPermission("warpUse", "warp", "use")),
    WARP_ME(config -> config.getPermission("warpMe", "warp", "me")),
    WARP_OTHERS(config -> config.getPermission("warpOthers", "warp", "others")),
    KICK_USE(config -> config.getPermission("kickUse", "kick", "use")),
    KILL_USE(config -> config.getPermission("killUse", "kill", "use")),
    FLY_USE(config -> config.getPermission("flyUse", "fly", "use"));

    private static PermissionConfig config;
    private final Function<PermissionConfig, Permission> provider;

    Permissions(Function<PermissionConfig, Permission> provider) {
        this.provider = provider;
    }

    public static void init(PermissionConfig permissionConfig) {
        config = permissionConfig;
    }

    public boolean hasNo(CommandSender holder) {
        if (!(holder instanceof Player)) return false;
        Permission configPermission = ConfigUtils.applyWithConfigCheck(
                config,
                provider,
                "Permissions class is not initialized. Call init() first."
        );
        return !holder.hasPermission(configPermission.get());
    }

}