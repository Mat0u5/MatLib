package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.server.MinecraftServer;

public class ServerLifecycleEvents {

	/**
	 * Fires upon server beginning starting sequence.
	 */
	public static final Event<ServerStarting> SERVER_STARTING = EventFactory.create(ServerStarting.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.starting(server))
	);

	/**
	 * Fires upon server starting.
	 */
	public static final Event<ServerStarted> SERVER_STARTED = EventFactory.create(ServerStarted.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.started(server))
	);

	/**
	 * Fires upon server detecting a stop.
	 */
	public static final Event<ServerStopping> SERVER_STOPPING = EventFactory.create(ServerStopping.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.stopping(server))
	);

	@FunctionalInterface
	public interface ServerStarting {
		void starting(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStarted {
		void started(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopping {
		void stopping(MinecraftServer server);
	}
}
