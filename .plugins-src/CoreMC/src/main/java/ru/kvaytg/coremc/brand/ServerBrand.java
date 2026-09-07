package ru.kvaytg.coremc.brand;

import org.bukkit.ChatColor;
import ru.kvaytg.coremc.ProjectInfo;

public class ServerBrand {

    private ServerBrand() {}

    private static final String MC_BRAND = ChatColor.GREEN + ProjectInfo.NAME + ChatColor.RESET;

    public static String get() {
        return MC_BRAND;
    }

}