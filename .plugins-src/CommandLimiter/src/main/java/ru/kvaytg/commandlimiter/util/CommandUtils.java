package ru.kvaytg.commandlimiter.util;

public class CommandUtils {

    private CommandUtils() {
        throw new AssertionError("No instances allowed");
    }

    public static String prepareCommand(String command) {
        return command.trim().toLowerCase();
    }

}