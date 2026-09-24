package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.world.entity.Entity;

public class ClientEntityEvents {

	/**
	 * Fires when an entity ticks.
	 */
	public static final Event<TickEntity> TICK_ENTITY = EventFactory.createClient(TickEntity.class,
			listeners -> entity -> EventFactory.dispatch(listeners, listener -> listener.onTick(entity))
	).markLoud();

	@FunctionalInterface
	public interface TickEntity {
		void onTick(Entity entity);
	}
}
