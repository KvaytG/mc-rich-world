package ru.kvaytg.richworld.config;

import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.permission.Permission;
import ru.kvaytg.richworld.permission.Permissions;
import ru.kvaytg.richworld.utils.StringUtils;
import ru.kvaytg.richworld.utils.other.DotPath;

public class PermissionConfig extends AbstractConfig {

    public PermissionConfig(RichWorld plugin) {
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