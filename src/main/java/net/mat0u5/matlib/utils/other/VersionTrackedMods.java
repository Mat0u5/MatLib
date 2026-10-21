package net.mat0u5.matlib.utils.other;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.services.ServiceProvider;
import net.mat0u5.matlib.services.VersionTrackedMod;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@AutoService(RegistrableServer.class)
public class VersionTrackedMods implements RegistrableServer {
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

	@Nullable
	public static VersionTrackedMod getModFromId(String id) {
		for (VersionTrackedMod localMod : CACHED_VERSION_TRACKED_MODS) {
			if (localMod.modId().equals(id)) {
				return localMod;
			}
		}
		return null;
	}
}
