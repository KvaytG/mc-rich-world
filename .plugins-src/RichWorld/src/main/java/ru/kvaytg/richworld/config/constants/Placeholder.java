package ru.kvaytg.richworld.config.constants;

public enum Placeholder {

    PLAYER("{PLAYER}"),
    ALIAS("{ALIAS}"),
    REASON("{REASON}"),
    WARP("{WARP}");

    private final String placeholder;

    Placeholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String get() {
        return placeholder;
    }

}