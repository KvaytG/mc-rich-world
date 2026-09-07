package ru.kvaytg.coremc.console;

import org.apache.logging.log4j.LogManager;
import ru.kvaytg.coremc.config.ConfigManager;
import ru.kvaytg.coremc.config.constants.ConfigSection;
import ru.kvaytg.coremc.utils.other.DotPath;
import java.util.List;

public enum ConsoleManager {

    INSTANCE;

    public void init() {
        org.apache.logging.log4j.core.Logger logger;
        logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        DotPath path = ConfigSection.CONSOLE.getDotPath().add("filter");
        List<String> blockedPhrases = ConfigManager.INSTANCE.getMainConfig().getList(path);
        logger.addFilter(new ConsoleFilter(blockedPhrases));
    }

}