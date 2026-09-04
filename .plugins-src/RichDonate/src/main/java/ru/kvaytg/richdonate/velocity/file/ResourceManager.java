package ru.kvaytg.richdonate.velocity.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
*
* Менеджер jar-ресурсов на стороне Velocity
*
*/
public enum ResourceManager {

    INSTANCE;

    private static final Logger logger = Logger.getLogger(ResourceManager.class.getName());

    public static final Path DATA_FOLDER = Paths.get("plugins", "RichDonate");

    @Nullable
    public InputStream getResource(@NotNull String filename) {
        try {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url == null) {
                return null;
            }
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error fetching resource: " + filename, ex);
            return null;
        }
    }

    public void saveResource(@NotNull String resourcePath, boolean replace) {
        if (resourcePath.isEmpty()) {
            throw new IllegalArgumentException("Resource path cannot be null or empty");
        }
        resourcePath = resourcePath.replace('\\', '/');
        try (InputStream in = getResource(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
            }
            Path outFilePath = DATA_FOLDER.resolve(resourcePath);
            Path outDir = outFilePath.getParent();
            Files.createDirectories(outDir);
            if (Files.exists(outFilePath) && !replace) {
                //System.out.println("Resource '" + resourcePath + "' already exists, skipping save.");
                return;
            }
            try (OutputStream out = Files.newOutputStream(outFilePath)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                System.out.println("Resource saved: " + resourcePath);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error saving resource: " + resourcePath, ex);
        }
    }

}