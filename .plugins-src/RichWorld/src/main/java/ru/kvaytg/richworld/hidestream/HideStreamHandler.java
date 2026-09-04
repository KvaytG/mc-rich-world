package ru.kvaytg.richworld.hidestream;

import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.component.AutoConfigurableHandler;
import ru.kvaytg.richworld.config.constants.ConfigSection;

public abstract class HideStreamHandler extends AutoConfigurableHandler {

    public HideStreamHandler(RichWorld plugin, String enablingParameter) {
        super(plugin, ConfigSection.HIDE_STREAM.getDotPath(), enablingParameter);
    }

}