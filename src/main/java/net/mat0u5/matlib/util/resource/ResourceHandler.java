package net.mat0u5.matlib.util.resource;

import net.mat0u5.matlib.MatLib;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceHandler {
    //? if fabric || (forge && > 1.20) {
    public void copyBundledSingleFile(String resourcePath, Path targetFile) {
        copyBundledSingleFile(resourcePath, targetFile, false);
    }
    public void copyBundledSingleFile(String resourcePath, Path targetFile, boolean silent) {
        try {
            Files.createDirectories(targetFile.getParent());

            URL resourceUrl = getClass().getResource(resourcePath);

            if (resourceUrl == null) {
                MatLib.LOGGER.error("File not found: " + resourcePath);
                return;
            }

            if (resourceUrl.getProtocol().equals("file")) {
                handleSingleFileNormal(targetFile, resourceUrl, silent);
            }
            else if (resourceUrl.getProtocol().equals("jar")) {
                handleSingleFileJar(targetFile, resourcePath, silent);
            }
            else {
                MatLib.LOGGER.error("Unsupported resource protocol: " + resourceUrl.getProtocol());
            }
        } catch (Exception e) {
            MatLib.LOGGER.error("Error copying bundled file: " + resourcePath, e);
        }
    }
    private void handleSingleFileNormal(Path targetFile, URL resourceUrl, boolean silent) {
        try {
            Path sourcePath = Paths.get(resourceUrl.toURI());

            if (Files.isRegularFile(sourcePath)) {
                Files.copy(sourcePath, targetFile, StandardCopyOption.REPLACE_EXISTING);
                if (!silent) MatLib.LOGGER.info("Copied file: {} -> {}", sourcePath, targetFile);
            } else {
                MatLib.LOGGER.error("Source is not a regular file: " + sourcePath);
            }
        } catch (Exception e) {
            MatLib.LOGGER.error("Error copying bundled file.", e);
        }
    }

    private void handleSingleFileJar(Path targetFile, String resourcePath, boolean silent) {
        try {
            try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
                if (in == null) {
                    MatLib.LOGGER.error("Could not find resource: " + resourcePath);
                    return;
                }

                Files.copy(in, targetFile, StandardCopyOption.REPLACE_EXISTING);
                if (!silent) MatLib.LOGGER.info("Copied file from JAR: {} -> {}", resourcePath, targetFile);
            }
        } catch (Exception e) {
            MatLib.LOGGER.error("Error copying file from JAR: " + resourcePath, e);
        }
    }
    //?} else {
    /*public void copyBundledSingleFile(String resourcePath, Path targetFile) {
        copyBundledSingleFile(resourcePath, targetFile, false);
    }
    public void copyBundledSingleFile(String resourcePath, Path targetFile, boolean silent) {
        try {
            Files.createDirectories(targetFile.getParent());

            InputStream in = getClass().getResourceAsStream(resourcePath);

            if (in == null) {
                in = getClass().getClassLoader().getResourceAsStream(
                        resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath
                );
            }

            if (in == null) {
                MatLib.LOGGER.error("File not found: " + resourcePath);
                return;
            }

            InputStream inFinal = in;
            try (inFinal) {
                Files.copy(inFinal, targetFile, StandardCopyOption.REPLACE_EXISTING);
                if (!silent) MatLib.LOGGER.info("Copied bundled file: {} -> {}", resourcePath, targetFile);
            }
        } catch (Exception e) {
            MatLib.LOGGER.error("Error copying bundled file: " + resourcePath, e);
        }
    }
    *///?}
}
