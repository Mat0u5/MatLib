package net.mat0u5.matlib.utils.resource;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.network.NetworkHandlerServer;
import net.mat0u5.matlib.network.packets.TexturePayload;
import net.mat0u5.matlib.utils.player.PlayerUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SyncedTextureFolder {
	private static final Map<String, SyncedTextureFolder> FOLDERS = new ConcurrentHashMap<>();

	private final Identifier id;
	private final List<Path> folders = new ArrayList<>();
	private final int textureWidth;
	private final int textureHeight;
	private final Set<String> excludedFiles = new HashSet<>();
	@Nullable
	private Consumer<File> preprocessor = null;

	public SyncedTextureFolder(Identifier id, Path folder, int textureWidth, int textureHeight) {
		this.id = id;
		this.folders.add(folder);
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		if (FOLDERS.containsKey(id.toString())) {
			MatLib.LOGGER.error("Tried to register duplicate synced texture folder: {}", id);
		}
		FOLDERS.put(id.toString(), this);
	}

	@Nullable
	public static SyncedTextureFolder get(String folderId) {
		return FOLDERS.get(folderId);
	}

	public SyncedTextureFolder addSyncedFolder(Path folder) {
		this.folders.add(folder);
		return this;
	}

	public SyncedTextureFolder excludeFiles(String... fileNames) {
		for (String fileName : fileNames) {
			excludedFiles.add(fileName.toLowerCase(Locale.ROOT));
		}
		return this;
	}

	public SyncedTextureFolder addTexturePreProcessor(Consumer<File> preprocessor) {
		this.preprocessor = preprocessor;
		return this;
	}

	public Identifier getId() {
		return id;
	}

	public Path getMainFolder() {
		return folders.get(0);
	}

	public int getTextureWidth() {
		return textureWidth;
	}

	public int getTextureHeight() {
		return textureHeight;
	}

	public boolean createFolder() {
		File folder = getMainFolder().toFile();
		if (folder.exists()) return true;
		if (!folder.mkdirs()) {
			MatLib.LOGGER.error("Failed to create folder {}", folder);
			return false;
		}
		return true;
	}

	public List<File> getFiles() {
		List<File> result = new ArrayList<>();
		for (Path folder : folders) {
			result.addAll(getFilesIn(folder));
		}
		return result;
	}

	public List<File> getFilesIn(Path folder) {
		List<File> result = new ArrayList<>();
		try {
			File[] files = folder.toFile().listFiles();
			if (files == null) return result;
			for (File file : files) {
				if (!file.isFile()) continue;
				String name = file.getName().toLowerCase(Locale.ROOT);
				if (excludedFiles.contains(name)) continue;
				if (!name.endsWith(".png")) continue;
				if (preprocessor != null) preprocessor.accept(file);
				result.add(file);
			}
		} catch (Exception e) {
			MatLib.LOGGER.error("Error while listing synced textures in {}", folder, e);
		}
		return result;
	}

	public static String getTextureName(File file) {
		String name = file.getName().toLowerCase(Locale.ROOT);
		return name.endsWith(".png") ? name.substring(0, name.length() - 4) : name;
	}

	public List<String> getAllTextureNames() {
		List<String> result = new ArrayList<>();
		for (File file : getFiles()) {
			result.add(getTextureName(file));
		}
		return result;
	}

	public void sendTo(ServerPlayer player) {
		sendTo(List.of(player));
	}

	public void sendToAll() {
		sendTo(PlayerUtils.getAllPlayers());
	}

	public void sendTo(Collection<ServerPlayer> players) {
		if (players.isEmpty()) return;
		for (File file : getFiles()) {
			try {
				String name = getTextureName(file);
				TexturePayload payload = new TexturePayload(id.toString(), name, Files.readAllBytes(file.toPath()));
				for (ServerPlayer player : players) {
					if (MatLib.DEBUG) MatLib.LOGGER.info("Sending texture '{}' from {} to {}", name, id, player);
					NetworkHandlerServer.sendPacket(player, payload);
				}
			} catch (Exception e) {
				MatLib.LOGGER.error("Error while sending synced texture {} from {}", file, id, e);
			}
		}
	}
}
