package ru.kvaytg.coremc.config;

import ru.kvaytg.colorapi.ColorAPI;
import ru.kvaytg.coremc.utils.StringUtils;

public abstract class AbstractConfigOption {

    private final String main;
    private final String alternative;

    public String process(String text, boolean colorize) {
        return StringUtils.isNullOrBlank(text) ? null
                : colorize ? ColorAPI.colorize(text) : text;
    }

    public AbstractConfigOption(String main, String alternative, boolean colorize) {
        this.main = process(main, colorize);
        this.alternative = process(alternative, colorize);
    }

    public String getMain() {
        return main;
    }

    public String getAlternative() {
        return alternative;
    }

}