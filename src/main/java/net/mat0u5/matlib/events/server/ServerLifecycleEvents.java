package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.server.MinecraftServer;

public class ServerLifecycleEvents {

	/**
	 * Fires upon server beginning starting sequence.
	 */
	public static final Event<ServerStarting> SERVER_STARTING = EventFactory.createServer(ServerStarting.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.onStarting(server))
	);

	/**
	 * Fires upon server starting.
	 */
	public static final Event<ServerStarted> SERVER_STARTED = EventFactory.createServer(ServerStarted.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.onStarted(server))
	);

	/**
	 * Fires upon server detecting a stop.
	 */
	public static final Event<ServerStopping> SERVER_STOPPING = EventFactory.createServer(ServerStopping.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.onStopping(server))
	);

	/**
	 * Fires upon server fully stopped.
	 */
	public static final Event<ServerStopped> SERVER_STOPPED = EventFactory.createServer(ServerStopped.class,
			listeners -> server -> EventFactory.dispatch(listeners, listener -> listener.onStopped(server))
	);

	@FunctionalInterface
	public interface ServerStarting {
		void onStarting(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStarted {
		void onStarted(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopping {
		void onStopping(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopped {
		void onStopped(MinecraftServer server);
	}
}
