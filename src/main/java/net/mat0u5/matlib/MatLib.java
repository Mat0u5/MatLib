package net.mat0u5.matlib;

import net.mat0u5.matlib.events.server.ServerPackSourceEvents;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.services.ServiceProvider;
import net.mat0u5.matlib.services.MultiplatformModInitializer;
import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.mat0u5.matlib.platform.Platform;
import net.mat0u5.matlib.registries.MobRegistry;
import net.mat0u5.matlib.utils.interfaces.ClientAccessor;
import net.mat0u5.matlib.utils.other.ModBuiltInPacks;
import net.mat0u5.matlib.utils.other.UpdateChecker;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

//? fabric {
import net.mat0u5.matlib.platform.fabric.FabricPlatform;
//?} neoforge {
/*import net.mat0u5.matlib.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import net.mat0u5.matlib.platform.forge.ForgePlatform;
*///?}

//? fabric && <= 1.20.5 {
/*import net.mat0u5.matlib.events.common.CommonRegistryEvents;
*///?}

public class MatLib {

	public static final boolean DEBUG = false; //TODO debug options for logging specific events
	public static final String MOD_ID = "matlib";
	public static final String MOD_VERSION = "0.6.1";
	public static final String MOD_FRIENDLY_NAME = "MatLib";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	private static final Platform PLATFORM = createPlatformInstance();
	public static MinecraftServer server;
	private static ClientAccessor clientAccessor;
	private static volatile boolean registered = false;
	private static boolean registerStarted = false;

	public static synchronized void onRegister() {
		if (registerStarted) return;
		registerStarted = true;

		ServiceProvider.callListeners(RegistrableServer.class, RegistrableServer::onRegister);
		ServiceProvider.callListeners(MultiplatformModInitializer.class, MultiplatformModInitializer::onRegister);

		CommonRegistryEvents.PRE_FREEZE.invoker().onPreFreeze();
		CommonRegistryEvents.PARTICLE.invoker().getIdentifiedParticles().forEach(particle -> {
			Registry.register(BuiltInRegistries.PARTICLE_TYPE, particle.id(), particle.particleType());
		});

		ServerPackSourceEvents.LOAD_PACK.register(consumer -> ModBuiltInPacks.loadPacks(consumer, PackType.SERVER_DATA, ServerPackSourceEvents.GATHER_PACKS.invoker().getDataPacks()));
		registered = true;
	}

	public static boolean isRegistered() {
		return registered;
	}

	public static void onInitialize() {
		LOGGER.info("Initializing {} on {}", MOD_ID, platform().loader());
		LOGGER.info("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
		oldRegister();
		ServiceProvider.callListeners(MultiplatformModInitializer.class, MultiplatformModInitializer::onInitialize);
		UpdateChecker.checkAll();
	}

	public static void oldRegister() {
		onRegister();

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

	public static boolean hasClient() {
		return clientAccessor != null;
	}

	public static ClientAccessor getClientAccessor() {
		return clientAccessor;
	}

	public static void setClientAccessor(ClientAccessor helper) {
		clientAccessor = helper;
	}

	public static boolean isLogicalSide() {
		if (clientAccessor == null) return true;
		return clientAccessor.isRunningIntegratedServer();
	}

	public static boolean isClientPlayer(UUID uuid) {
		return clientAccessor != null && clientAccessor.isMainClientPlayer(uuid);
	}
}
