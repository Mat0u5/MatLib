package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.server.packs.repository.Pack;

import java.util.function.Consumer;

public class ServerPackSourceEvents {
	/**
	 * Fires when client data packs begin loading.
	 */
	public static final Event<LoadPack> LOAD_PACK = EventFactory.create(LoadPack.class,
			listeners -> consumer -> EventFactory.dispatch(listeners, listener -> listener.onLoad(consumer))
	);

	@FunctionalInterface
	public interface LoadPack {
		void onLoad(Consumer<Pack> consumer);
	}
}
