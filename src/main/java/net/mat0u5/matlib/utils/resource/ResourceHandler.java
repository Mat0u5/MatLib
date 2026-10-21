package net.mat0u5.matlib.utils.resource;

import net.mat0u5.matlib.MatLib;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ResourceHandler {

    public void copyBundledSingleFile(String resourcePath, Path targetFile) {
        copyBundledSingleFile(resourcePath, targetFile, false);
    }

    public void copyBundledSingleFile(String resourcePath, Path targetFile, boolean silent) {
        try {
            if (targetFile.getParent() != null) {
                Files.createDirectories(targetFile.getParent());
            }

            InputStream in = openBundledResource(resourcePath);

            if (in == null) {
                MatLib.LOGGER.error("Bundled file not found in resources: {}", resourcePath);
                return;
            }

            try (InputStream stream = in) {
                Files.copy(stream, targetFile, StandardCopyOption.REPLACE_EXISTING);
                if (!silent) {
                    MatLib.LOGGER.info("Copied bundled file: {} -> {}", resourcePath, targetFile);
                }
            }
        } catch (Exception e) {
            MatLib.LOGGER.error("Error copying bundled file: " + resourcePath, e);
        }
    }

    @Nullable
    public static InputStream openBundledResource(String resourcePath) {
        String absolutePath = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
        String relativePath = absolutePath.substring(1);

        InputStream in = ResourceHandler.class.getResourceAsStream(absolutePath);
        if (in != null) return in;

        ClassLoader classLoader = ResourceHandler.class.getClassLoader();
        if (classLoader != null) {
            in = classLoader.getResourceAsStream(relativePath);
            if (in != null) return in;
        }

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader != null && contextClassLoader != classLoader) {
            in = contextClassLoader.getResourceAsStream(relativePath);
        }
        return in;
    }
}