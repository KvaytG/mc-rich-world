package ru.kvaytg.coremc.config.constants;

public enum Placeholder {

    PLAYER("{PLAYER}"),
    ALIAS("{ALIAS}"),
    REASON("{REASON}"),
    WARP("{WARP}"),
    MINUTES("{MINUTES}"),
    VALUE("{VALUE}");

    private final String placeholder;

    Placeholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String get() {
        return placeholder;
    }

}