package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class ServerNetworkEvents {

	/**
	 * Fires when the server receives a custom payload.
	 * Return `true` if packet was consumed, `false` otherwise.
	 */
	public static final Event<ReceiveCustomPacket> RECEIVE_CUSTOM_PACKET = EventFactory.createServer(ReceiveCustomPacket.class,
			listeners -> (customPacketPayload, player) -> EventFactory.dispatchReturn(listeners, false,  listener -> listener.onReceivePacket(customPacketPayload, player))
	);

	@FunctionalInterface
	public interface ReceiveCustomPacket {
		boolean onReceivePacket(CustomPacketPayload customPacketPayload, ServerPlayer player);
	}
}
