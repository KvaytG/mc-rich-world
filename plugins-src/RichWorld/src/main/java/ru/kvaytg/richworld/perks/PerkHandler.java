package ru.kvaytg.richworld.perks;

import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AutoConfigurableHandler;
import ru.kvaytg.richworld.config.constants.ConfigSection;

public abstract class PerkHandler extends AutoConfigurableHandler {

    public PerkHandler(RichWorld plugin, String configSection) {
        super(plugin, ConfigSection.PERKS.getDotPath().add(configSection));
    }

}