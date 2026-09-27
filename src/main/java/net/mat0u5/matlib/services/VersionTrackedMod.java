package net.mat0u5.matlib.services;

import net.mat0u5.matlib.utils.other.VersionCompatibility;

public interface VersionTrackedMod {
	String modId();
	String modReadableName();
	String modVersion();
	default VersionCompatibility clientCompatibility() { return VersionCompatibility.any(); }
	default VersionCompatibility serverCompatibility() { return VersionCompatibility.any(); }
}
