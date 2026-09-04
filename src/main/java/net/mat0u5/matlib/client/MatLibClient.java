package net.mat0u5.matlib.client;

import net.mat0u5.matlib.MatLib;

import static net.mat0u5.matlib.MatLib.*;

public class MatLibClient {
	public static void onInitializeClient() {
		MatLib.LOGGER.info("Initializing {} Client on {}", MOD_ID, platform().loader());
		MatLib.LOGGER.info("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}
}
