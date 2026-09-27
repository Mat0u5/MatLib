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

	@Override
	public VersionCompatibility clientCompatibility() {
		return VersionCompatibility.any();
	}

	@Override
	public VersionCompatibility serverCompatibility() {
		return VersionCompatibility.any();
	}
}
