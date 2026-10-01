package net.mat0u5.matlib.utils.other;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;

//? if >= 1.20.5 {
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import java.util.Optional;
//?}

//? if fabric
import net.fabricmc.loader.api.FabricLoader;

//? if neoforge {
/*import net.neoforged.fml.ModList;
    //? if > 1.21.6 {
    import net.neoforged.neoforgespi.language.IModFileInfo;
    import net.neoforged.neoforgespi.locating.IModFile;
    //?}
*///?}

public final class ModBuiltInPacks {
    private static final Logger LOGGER = LoggerFactory.getLogger("MatLibPackLoader");

    public record PackDef(Identifier id, Component title, boolean required) {}

    public static void loadPacks(Consumer<Pack> consumer, PackType packType, List<PackDef> packDefs) {
        for (PackDef def : packDefs) {
            Pack pack = createPack(def, packType);
            if (pack != null) consumer.accept(pack);
        }
    }

    private static @Nullable Pack createPack(PackDef def, PackType packType) {
        String internalPath = "/resourcepacks/" + def.id().getPath();
        Path path = getResourceAsPath(def.id().getNamespace(), internalPath);

        if (path == null || !Files.isDirectory(path)) {
            LOGGER.warn("[MatLib] Built-in pack folder not found: {}", internalPath);
            return null;
        }

        String packId = def.id().toString();
        //? if <= 1.20.3 {
        /*return Pack.readMetaAndCreate(
                packId,
                def.title(),
                def.required(),
                //? if <= 1.20 {
                /^(name) -> new PathPackResources(name, path, true),
                ^///?} else {
                new PathPackResources.PathResourcesSupplier(path, true),
                //?}
                packType,
                Pack.Position.TOP,
                PackSource.BUILT_IN
        );
        *///?} else {
        PackLocationInfo locationInfo = new PackLocationInfo(
                packId,
                def.title(),
                PackSource.BUILT_IN,
                Optional.empty() // knownPackInfo
        );

        PackSelectionConfig selectionConfig = new PackSelectionConfig(
                def.required(),
                Pack.Position.TOP,
                false // fixedPosition
        );

        return Pack.readMetaAndCreate(
                locationInfo,
                new PathPackResources.PathResourcesSupplier(path),
                packType,
                selectionConfig
        );
        //?}

    }

    private static @Nullable Path getResourceAsPath(String namespace, String path) {
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;

        //? if fabric {
        try {
            return FabricLoader.getInstance()
                    .getModContainer(namespace)
                    .flatMap(mod -> mod.findPath(cleanPath))
                    .orElse(null);
        } catch (Exception e) {
            LOGGER.error("Failed to resolve path for built-in pack: " + path, e);
            return null;
        }
        //?} else if neoforge {
        /*
        //? if <= 1.21.6 {
        /^try {
            return net.neoforged.fml.ModList.get().getModFileById(namespace)
                    .getFile()
                    .findResource(cleanPath);
        } catch (Exception e) {
            LOGGER.error("Failed to resolve path for built-in pack: " + path, e);
            return null;
        }
        ^///?} else {
        IModFileInfo info = ModList.get().getModFileById(namespace);
        if (info == null) return null;

        IModFile modFile = info.getFile();

        for (Path jarOrRoot : modFile.getContents().getContentRoots()) {
            try {
                FileSystem zipFs;
                Path root;
                if (Files.isRegularFile(jarOrRoot)) {
                    zipFs = FileSystems.newFileSystem(jarOrRoot, (ClassLoader) null);
                    root = zipFs.getRootDirectories().iterator().next();
                } else {
                    zipFs = null;
                    root = jarOrRoot;
                }

                Path candidate = root;
                for (String segment : cleanPath.split("/")) {
                    candidate = candidate.resolve(segment);
                }

                if (Files.exists(candidate.resolve("pack.mcmeta"))) {
                    return candidate;
                }

                if (zipFs != null) zipFs.close();
            } catch (Exception e) {
                LOGGER.error("[MatLib] Failed to open mod JAR as ZIP filesystem", e);
            }
        }

        return null;
        //?}
        *///?} else if forge {
        /*try {
            return ModList.get().getModFileById(namespace)
                    .getFile()
                    .findResource(cleanPath);
        } catch (Exception e) {
            LOGGER.error("Failed to resolve path for built-in pack: " + path, e);
            return null;
        }
        *///?}
    }
}