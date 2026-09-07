package ru.kvaytg.coremc.utils;

public class StringUtils {

    private StringUtils() {}

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

    public static String stripStart(String str, String stripChars) {
        int strLen = length(str);
        if (strLen == 0) return str;
        int start = 0;
        if (stripChars == null) {
            while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else {
            if (stripChars.isEmpty()) return str;
            while (start != strLen && stripChars.indexOf(str.charAt(start)) != -1) {
                start++;
            }
        }
        return str.substring(start);
    }

    public static String stripEnd(String str, String stripChars) {
        int end = length(str);
        if (end == 0) return str;
        if (stripChars == null) {
            while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                --end;
            }
        } else {
            if (stripChars.isEmpty()) return str;
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                --end;
            }
        }
        return str.substring(0, end);
    }

    public static String strip(String str, String stripChars) {
        str = stripStart(str, stripChars);
        return stripEnd(str, stripChars);
    }

}