package net.mat0u5.matlib.client;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.network.NetworkHandlerClient;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.services.ServiceProvider;
import net.mat0u5.matlib.client.services.MatLibClientInitializer;
import net.mat0u5.matlib.utils.interfaces.ClientAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

import static net.mat0u5.matlib.MatLib.*;

public class MatLibClient implements ClientAccessor {
	public static void onRegister() {
		ServiceProvider.callListeners(RegistrableClient.class, RegistrableClient::onRegister);
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

	public static boolean isClientPlayer(UUID uuid) {
		Minecraft client = Minecraft.getInstance();
		if (client == null) return false;
		if (client.player == null) return false;
		return client.player.getUUID().equals(uuid);
	}

	@Override
	public boolean isRunningIntegratedServer() {
		Minecraft client = Minecraft.getInstance();
		if (client == null) return false;
		return client.hasSingleplayerServer();
	}

	@Override
	public boolean isMainClientPlayer(UUID uuid) {
		return isClientPlayer(uuid);
	}

	@Override
	public void sendPacket(CustomPacketPayload payload) {
		NetworkHandlerClient.send(payload);
	}
	//? if neoforge {
    /*@Override
    public <T extends CustomPacketPayload> void handlePacket(T payload, IPayloadContext context) {
        NeoForgeClientNetworkRegistration.handleClientPacket(payload, context);
    }
    *///?}
}
