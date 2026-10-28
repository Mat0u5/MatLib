package net.mat0u5.matlib.utils.other;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RegistrySyncUtils {
	//? neoforge {
	/*// Tracks which players registry is currently being modified
	private static final ThreadLocal<UUID> CONFIGURING_PLAYER = new ThreadLocal<>();

	public static void runConfigurationTask(@Nullable UUID playerUUID, Runnable task) {
		CONFIGURING_PLAYER.set(playerUUID);
		try {
			task.run();
		} finally {
			CONFIGURING_PLAYER.remove();
		}
	}

	@Nullable
	public static UUID getConfiguringPlayer() {
		return CONFIGURING_PLAYER.get();
	}
	*///?}

	public static boolean editRegistryMap(@Nullable Map<Identifier, Object2IntMap<Identifier>> registryMap, UUID playerUUID) {
		if (registryMap == null) return false;

		Map<Identifier, Collection<Identifier>> registries = new LinkedHashMap<>();
		registryMap.forEach((registry, registryEntries) -> registries.put(registry, registryEntries.keySet()));
		return editRegistries(registries, playerUUID);
	}

	public static boolean editRegistries(Map<Identifier, ? extends Collection<Identifier>> registries, UUID playerUUID) {
		Set<String> trackedNamespaces = new HashSet<>();
		Set<String> strippedNamespaces = new HashSet<>();
		for (VersionTrackedMod mod : VersionTrackedMods.CACHED_VERSION_TRACKED_MODS) {
			String namespace = mod.modId().toLowerCase(Locale.ROOT);
			trackedNamespaces.add(namespace);
			try {
				if (mod.shouldStripRegistriesFor(playerUUID)) {
					strippedNamespaces.add(namespace);
				}
			} catch (Exception e) {
				MatLib.LOGGER.error("Error while checking registry edits for {}", mod.modId(), e);
			}
		}

		boolean hasTrackedEntries = false;
		List<String> removedEntries = new ArrayList<>();
		for (Map.Entry<Identifier, ? extends Collection<Identifier>> entry : registries.entrySet()) {
			Identifier registry = entry.getKey();
			Collection<Identifier> registryEntries = entry.getValue();
			for (Identifier value : registryEntries) {
				if (trackedNamespaces.contains(value.getNamespace().toLowerCase(Locale.ROOT))) {
					hasTrackedEntries = true;
					break;
				}
			}
			registryEntries.removeIf(value -> {
				if (strippedNamespaces.contains(value.getNamespace().toLowerCase(Locale.ROOT))) {
					removedEntries.add(registry.getPath() + ":" + value);
					return true;
				}
				return false;
			});
		}

		if (removedEntries.isEmpty()) {
			MatLib.LOGGER.info("Sending unmodified registry entries to client");
		}
		else {
			MatLib.LOGGER.info("Sending modified registry entries to client. Removed: " + String.join(", ", removedEntries));
		}
		return hasTrackedEntries;
	}

	public static boolean shouldPreventDisconnect(Component reason) {
		if (reason == null) return false;
		String text = reason.getString().toLowerCase(Locale.ROOT);
		if (!text.contains("registr")) return false;
		for (String modId : VersionTrackedMods.getTrackedModIds()) {
			if (modId.equals(MatLib.MOD_ID)) continue;
			if (text.contains(modId.toLowerCase(Locale.ROOT))) return true;
		}
		return false;
	}
}
