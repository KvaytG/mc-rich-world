package ru.kvaytg.coremc.component;

public abstract class Enabling {

    private final boolean enabled;

    public Enabling(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

}