package ru.kvaytg.richdonate.velocity.file;

import org.ini4j.Wini;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private final Path file;

    public Config(String fileName) {
        this.file = new File(fileName).toPath();
    }

    public synchronized Map<String, String> load(String sectionName) {
        Map<String, String> result = new HashMap<>();
        if (sectionName == null || sectionName.isBlank()) return result;
        try {
            if (!Files.exists(file)) return result;
            Wini ini = new Wini(file.toFile());
            if (ini.containsKey(sectionName.toUpperCase())) {
                result.putAll(ini.get(sectionName.toUpperCase()));
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to load section " + sectionName, ex);
        }
        return result;
    }

    public synchronized void save(Map<String, Map<String, String>> sections) {
        if (sections == null || sections.isEmpty()) return;

        try {
            Path parent = file.getParent();
            if (parent != null) Files.createDirectories(parent);

            Path temp = file.resolveSibling(file.getFileName() + ".tmp");
            Files.deleteIfExists(temp);
            Files.createFile(temp);
            Wini ini = new Wini(temp.toFile());

            for (Map.Entry<String, Map<String, String>> section : sections.entrySet()) {
                for (Map.Entry<String, String> entry : section.getValue().entrySet()) {
                    ini.put(section.getKey().toUpperCase(), entry.getKey(), entry.getValue());
                }
            }

            ini.store();
            try {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Failed to save " + file, ex);
        }
    }

}