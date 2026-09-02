package ru.kvaytg.richworld.brand;

import org.bukkit.ChatColor;
import ru.kvaytg.richworld.ProjectInfo;

public class ServerBrand {

    private ServerBrand() {}

    private static final String MC_BRAND = ChatColor.GREEN + ProjectInfo.NAME + ChatColor.RESET;

    public static String get() {
        return MC_BRAND;
    }

}