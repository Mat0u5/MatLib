package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ClientNetworkEvents {

	/**
	 * Fires when the client receives a custom payload.
	 * Return `true` if packet was consumed, `false` otherwise.
	 */
	public static final Event<ReceiveCustomPacket> RECEIVE_CUSTOM_PACKET = EventFactory.createClient(ReceiveCustomPacket.class,
			listeners -> customPacketPayload -> EventFactory.dispatchReturn(listeners, false,  listener -> listener.onReceivePacket(customPacketPayload))
	);

	@FunctionalInterface
	public interface ReceiveCustomPacket {
		boolean onReceivePacket(CustomPacketPayload customPacketPayload);
	}

	/**
	 * Fires when the client receives the server handshake.
	 */
	public static final Event<ReceiveHandshake> RECEIVE_HANDSHAKE = EventFactory.createClient(ReceiveHandshake.class,
			listeners -> () -> EventFactory.dispatch(listeners,  listener -> listener.onReceiveHandshake())
	);

	@FunctionalInterface
	public interface ReceiveHandshake {
		void onReceiveHandshake();
	}
}
