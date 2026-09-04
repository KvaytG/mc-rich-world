package ru.kvaytg.richdonate;

public enum ChannelCommand {

    BALANCE_GIVE(1),
    BALANCE_TAKE(2),
    REQUEST_BALANCE(3),
    RESPONSE_BALANCE(4),
    STATUS_GIVE(5),
    STATUS_TAKE(6),
    REQUEST_STATUS(7),
    RESPONSE_STATUS(8),
    PURCHASE_VIP(9),
    RESPONSE_PURCHASE(10);

    private final int id;

    ChannelCommand(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ChannelCommand fromId(int id) throws java.io.IOException {
        for (ChannelCommand command : values()) {
            if (command.id == id) return command;
        }
        throw new java.io.IOException("Unknown RichDonate command id: " + id);
    }

}