package net.mat0u5.matlib.network;

import net.mat0u5.matlib.MatLibVersionControl;
import net.mat0u5.matlib.network.packets.HandshakePayload;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.mat0u5.matlib.utils.interfaces.VersionTrackedModImpl;
import net.mat0u5.matlib.utils.other.VersionCompatibility;

import java.util.ArrayList;
import java.util.List;

public class MatLibNetworkHandlerCommon {

	public static VersionCompatibility.Result getCompatibilityResult(HandshakePayload payload, boolean isServer) {
		List<String> modIds = payload.modIds();
		List<String> modVersions = payload.modVersions();
		List<String> modReadableNames = payload.modReadableNames();
		if (modIds == null || modVersions == null || modReadableNames == null) return VersionCompatibility.Result.PASS;
		if (modIds.size() != modVersions.size()) return VersionCompatibility.Result.PASS;
		if (modIds.size() != modReadableNames.size()) return VersionCompatibility.Result.PASS;
		int modsLength = modIds.size();

		for (VersionTrackedMod localMod : MatLibVersionControl.CACHED_VERSION_TRACKED_MODS) {
			for (int i = 0; i < modsLength; i++) {
				VersionTrackedModImpl otherMod = new VersionTrackedModImpl(modIds.get(i), modVersions.get(i), modReadableNames.get(i));
				VersionCompatibility.Result result = VersionCompatibility.passes(localMod, otherMod, isServer);
				if (!result.passes()) return result;
			}
		}
		return VersionCompatibility.Result.PASS;
	}

	public static HandshakePayload getHandshakePayload() {
		List<String> modIds = new ArrayList<>();
		List<String> modVersions = new ArrayList<>();
		List<String> modReadableNames = new ArrayList<>();
		for (VersionTrackedMod localMod : MatLibVersionControl.CACHED_VERSION_TRACKED_MODS) {
			modIds.add(localMod.modId());
			modVersions.add(localMod.modVersion());
			modReadableNames.add(localMod.modReadableName());
		}
		return new HandshakePayload(modIds, modVersions, modReadableNames);
	}
}
