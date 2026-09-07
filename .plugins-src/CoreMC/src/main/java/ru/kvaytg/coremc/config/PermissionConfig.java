package ru.kvaytg.coremc.config;

import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.permission.Permission;
import ru.kvaytg.coremc.permission.Permissions;
import ru.kvaytg.coremc.utils.StringUtils;
import ru.kvaytg.coremc.utils.other.DotPath;

public class PermissionConfig extends AbstractConfig {

    public PermissionConfig(CoreMc plugin) {
        super(plugin, "permissions.yml");
        Permissions.init(this);
    }

    public Permission getPermission(String permissionId,
                                    String alternativePermissionBase,
                                    String... alternativePermissionContinued) {
        String permission = null;
        if (!StringUtils.isNullOrBlank(permissionId)) {
            permission = getConfig().getString(permissionId);
        }
        String alternativePermission = null;
        if (!StringUtils.isNullOrBlank(alternativePermissionBase)) {
            alternativePermission = new DotPath(
                    alternativePermissionBase,
                    alternativePermissionContinued
            ).getPath();
        }
        return new Permission(permission, alternativePermission);
    }

}