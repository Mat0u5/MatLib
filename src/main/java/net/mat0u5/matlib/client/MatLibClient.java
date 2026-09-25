package net.mat0u5.matlib.client;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.events.ClientRenderEvents;
import net.mat0u5.matlib.client.render.VignetteRenderer;
import net.mat0u5.matlib.services.ServiceProvider;
import net.mat0u5.matlib.client.services.MatLibClientInitializer;

import static net.mat0u5.matlib.MatLib.*;

public class MatLibClient {
	public static void onRegister() {
		ServiceProvider.callListeners(MatLibClientInitializer.class, MatLibClientInitializer::onRegister);
		ClientRenderEvents.RENDER_GUI.register((guiGraphics, deltaTracker) -> VignetteRenderer.renderVignette(guiGraphics));
	}

	public static void onInitializeClient() {
		MatLib.LOGGER.info("Initializing {} Client on {}", MOD_ID, platform().loader());
		MatLib.LOGGER.info("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
		oldRegister();
		ServiceProvider.callListeners(MatLibClientInitializer.class, MatLibClientInitializer::onInitializeClient);
	}

	public static void oldRegister() {
		//? fabric && <= 1.20.5 {
		/*onRegister();
		 *///?}
	}
}
