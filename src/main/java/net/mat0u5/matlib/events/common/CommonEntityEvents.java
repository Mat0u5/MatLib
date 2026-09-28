package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public class CommonEntityEvents {
	/**
	 * Fires when an entity jumps.
	 */
	public static final Event<Jump> JUMP = EventFactory.createCommon(Jump.class,
			listeners -> entity -> EventFactory.dispatch(listeners, listener -> listener.onJump(entity))
	);

	@FunctionalInterface
	public interface Jump {
		void onJump(LivingEntity entity);
	}

	/**
	 * Fires when the entity gets ticked.
	 */
	public static final Event<Tick> TICK = EventFactory.createCommon(Tick.class,
			listeners -> entity -> EventFactory.dispatch(listeners, listener -> listener.onTickStart(entity))
	).markLoud();

	@FunctionalInterface
	public interface Tick {
		void onTickStart(LivingEntity entity);
	}
}
