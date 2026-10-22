package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerNetworkEvents {

	/**
	 * Fires when the server receives a custom payload.
	 * Return `true` if packet was consumed, `false` otherwise.
	 */
	public static final Event<ReceiveCustomPacket> RECEIVE_CUSTOM_PACKET = EventFactory.createServer(ReceiveCustomPacket.class,
			listeners -> (customPacketPayload, player) -> {
				for (ReceiveCustomPacket listener : listeners) {
					try {
						if (listener.onReceivePacket(customPacketPayload, player)) {
							return true;
						}
					}
					catch (Exception e) {
						EventFactory.logListenerError(listener, e);
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
	public static final Event<PreLoginMatLibPacket> PRE_LOGIN_MATLIB_PACKET = EventFactory.createServer(PreLoginMatLibPacket.class,
			listeners -> (handler, uuid, username, understood, modIds) -> EventFactory.dispatch(listeners,  listener -> listener.onPreLoginMatLibPacket(handler, uuid, username, understood, modIds))
	);

	@FunctionalInterface
	public interface PreLoginMatLibPacket {
		void onPreLoginMatLibPacket(ServerLoginPacketListenerImpl handler, UUID uuid, String username, boolean understood, List<String> modIds);
	}

	/**
	 * Fires when the server is about send pre-login packets.
	 * <p>Return a map of transaction ids to packet ids, transaction ids must be unique across all listeners.
	 */
	public static final Event<SendPreLogin> SEND_PRELOGIN_PACKETS = EventFactory.createServer(SendPreLogin.class,
			listeners -> handler -> EventFactory.dispatchCollectMap(listeners, listener -> listener.onSendPreLogin(handler))
	);

	@FunctionalInterface
	public interface SendPreLogin {
		Map<Integer, Identifier> onSendPreLogin(ServerLoginPacketListenerImpl handler);
	}

	/**
	 * Fires when the server receives a pre-login packet answer.
	 * <p>Return true if packet is understood.
	 */
	public static final Event<ReceivePreLogin> RECEIVE_PRELOGIN_ANSWER = EventFactory.createServer(ReceivePreLogin.class,
			listeners -> (handler, id, data) -> EventFactory.dispatchReturn(listeners, false, listener -> listener.onReceivePreLogin(handler, id, data))
	);

	@FunctionalInterface
	public interface ReceivePreLogin {
		boolean onReceivePreLogin(ServerLoginPacketListenerImpl handler, Identifier id, FriendlyByteBuf data);
	}
}
