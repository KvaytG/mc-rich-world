package ru.kvaytg.coremc.console;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsoleFilter extends AbstractFilter {

    private final Set<String> blockedPhrases;

    public ConsoleFilter(List<String> blockedPhrases) {
        this.blockedPhrases = blockedPhrases.stream()
                .map(this::prepareString)
                .collect(Collectors.toSet());
    }

    private String prepareString(String string) {
        return string.trim().toLowerCase();
    }

    private Result validateMessage(String message) {
        if (message == null) return Result.NEUTRAL;
        for (String blocked : blockedPhrases) {
            String preparedMessage = prepareString(message);
            if (preparedMessage.contains(blocked)) {
                return Result.DENY;
            }
        }
        return Result.NEUTRAL;
    }

    @Override
    public Result filter(LogEvent event) {
        return validateMessage(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return validateMessage(msg.toString());
    }

}