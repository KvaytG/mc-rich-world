package ru.kvaytg.coremc.utils.other;

import ru.kvaytg.coremc.utils.ObjectUtils;
import ru.kvaytg.coremc.utils.StringUtils;

public class DotPath {

    public static final String SEPARATOR = ".";
    private final String path;

    public DotPath(String basePath, String... pathSegments) {
        path = buildPath(basePath, pathSegments);
    }

    private static String normalizePathSegment(String pathSegment) {
        return StringUtils.strip(pathSegment.trim().replaceAll("\\.+", SEPARATOR), SEPARATOR);
    }

    private static String buildPath(String basePath, String... pathSegments) {
        basePath = basePath != null ? basePath : "";
        if (ObjectUtils.isNullOrEmpty(pathSegments)) return basePath;
        StringBuilder result = new StringBuilder(basePath);
        for (String segment : pathSegments) {
            if (StringUtils.isNullOrBlank(segment)) continue;
            if (!result.isEmpty()) result.append(SEPARATOR);
            result.append(normalizePathSegment(segment));
        }
        return result.toString();
    }

    public DotPath add(String... pathSegments) {
        return new DotPath(path, pathSegments);
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return path;
    }

}