package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.utils.other.ModBuiltInPacks;
import net.minecraft.server.packs.repository.Pack;

import java.util.List;
import java.util.function.Consumer;

public class ServerPackSourceEvents {
	/**
	 * Fires when the server data packs begin loading.
	 */
	public static final Event<LoadPack> LOAD_PACK = EventFactory.createServer(LoadPack.class,
			listeners -> consumer -> EventFactory.dispatch(listeners, listener -> listener.onLoad(consumer))
	);

	@FunctionalInterface
	public interface LoadPack {
		void onLoad(Consumer<Pack> consumer);
	}

	/**
	 * Fires when the server reloads.
	 */
	public static final Event<GatherDataPacks> GATHER_PACKS = EventFactory.createServer(GatherDataPacks.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getDataPacks())
	);

	@FunctionalInterface
	public interface GatherDataPacks {
		List<ModBuiltInPacks.PackDef> getDataPacks();
	}
}
