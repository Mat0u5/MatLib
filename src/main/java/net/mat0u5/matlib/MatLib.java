package net.mat0u5.matlib;

import net.mat0u5.matlib.api.ApiProvider;
import net.mat0u5.matlib.api.MatLibClientInitializer;
import net.mat0u5.matlib.api.MatLibInitializer;
import net.mat0u5.matlib.platform.Platform;
import net.mat0u5.matlib.registries.MobRegistry;
import net.mat0u5.matlib.registries.ModRegistries;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//? fabric {
import net.mat0u5.matlib.platform.fabric.FabricPlatform;
import org.jetbrains.annotations.Nullable;
//?} neoforge {
/*import net.mat0u5.matlib.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import net.mat0u5.matlib.platform.forge.ForgePlatform;
*///?}

//? fabric && <= 1.20.5 {
/*import net.mat0u5.matlib.events.common.CommonRegistryEvents;
*///?}

public class MatLib {

	public static final boolean DEBUG = true;
	public static final String MOD_ID = "matlib";
	public static final String MOD_VERSION = "0.2.0";
	public static final String MOD_FRIENDLY_NAME = "MatLib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	private static final Platform PLATFORM = createPlatformInstance();
	public static MinecraftServer server;

	public static void onRegister() {
		ApiProvider.callListeners(MatLibInitializer.class, MatLibInitializer::onRegister);
		ApiProvider.callListeners(MatLibClientInitializer.class, MatLibClientInitializer::onRegister);
	}

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, platform().loader());
		LOGGER.info("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
		oldRegister();
		ApiProvider.callListeners(MatLibInitializer.class, MatLibInitializer::onInitialize);

		ModRegistries.initialize();
	}

	public static void oldRegister() {
		//? fabric && <= 1.20.5 {
		/*onRegister();
		CommonRegistryEvents.PRE_FREEZE.invoker().onPreFreeze();
		*///?}

		//? fabric || (forge && > 1.21) {
		MobRegistry.registerAttributes();
		//?}
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

	public static void setServer(MinecraftServer server) {
		MatLib.server = server;
	}

	@Nullable
	public static MinecraftServer server() {
		return server;
	}
}
