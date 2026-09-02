package ru.kvaytg.richdonate;

/*
*
* ОБЩИЙ КЛАСС
*
* Хранилище шаблонов команд
*
*/
public enum ChannelCommand {

    BALANCE_GIVE("BALANCE_GIVE {PLAYER} {AMOUNT}"),
    BALANCE_TAKE("BALANCE_TAKE {PLAYER} {AMOUNT}"),

    REQUEST_BALANCE("REQUEST_BALANCE {PLAYER}"),
    RESPONSE_BALANCE("RESPONSE_BALANCE {PLAYER} {AMOUNT}"),

    STATUS_GIVE("STATUS_GIVE {PLAYER} {STATUS}"),
    STATUS_TAKE("STATUS_TAKE {PLAYER} {REASON}"),

    REQUEST_STATUS("REQUEST_STATUS {PLAYER}"),
    RESPONSE_STATUS("RESPONSE_STATUS {PLAYER} {STATUS}");

    private final String pattern;

    ChannelCommand(String pattern) {
        this.pattern = pattern.replaceAll("\\{[^}]+}", "{}").trim();
    }

    public String getText(Object... args) {
        String result = pattern;
        for (Object arg : args) {
            result = result.replaceFirst("\\{}", arg.toString());
        }
        return result.replace("{}", "EMPTY");
    }

    public String getName() {
        return pattern.split(" ")[0];
    }

}