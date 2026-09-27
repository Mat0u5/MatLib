package net.mat0u5.matlib.utils.interfaces;

import net.mat0u5.matlib.services.VersionTrackedMod;

public record VersionTrackedModImpl(String id, String version, String readableName) implements VersionTrackedMod {
	@Override
	public String modId() {
		return id;
	}

	@Override
	public String modReadableName() {
		return readableName;
	}

	@Override
	public String modVersion() {
		return version;
	}
}
