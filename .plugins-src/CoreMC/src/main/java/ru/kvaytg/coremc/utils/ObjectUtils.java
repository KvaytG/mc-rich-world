package ru.kvaytg.coremc.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ObjectUtils {

    private ObjectUtils() {}

    public static boolean isNullOrEmpty(Object object) {
        if (object == null) {
            return true;
        } else if (object instanceof CharSequence) {
            return ((CharSequence) object).isEmpty();
        } else if (object.getClass().isArray()) {
            return Array.getLength(object) == 0;
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object instanceof Optional) {
            return ((Optional<?>) object).isEmpty();
        } else {
            return false;
        }
    }

}