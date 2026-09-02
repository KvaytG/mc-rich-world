package ru.kvaytg.commandlimiter.util;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private ColorUtils() {
        throw new AssertionError("No instances allowed");
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    public static String colorize(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String hexCode = matcher.group(1);
            StringBuilder replacement = new StringBuilder(ChatColor.COLOR_CHAR + "x");
            for (char ch : hexCode.toCharArray()) {
                replacement.append(ChatColor.COLOR_CHAR).append(ch);
            }
            matcher.appendReplacement(builder, replacement.toString());
        }
        matcher.appendTail(builder);
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }

}