package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public class CommonEntityEvents {
	/**
	 * Fires when an entity jumps.
	 */
	public static final Event<Jump> JUMP = EventFactory.createServer(Jump.class,
			listeners -> entity -> EventFactory.dispatch(listeners, listener -> listener.onJump(entity))
	);

	@FunctionalInterface
	public interface Jump {
		void onJump(LivingEntity entity);
	}
}
