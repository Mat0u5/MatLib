package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;

public class ServerResourceEvents {
	public static final Event<ServerResourceEvents.ReloadStart> RELOAD_START = EventFactory.create(ServerResourceEvents.ReloadStart.class,
			listeners -> (server, manager) -> EventFactory.dispatch(listeners, listener -> listener.start(server, manager))
	);
	public static final Event<ServerResourceEvents.ReloadEnd> RELOAD_STOPPING = EventFactory.create(ServerResourceEvents.ReloadEnd.class,
			listeners -> (server, manager, success) -> EventFactory.dispatch(listeners, listener -> listener.end(server, manager, success))
	);

	@FunctionalInterface
	public interface ReloadStart {
		void start(MinecraftServer server, CloseableResourceManager resourceManager);
	}

	@FunctionalInterface
	public interface ReloadEnd {
		void end(MinecraftServer server, CloseableResourceManager resourceManager, boolean success);
	}
}
