package ru.kvaytg.coremc.config;

import ru.kvaytg.coremc.CoreMc;
import ru.kvaytg.coremc.message.Message;
import ru.kvaytg.coremc.message.Messages;
import ru.kvaytg.coremc.utils.StringUtils;

public class MessageConfig extends AbstractConfig {

    public MessageConfig(CoreMc plugin) {
        super(plugin, "messages.yml");
        Messages.init(this);
    }

    public Message getMessage(String messageId,
                              String alternativeMessage,
                              String consoleMessage) {
        String message = null;
        if (!StringUtils.isNullOrBlank(messageId)) {
            message = getConfig().getString(messageId);
        }
        return new Message(message, alternativeMessage, consoleMessage);
    }

    public Message getMessage(String messageId,
                              String alternativeMessage) {
        return getMessage(messageId, alternativeMessage, null);
    }

}