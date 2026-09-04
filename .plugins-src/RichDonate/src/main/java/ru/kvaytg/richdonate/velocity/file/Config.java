package ru.kvaytg.richdonate.velocity.file;

import org.ini4j.Wini;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
*
* Экземпляр ini-конфигурации на стороне Velocity
*
*/
public class Config {

    private static final Logger logger = Logger.getLogger(Config.class.getName());

    private final File file;

    public Config(String fileName) {
        this.file = new File(fileName);
    }

    public void save(String sectionName, Map<String, String> entries) {
        if (sectionName == null || sectionName.isEmpty() || entries == null || entries.isEmpty()) {
            return;
        }
        sectionName = sectionName.toUpperCase();
        try {
            Wini ini = new Wini(file);
            for (Entry<String, String> entry : entries.entrySet()) {
                ini.put(sectionName, entry.getKey(), entry.getValue());
            }
            ini.store();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error loading the section:: " + sectionName, ex);
        }
    }

    public Map<String, String> load(String sectionName) {
        Map<String, String> result = new HashMap<>();
        if (sectionName == null || sectionName.isEmpty()) {
            return result;
        }
        sectionName = sectionName.toUpperCase();
        try {
            Wini readIni = new Wini(file);
            if (readIni.containsKey(sectionName)) {
                result.putAll(readIni.get(sectionName));
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error loading the section:: " + sectionName, ex);
        }
        return result;
    }

}