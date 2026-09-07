package ru.kvaytg.coremc.world;

public enum WorldSetting {

    DAYLIGHT_CYCLE("daylightCycle", DisabledGameRule.DAYLIGHT),
    WEATHER_CYCLE("weatherCycle", DisabledGameRule.WEATHER),
    ADVANCEMENTS("advancements", DisabledGameRule.ADVANCEMENTS),
    RANDOM_TICK_SPEED("tickSpeed", DisabledGameRule.TICK_SPEED),
    MOB_AI("mobAi", null);

    private final String configName;
    private final DisabledGameRule gameRule;

    WorldSetting(String configName, DisabledGameRule gameRule) {
        this.configName = configName;
        this.gameRule = gameRule;
    }

    public String getName() {
        return configName;
    }

    public DisabledGameRule getRule() {
        return gameRule;
    }

}