package net.mat0u5.matlib.utils.interfaces;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.UUID;

//? if neoforge
//import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface ClientAccessor {
	boolean isRunningIntegratedServer();
	boolean isMainClientPlayer(UUID uuid);
	void sendPacket(CustomPacketPayload payload);

	//? if neoforge {
	/*<T extends CustomPacketPayload> void handlePacket(T payload, IPayloadContext context);
	 *///?}
}
