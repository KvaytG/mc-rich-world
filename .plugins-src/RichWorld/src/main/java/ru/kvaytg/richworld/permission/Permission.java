package ru.kvaytg.richworld.permission;

import ru.kvaytg.richworld.config.AbstractConfigOption;

public class Permission extends AbstractConfigOption {

    public Permission(String permissionMain, String permissionAlternative) {
        super(permissionMain, permissionAlternative, false);
    }

    public String get() {
        return getMain() != null ? getMain() : getAlternative();
    }

}