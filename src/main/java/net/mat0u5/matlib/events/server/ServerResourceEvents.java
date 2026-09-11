package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.CloseableResourceManager;

import java.util.Optional;

public class ServerResourceEvents {

	/**
	 * Fires upon a reload starting.
	 */
	public static final Event<ReloadStart> RELOAD_START = EventFactory.create(ReloadStart.class,
			listeners -> (server, manager) -> EventFactory.dispatch(listeners, listener -> listener.onStart(server, manager))
	);

	/**
	 * Fires upon a reload ending.
	 */
	public static final Event<ReloadEnd> RELOAD_STOPPING = EventFactory.create(ReloadEnd.class,
			listeners -> (server, manager, success) -> EventFactory.dispatch(listeners, listener -> listener.onEnd(server, manager, success))
	);

	/**
	 * Fires upon the server reading its resourcepack info.
	 * <p>This event returns the first non-null value returned by any listener, or original if not found.
	 */
	public static final Event<GetServerPack> GET_SERVER_PACK = EventFactory.create(GetServerPack.class,
			listeners -> originalServerPack -> EventFactory.dispatchReturn(listeners, listener -> listener.onGetServerPack(originalServerPack))
	);

	@FunctionalInterface
	public interface ReloadStart {
		void onStart(MinecraftServer server, CloseableResourceManager resourceManager);
	}

	@FunctionalInterface
	public interface ReloadEnd {
		void onEnd(MinecraftServer server, CloseableResourceManager resourceManager, boolean success);
	}

	@FunctionalInterface
	public interface GetServerPack {
		Optional<MinecraftServer.ServerResourcePackInfo> onGetServerPack(Optional<MinecraftServer.ServerResourcePackInfo> originalServerPack);
	}
}
