package net.mat0u5.matlib.platform.fabric;

//? fabric {

import net.mat0u5.matlib.MatLib;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ModInitializer;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		MatLib.onInitialize();
	}
}
//?}
