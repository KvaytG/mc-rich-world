package ru.kvaytg.coremc.utils.other;

public class PaperLib {

    private PaperLib() {}

    private static final boolean isPaper = hasClass("com.destroystokyo.paper.PaperConfig") ||
            hasClass("io.papermc.paper.configuration.Configuration");

    public static boolean isPaper() {
        return isPaper;
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}