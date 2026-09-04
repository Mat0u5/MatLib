package net.mat0u5.matlib.client.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.mat0u5.matlib.client.MatLibClient;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MatLibClient.onInitializeClient();
	}

}
//?}
