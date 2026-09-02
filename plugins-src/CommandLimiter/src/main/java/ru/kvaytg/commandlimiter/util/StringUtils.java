package ru.kvaytg.commandlimiter.util;

public class StringUtils {

    private StringUtils() {
        throw new AssertionError("No instances allowed");
    }

    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen = length(cs);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNullOrBlank(CharSequence cs) {
        return cs == null || isBlank(cs);
    }

}