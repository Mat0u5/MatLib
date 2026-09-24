package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.client.multiplayer.ClientLevel;

public class ClientLevelEvents {
	/**
	 * Fires when the level ticks entities.
	 */
	public static final Event<TickEntities> TICK_ENTITIES = EventFactory.createClient(TickEntities.class,
			listeners -> level -> EventFactory.dispatch(listeners, listener -> listener.onTickEntities(level))
	).markLoud();
	@FunctionalInterface
	public interface TickEntities {
		void onTickEntities(ClientLevel level);
	}
}
