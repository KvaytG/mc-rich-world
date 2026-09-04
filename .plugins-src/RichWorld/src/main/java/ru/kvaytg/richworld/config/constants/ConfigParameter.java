package ru.kvaytg.richworld.config.constants;

public enum ConfigParameter {

    ENABLED("enabled"),
    WORLDS("worlds");

    private final String parameter;

    ConfigParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter;
    }

}