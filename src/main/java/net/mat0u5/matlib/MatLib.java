package net.mat0u5.matlib;

import net.mat0u5.matlib.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//? fabric {
import net.mat0u5.matlib.platform.fabric.FabricPlatform;
//?} neoforge {
/*import net.mat0u5.matlib.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import net.mat0u5.matlib.platform.forge.ForgePlatform;
*///?}

public class MatLib {

	public static final String MOD_ID = "matlib";
	public static final String MOD_VERSION = "0.0.2";
	public static final String MOD_FRIENDLY_NAME = "MatLib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, platform().loader());
		LOGGER.info("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static Platform platform() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		return new FabricPlatform();
		//?} neoforge {
		/*return new NeoforgePlatform();
		 *///?} forge {
		/*return new ForgePlatform();
		*///?}
	}
}
