package net.mat0u5.matlib;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.mat0u5.matlib.utils.other.VersionCompatibility;

@AutoService(VersionTrackedMod.class)
public class MatLibVersionControl implements VersionTrackedMod {
	@Override
	public String modId() {
		return MatLib.MOD_ID;
	}

	@Override
	public String modReadableName() {
		return MatLib.MOD_FRIENDLY_NAME;
	}

	@Override
	public String modVersion() {
		return MatLib.MOD_VERSION;
	}


	/*
	 *     COMPATIBILITY TABLE
	 *   1.0.0       -   *
	 */
	@Override
	public VersionCompatibility clientCompatibility() {
		// This is the version that the SERVER needs to have for the current client.
		return VersionCompatibility.min("1.0.0");
	}

	@Override
	public VersionCompatibility serverCompatibility() {
		// This is the version that the CLIENT needs to have for the current server.
		return VersionCompatibility.min("1.0.0");
	}
}
