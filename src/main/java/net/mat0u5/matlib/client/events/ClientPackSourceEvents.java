package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.EventResult;
import net.minecraft.server.packs.repository.Pack;

import java.util.function.Consumer;

public class ClientPackSourceEvents {
	/**
	 * Fires when client resource packs begin loading.
	 */
	public static final Event<LoadPack> LOAD_PACK = EventFactory.createClient(LoadPack.class,
			listeners -> consumer -> EventFactory.dispatch(listeners, listener -> listener.onLoad(consumer))
	);

	/**
	 * Fires when client begins downloading a resource pack from the server
	 */
	public static final Event<ServerPackDownload> SERVER_PACK_DOWNLOAD = EventFactory.createClient(ServerPackDownload.class,
			listeners -> url -> EventFactory.dispatchResult(listeners, listener -> listener.onPackDownload(url))
	);

	@FunctionalInterface
	public interface LoadPack {
		void onLoad(Consumer<Pack> consumer);
	}
	@FunctionalInterface
	public interface ServerPackDownload {
		EventResult onPackDownload(String url);
	}
}
