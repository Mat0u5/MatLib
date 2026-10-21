package net.mat0u5.matlib.client.utils;

import com.google.auto.service.AutoService;
import com.mojang.blaze3d.platform.NativeImage;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.events.ClientNetworkEvents;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.network.packets.TexturePayload;
import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.mat0u5.matlib.utils.resource.SyncedTextureFolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(RegistrableClient.class)
public class SyncedTexturesClient implements RegistrableClient {
	private static final Map<String, Map<String, Identifier>> textures = new ConcurrentHashMap<>();

	@Override
	public void onRegister() {
		ClientNetworkEvents.RECEIVE_CUSTOM_PACKET.register(SyncedTexturesClient::onCustomPayload);
	}

	private static boolean onCustomPayload(CustomPacketPayload customPacketPayload) {
		if (customPacketPayload instanceof TexturePayload payload) {
			Minecraft.getInstance().execute(() -> handleTexture(payload.folderId(), payload.name(), payload.textureData()));
			return true;
		}
		return false;
	}

	public static void handleTexture(String folderId, String name, byte[] textureData) {
		try {
			MatLib.DEBUG.network.syncedTextures.log("Received texture '{}' for {} ({} bytes)", name, folderId, textureData.length);
			Identifier folder = IdentifierHelper.parse(folderId);
			Identifier textureId = IdentifierHelper.of(folder.getNamespace(), "dynamic/" + folder.getPath() + "/" + name);

			NativeImage image = NativeImage.read(new ByteArrayInputStream(textureData));

			SyncedTextureFolder syncedFolder = SyncedTextureFolder.get(folderId);
			if (syncedFolder != null) {
				boolean badWidth = syncedFolder.getTextureWidth() > 0 && image.getWidth() != syncedFolder.getTextureWidth();
				boolean badHeight = syncedFolder.getTextureHeight() > 0 && image.getHeight() != syncedFolder.getTextureHeight();
				if (badWidth || badHeight) {
					MatLib.LOGGER.info("Texture '{}' for {} has wrong dimensions, ignoring.", name, folderId);
					image.close();
					return;
				}
			}

			//? if <= 1.21.4 {
			/*DynamicTexture texture = new DynamicTexture(image);
			*///?} else {
			DynamicTexture texture = new DynamicTexture(() -> name, image);
			//?}
			removeTexture(folderId, name);
			Minecraft.getInstance().getTextureManager().register(textureId, texture);
			textures.computeIfAbsent(folderId, key -> new ConcurrentHashMap<>()).put(name, textureId);
			MatLib.LOGGER.info("Added texture '{}'", textureId);
		} catch (Exception e) {
			MatLib.LOGGER.error("Error while loading texture '{}' for {}", name, folderId, e);
		}
	}

	@Nullable
	public static Identifier getTexture(Identifier folderId, String name) {
		return getTexture(folderId.toString(), name);
	}

	@Nullable
	public static Identifier getTexture(String folderId, String name) {
		if (name == null) return null;
		Map<String, Identifier> folderTextures = textures.get(folderId);
		if (folderTextures == null) return null;
		return folderTextures.get(name);
	}

	public static void removeTexture(String folderId, String name) {
		Map<String, Identifier> folderTextures = textures.get(folderId);
		if (folderTextures == null) return;
		Identifier textureId = folderTextures.remove(name);
		if (textureId != null) {
			Minecraft.getInstance().getTextureManager().release(textureId);
		}
	}
}
