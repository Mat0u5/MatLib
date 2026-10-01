package net.mat0u5.matlib.utils.resource;

import net.mat0u5.matlib.MatLib;

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

            String absolutePath = resourcePath.startsWith("/") ? resourcePath : "/" + resourcePath;
            String relativePath = absolutePath.substring(1);

            InputStream in = getClass().getResourceAsStream(absolutePath);
            if (in == null) {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(relativePath);
            }

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
}