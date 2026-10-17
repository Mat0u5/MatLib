package net.mat0u5.matlib.services;

import net.mat0u5.matlib.utils.other.VersionCompatibility;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface VersionTrackedMod {
	String modId();
	String modReadableName();
	String modVersion();
	default VersionCompatibility clientCompatibility() { return VersionCompatibility.any(); }
	default VersionCompatibility serverCompatibility() { return VersionCompatibility.any(); }

	default boolean isUnstableVersion() {
		String version = modVersion();
		return version != null && (version.contains("dev") || version.contains("pre"));
	}

	/*
		Update Checking
	 */

	/**
	 * @return The GitHub repo used for update checking, formatted {@code owner/repo}, or null to disable update checking.
	 */
	@Nullable
	default String githubRepo() {
		return null;
	}

	/**
	 * @return The page where players can download the mod.
	 */
	@Nullable
	default String modrinthUrl() {
		return null;
	}

	/**
	 * @return Discord server invite link, shown on the update screen.
	 */
	@Nullable
	default String discordUrl() {
		return null;
	}

	/**
	 * @return Changelog link, shown on the update screen.
	 */
	@Nullable
	default String changelogUrl(String fromVersion, String toVersion) {
		String repo = githubRepo();
		if (repo == null) return null;
		return "https://github.com/" + repo + "/releases/tag/" + toVersion;
	}

	/**
	 * @return Whether MatLib should check GitHub for updates of this mod.
	 */
	default boolean checkForUpdates() {
		return githubRepo() != null;
	}

	/**
	 * Whether the registries sent to the client should be modified, to not kick the player from the game for having mismatched registry entries.
	 */
	default boolean shouldStripRegistriesFor(UUID playerUUID) {
		//return !NetworkHandlerServer.wasPreLoginHandshakeSuccessful(playerUUID, modId());
		return false;
	}
}
