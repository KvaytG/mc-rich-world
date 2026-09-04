package ru.kvaytg.richworld.config;

import ru.kvaytg.richworld.RichWorld;
import ru.kvaytg.richworld.message.Message;
import ru.kvaytg.richworld.message.Messages;
import ru.kvaytg.richworld.utils.StringUtils;

public class MessageConfig extends AbstractConfig {

    public MessageConfig(RichWorld plugin) {
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