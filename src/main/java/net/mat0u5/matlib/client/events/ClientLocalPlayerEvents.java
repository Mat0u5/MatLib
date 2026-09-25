package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;

public class ClientLocalPlayerEvents extends ClientPlayerEvents {

	/**
	 * Fires when you join a world.
	 */
	public static final Event<Join> JOIN = EventFactory.createClient(Join.class,
			listeners -> packet -> EventFactory.dispatch(listeners, listener -> listener.onJoin(packet))
	);

	/**
	 * Fires when you leave a world.
	 */
	public static final Event<Leave> LEAVE = EventFactory.createClient(Leave.class,
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
	/**
	 * Fires at the start of the local player tick.
	 */
	public static final Event<StartTick> START_TICK = EventFactory.createClient(StartTick.class,
			listeners -> player -> EventFactory.dispatch(listeners, listener -> listener.onTickStart(player))
	).markLoud();

	/**
	 * Fires at the end of the local player tick.
	 */
	public static final Event<EndTick> END_TICK = EventFactory.createClient(EndTick.class,
			listeners -> player -> EventFactory.dispatch(listeners, listener -> listener.onTickEnd(player))
	).markLoud();

	@FunctionalInterface
	public interface StartTick {
		void onTickStart(LocalPlayer player);
	}

	@FunctionalInterface
	public interface EndTick {
		void onTickEnd(LocalPlayer player);
	}
}
