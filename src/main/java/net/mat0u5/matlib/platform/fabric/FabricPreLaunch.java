package net.mat0u5.matlib.platform.fabric;

//? fabric {

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.mat0u5.matlib.MatLib;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;

@Entrypoint("main")
public class FabricPreLaunch implements PreLaunchEntrypoint {

	@Override
	public void onPreLaunch() {
		MatLib.onRegister();
	}
}
//?}
