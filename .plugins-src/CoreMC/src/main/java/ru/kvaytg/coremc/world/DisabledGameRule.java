package ru.kvaytg.coremc.world;

import org.bukkit.GameRule;

public enum DisabledGameRule {

    DAYLIGHT(GameRule.DO_DAYLIGHT_CYCLE, false),
    WEATHER(GameRule.DO_WEATHER_CYCLE, false),
    ADVANCEMENTS(GameRule.ANNOUNCE_ADVANCEMENTS, false),
    TICK_SPEED(GameRule.RANDOM_TICK_SPEED, 0);

    private final GameRule<?> gameRule;
    private final Object value;

    <T> DisabledGameRule(GameRule<T> gameRule, T value) {
        this.gameRule = gameRule;
        this.value = value;
    }

    public GameRule<?> getRule() {
        return gameRule;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

}