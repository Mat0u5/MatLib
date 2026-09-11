package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.server.MinecraftServer;

public class ServerTickEvents {
	/**
	 * Fires at the start of the server tick.
	 */
	public static final Event<StartTick> START_TICK = EventFactory.create(StartTick.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.onTickStart(server))
	);

	/**
	 * Fires at the end of the server tick.
	 */
	public static final Event<EndTick> END_TICK = EventFactory.create(EndTick.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.onTickEnd(server))
	);
	
	@FunctionalInterface
	public interface StartTick {
		void onTickStart(MinecraftServer server);
	}
	
	@FunctionalInterface
	public interface EndTick {
		void onTickEnd(MinecraftServer server);
	}
}
