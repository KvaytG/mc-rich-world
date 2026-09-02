package ru.kvaytg.richworld.message;

import ru.kvaytg.richworld.config.AbstractConfigOption;

public class Message extends AbstractConfigOption {

    private final String consoleMessage;

    public Message(String playerMessageMain, String playerMessageAlternative, String consoleMessage) {
        super(playerMessageMain, playerMessageAlternative, true);
        this.consoleMessage = process(consoleMessage, true);
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }

}