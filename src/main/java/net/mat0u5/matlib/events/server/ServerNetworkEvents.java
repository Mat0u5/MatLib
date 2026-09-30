package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import java.util.List;
import java.util.UUID;

public class ServerNetworkEvents {

	/**
	 * Fires when the server receives a custom payload.
	 * Return `true` if packet was consumed, `false` otherwise.
	 */
	public static final Event<ReceiveCustomPacket> RECEIVE_CUSTOM_PACKET = EventFactory.createServer(ReceiveCustomPacket.class,
			listeners -> (customPacketPayload, player) -> {
				for (ReceiveCustomPacket listener : listeners) {
					if (listener.onReceivePacket(customPacketPayload, player)) {
						return true;
					}
				}
				return false;
			}
	);

	@FunctionalInterface
	public interface ReceiveCustomPacket {
		boolean onReceivePacket(CustomPacketPayload customPacketPayload, ServerPlayer player);
	}

	/**
	 * Fires when the server receives, or does not receive the pre-login packet from a player.
	 */
	public static final Event<PreLoginPacket> PRE_LOGIN_PACKET = EventFactory.createServer(PreLoginPacket.class,
			listeners -> (handler, uuid, username, understood, modIds) -> EventFactory.dispatch(listeners,  listener -> listener.onPreLoginPacket(handler, uuid, username, understood, modIds))
	);

	@FunctionalInterface
	public interface PreLoginPacket {
		void onPreLoginPacket(ServerLoginPacketListenerImpl handler, UUID uuid, String username, boolean understood, List<String> modIds);
	}
}
