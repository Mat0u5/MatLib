package net.mat0u5.matlib;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.services.ServiceProvider;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.mat0u5.matlib.utils.other.VersionCompatibility;

import java.util.ArrayList;
import java.util.List;

@AutoService({VersionTrackedMod.class, RegistrableServer.class})
public class MatLibVersionControl implements VersionTrackedMod, RegistrableServer {
	public static List<VersionTrackedMod> CACHED_VERSION_TRACKED_MODS = new ArrayList<>();

	@Override
	public void onRegister() {
		resetCache();
	}

	public static void resetCache() {
		List<VersionTrackedMod> newList = new ArrayList<>();
		for (var mod : ServiceProvider.getListeners(VersionTrackedMod.class)) {
			newList.add(mod);
			MatLib.LOGGER.info("Loaded version tracked mod {} {}", mod.modId(), mod.modVersion());
		}
		CACHED_VERSION_TRACKED_MODS = newList;
	}

	public static List<String> getTrackedModIds() {
		List<String> result = new ArrayList<>();
		for (VersionTrackedMod localMod : CACHED_VERSION_TRACKED_MODS) {
			result.add(localMod.modId());
		}
		return result;
	}


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
