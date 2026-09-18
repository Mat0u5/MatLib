package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;

public class ClientPlayerEvents {


	/**
	 * Fires when you join a world.
	 */
	public static final Event<Join> JOIN = EventFactory.create(Join.class,
			listeners -> packet -> EventFactory.dispatch(listeners, listener -> listener.onJoin(packet))
	);

	/**
	 * Fires when you leave a world.
	 */
	public static final Event<Leave> LEAVE = EventFactory.create(Leave.class,
			listeners -> () -> EventFactory.dispatch(listeners, listener -> listener.onLeave())
	);

	@FunctionalInterface
	public interface Join {
		void onJoin(ClientboundLoginPacket packet);
	}

	@FunctionalInterface
	public interface Leave {
		void onLeave();
	}
}
